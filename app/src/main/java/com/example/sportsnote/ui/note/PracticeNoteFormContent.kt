package com.example.sportsnote.ui.note

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sportsnote.R
import com.example.sportsnote.model.Note
import com.example.sportsnote.model.TaskListData
import com.example.sportsnote.ui.components.CustomSpacerColumn
import com.example.sportsnote.ui.components.DatePickerField
import com.example.sportsnote.ui.components.MultiLineTextInputField
import com.example.sportsnote.ui.components.TemperatureSlider
import com.example.sportsnote.ui.components.WeatherPickerField
import com.example.sportsnote.ui.task.TaskViewModel
import com.example.sportsnote.utils.Weather
import java.util.Date

/**
 * 練習ノートの共通フォーム
 *
 * @param note Noteオブジェクト（nullならデフォルト値を設定）
 * @param onDateChange 日付変更時の処理
 * @param onWeatherChange 天気変更時の処理
 * @param onTemperatureChange 温度変更時の処理
 * @param onConditionChange 体調変更時の処理
 * @param onPurposeChange 練習の目的変更時の処理
 * @param onDetailChange 練習内容変更時の処理
 * @param onReflectionChange 反省変更時の処理
 */
@Composable
fun PracticeNoteFormContent(
    note: Note? = null,
    onDateChange: (Date) -> Unit,
    onWeatherChange: (Int) -> Unit,
    onTemperatureChange: (Int) -> Unit,
    onConditionChange: (String) -> Unit,
    onPurposeChange: (String) -> Unit,
    onDetailChange: (String) -> Unit,
    onReflectionChange: (String) -> Unit
) {
    val date = remember { mutableStateOf(note?.date ?: Date()) }
    val weather = remember { mutableStateOf(note?.weather ?: Weather.SUNNY.id) }
    val temperature = remember { mutableStateOf(note?.temperature ?: 20) }
    val condition = remember { mutableStateOf(note?.condition ?: "") }
    val purpose = remember { mutableStateOf(note?.target ?: "") }
    val detail = remember { mutableStateOf(note?.consciousness ?: "") }
    val reflection = remember { mutableStateOf(note?.reflection ?: "") }

    // TODO: ノート詳細なら当時取り込んだ課題を取得
    val taskViewModel = TaskViewModel()
    val taskList = taskViewModel.taskLists

    val inputFields: List<@Composable () -> Unit> = listOf(
        // 日付
        {
            DatePickerField(
                initialDate = date.value,
                onDateSelected = { selectedDate -> date.value = selectedDate }
            )
        },
        // 天気
        {
            WeatherPickerField(
                initialWeather = weather.value,
                onWeatherSelected = { selectedWeather -> weather.value = selectedWeather }
            )
        },
        // 気温
        {
            TemperatureSlider(
                initialTemperature = temperature.value,
                onTemperatureSelected = { selectedTemperature -> temperature.value = selectedTemperature }
            )
        },
        // 体調
        {
            MultiLineTextInputField(
                title = stringResource(R.string.condition),
                onTextChanged = { input -> condition.value = input },
                initialText = condition.value
            )
        },
        // 練習の目的
        {
            MultiLineTextInputField(
                title = stringResource(R.string.practicePurpoce),
                onTextChanged = { input -> purpose.value = input },
                initialText = purpose.value
            )
        },
        // 練習内容
        {
            MultiLineTextInputField(
                title = stringResource(R.string.practiceDetail),
                onTextChanged = { input -> detail.value = input },
                initialText = detail.value
            )
        },
        // 取り組んだ課題
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "取り組んだ課題",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = { } // TODO: ノートに取り込まれてない未解決の課題リストを表示
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Task"
                    )
                }
            }
        },
        {
            TaskListInput(taskDataList = taskList.value)
        },
        // 反省
        {
            MultiLineTextInputField(
                title = stringResource(R.string.reflection),
                onTextChanged = { input -> reflection.value = input },
                initialText = reflection.value
            )
        }
    )

    // 入力フォーム
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        CustomSpacerColumn(items = inputFields)

        onDateChange(date.value)
        onWeatherChange(weather.value)
        onTemperatureChange(temperature.value)
        onConditionChange(condition.value)
        onPurposeChange(purpose.value)
        onDetailChange(detail.value)
        onReflectionChange(reflection.value)
    }
}

@Composable
fun TaskListInput(
    taskDataList: List<TaskListData>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Divider()
        taskDataList.forEach { taskData ->
            TaskInputItem(taskData) {}
            Divider()
        }
    }
}

@Composable
fun TaskInputItem(
    taskData: TaskListData,
    onOptionClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .width(20.dp)
                    .height(50.dp)
                    .background(taskData.groupColor)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = taskData.title, fontSize = 16.sp)
                Text(
                    text = stringResource(R.string.measuresLabel, taskData.measures),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            // オプションボタン
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Options",
                modifier = Modifier
                    .padding(end = 12.dp)
                    .clickable { onOptionClick() }
            )
        }

        val reflectionText = remember { mutableStateOf("") }
        MultiLineTextInputField(
            title = "",
            placeholder = "入力してください",
            onTextChanged = { reflectionText.value = it },
            initialText = reflectionText.value
        )
    }
}