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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sportsnote.R
import com.example.sportsnote.model.PracticeNote
import com.example.sportsnote.model.TaskListData
import com.example.sportsnote.ui.components.CustomAlertDialog
import com.example.sportsnote.ui.components.CustomSpacerColumn
import com.example.sportsnote.ui.components.DatePickerField
import com.example.sportsnote.ui.components.MultiLineTextInputField
import com.example.sportsnote.ui.components.TaskSelectionDialog
import com.example.sportsnote.ui.components.TemperatureSlider
import com.example.sportsnote.ui.components.WeatherPickerField
import com.example.sportsnote.ui.memo.MemoViewModel
import com.example.sportsnote.ui.task.TaskViewModel
import com.example.sportsnote.utils.Weather
import kotlinx.coroutines.launch
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
 * @param onTaskReflectionsChange 課題メモ変更時の処理
 */
@Composable
fun PracticeNoteFormContent(
    note: PracticeNote? = null,
    onDateChange: (Date) -> Unit,
    onWeatherChange: (Int) -> Unit,
    onTemperatureChange: (Int) -> Unit,
    onConditionChange: (String) -> Unit,
    onPurposeChange: (String) -> Unit,
    onDetailChange: (String) -> Unit,
    onReflectionChange: (String) -> Unit,
    onTaskReflectionsChange: (Map<TaskListData, String>) -> Unit
) {
    val date = remember { mutableStateOf(note?.date ?: Date()) }
    val weather = remember { mutableStateOf(note?.weather ?: Weather.SUNNY.id) }
    val temperature = remember { mutableStateOf(note?.temperature ?: 20) }
    val condition = remember { mutableStateOf(note?.condition ?: "") }
    val purpose = remember { mutableStateOf(note?.purpose ?: "") }
    val detail = remember { mutableStateOf(note?.detail ?: "") }
    val reflection = remember { mutableStateOf(note?.reflection ?: "") }

    // `note` があればその `taskReflections` を、なければ全課題リストをデフォルトの Map にする
    val taskViewModel = TaskViewModel()
    val taskLists by taskViewModel.taskLists.collectAsState()
    val taskListMap: Map<TaskListData, String> = note?.taskReflections ?: taskLists.associateWith { "" }
    val taskListState = remember { mutableStateOf(taskListMap) }

    // ノートに未追加の課題を取得
    val allTasks by taskViewModel.taskLists.collectAsState(emptyList())
    val unaddedTasks = remember(taskListState.value, allTasks) {
        allTasks.filter { it !in taskListState.value }
    }

    val showDialog = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

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
                    onClick = { showDialog.value = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Task"
                    )
                }
            }
        },
        {
            TaskListInput(
                taskDataList = taskListState.value,
                onTaskRemoved = { task ->
                    if (task.memoID != null) {
                        coroutineScope.launch {
                            val memoViewModel = MemoViewModel()
                            memoViewModel.deleteMemo(task.memoID!!)
                        }
                    }
                    taskListState.value -= task
                },
                onReflectionChanged = { task, reflection ->
                    taskListState.value += (task to reflection)
                }
            )
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
        onTaskReflectionsChange(taskListState.value)
    }

    // 課題追加ダイアログ
    if (showDialog.value) {
        TaskSelectionDialog(
            tasks = unaddedTasks,
            onTaskSelected = { selectedTask ->
                taskListState.value += selectedTask to ""
                showDialog.value = false
            },
            onDismiss = { showDialog.value = false }
        )
    }
}

/**
 * 取り組んだ課題セルのコンポーネント
 *
 * @param taskDataList 取り組んだ課題データとそのメモのMap
 * @param onTaskRemoved 課題削除処理
 * @param onReflectionChanged メモ変更時の処理
 */
@Composable
fun TaskListInput(
    taskDataList: Map<TaskListData, String>, // 課題と入力テキストのマップ
    onTaskRemoved: (TaskListData) -> Unit,
    onReflectionChanged: (TaskListData, String) -> Unit
) {
    val showDialog = remember { mutableStateOf(false) }
    val selectedTask = remember { mutableStateOf<TaskListData?>(null) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Divider()
        taskDataList.forEach { (taskData, reflectionText) ->
            TaskInputItem(
                taskData = taskData,
                initialReflection = reflectionText,
                onOptionClick = {
                    selectedTask.value = taskData
                    showDialog.value = true
                },
                onReflectionChanged = { updatedText ->
                    onReflectionChanged(taskData, updatedText)
                }
            )
            Divider()
        }
    }

    // 削除ダイアログ
    if (showDialog.value) {
        CustomAlertDialog(
            title = stringResource(R.string.deleteTaskFromNote),
            message = stringResource(R.string.deleteTaskFromNoteMessage),
            onConfirm = {
                selectedTask.value?.let { onTaskRemoved(it) }
                showDialog.value = false
            },
            showDialog = showDialog
        )
    }
}

/**
 * 取り組んだ課題セルのコンポーネント
 *
 * @param taskData 課題データ
 * @param initialReflection メモの初期値
 * @param onOptionClick オプションボタン押下時の処理
 * @param onReflectionChanged メモ変更時の処理
 */
@Composable
fun TaskInputItem(
    taskData: TaskListData,
    initialReflection: String,
    onOptionClick: () -> Unit,
    onReflectionChanged: (String) -> Unit
) {
    val reflectionText = remember { mutableStateOf(initialReflection) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // グループカラー
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
                // 課題タイトル
                Text(
                    text = taskData.title,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                // 最優先の対策
                Text(
                    text = stringResource(R.string.measuresLabel, taskData.measures),
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
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
        // メモ欄
        MultiLineTextInputField(
            title = "",
            placeholder = "入力してください",
            onTextChanged = { text ->
                reflectionText.value = text
                onReflectionChanged(text)
            },
            initialText = reflectionText.value
        )
    }
}