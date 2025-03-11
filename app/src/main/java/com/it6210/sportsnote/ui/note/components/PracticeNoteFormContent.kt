package com.it6210.sportsnote.ui.note.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.it6210.sportsnote.R
import com.it6210.sportsnote.model.PracticeNote
import com.it6210.sportsnote.model.TaskListData
import com.it6210.sportsnote.ui.components.TaskSelectionDialog
import com.it6210.sportsnote.ui.components.items.CustomSpacerColumn
import com.it6210.sportsnote.ui.components.items.MultiLineTextInputField
import com.it6210.sportsnote.ui.memo.MemoViewModel
import com.it6210.sportsnote.ui.task.TaskViewModel
import com.it6210.sportsnote.utils.Weather
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
    onTaskReflectionsChange: (Map<TaskListData, String>) -> Unit,
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
    val taskLists by taskViewModel.taskListsForNote.collectAsState()
    val taskListMap: Map<TaskListData, String> = note?.taskReflections ?: taskLists.associateWith { "" }
    val taskListState = remember { mutableStateOf(taskListMap) }

    // ノートに未追加の課題を取得
    val allTasks by taskViewModel.taskListsForNote.collectAsState()
    val unaddedTasks =
        remember(taskListState.value, allTasks) {
            derivedStateOf {
                val taskIDMap = taskListState.value.keys.map { it.taskID }.toSet()
                allTasks.filter { it.taskID !in taskIDMap }
            }
        }

    val showDialog = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val inputFields: List<@Composable () -> Unit> =
        listOf(
            // 日付
            {
                DatePickerField(
                    initialDate = date.value,
                    onDateSelected = { selectedDate -> date.value = selectedDate },
                )
            },
            // 天気
            {
                WeatherPickerField(
                    initialWeather = weather.value,
                    onWeatherSelected = { selectedWeather -> weather.value = selectedWeather },
                )
            },
            // 気温
            {
                TemperatureSlider(
                    initialTemperature = temperature.value,
                    onTemperatureSelected = { selectedTemperature -> temperature.value = selectedTemperature },
                )
            },
            // 体調
            {
                MultiLineTextInputField(
                    title = stringResource(R.string.condition),
                    onTextChanged = { input -> condition.value = input },
                    initialText = condition.value,
                )
            },
            // 練習の目的
            {
                MultiLineTextInputField(
                    title = stringResource(R.string.practicePurpoce),
                    onTextChanged = { input -> purpose.value = input },
                    initialText = purpose.value,
                )
            },
            // 練習内容
            {
                MultiLineTextInputField(
                    title = stringResource(R.string.practiceDetail),
                    onTextChanged = { input -> detail.value = input },
                    initialText = detail.value,
                )
            },
            // 取り組んだ課題
            {
                TaskHeader(
                    title = stringResource(R.string.doneTask),
                    onAddClick = { showDialog.value = true },
                )
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
                    },
                )
            },
            // 反省
            {
                MultiLineTextInputField(
                    title = stringResource(R.string.reflection),
                    onTextChanged = { input -> reflection.value = input },
                    initialText = reflection.value,
                )
            },
        )

    // 入力フォーム
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
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
            tasks = unaddedTasks.value,
            onTaskSelected = { selectedTask ->
                taskListState.value += selectedTask to ""
                showDialog.value = false
            },
            onDismiss = { showDialog.value = false },
        )
    }
}
