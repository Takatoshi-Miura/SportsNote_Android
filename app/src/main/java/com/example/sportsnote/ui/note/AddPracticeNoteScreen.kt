package com.example.sportsnote.ui.note

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.sportsnote.R
import com.example.sportsnote.model.TaskListData
import com.example.sportsnote.ui.components.header.AddScreenHeader
import com.example.sportsnote.utils.Weather
import java.util.Date

/**
 * 練習ノート追加画面
 *
 * @param onDismiss 閉じる時の処理
 */
@Composable
fun AddPracticeNoteScreen(onDismiss: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()

    // 入力データの状態管理
    val date = remember { mutableStateOf(Date()) }
    val weather = remember { mutableStateOf(Weather.SUNNY.id) }
    val temperature = remember { mutableStateOf(20) }
    val condition = remember { mutableStateOf("") }
    val purpose = remember { mutableStateOf("") }
    val detail = remember { mutableStateOf("") }
    val reflection = remember { mutableStateOf("") }
    val taskReflections = remember { mutableStateOf<Map<TaskListData, String>>(emptyMap()) }

    Dialog(
        onDismissRequest = onDismiss,
        properties =
            DialogProperties(
                usePlatformDefaultWidth = false,
            ),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.surface),
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // ヘッダー
                AddScreenHeader(
                    title = stringResource(R.string.addPracticeNote),
                    onCancel = onDismiss,
                    onSave = {
                        // 保存処理
                        val noteViewModel = NoteViewModel()
                        noteViewModel.savePracticeNote(
                            date = date.value,
                            weather = weather.value,
                            temperature = temperature.value,
                            condition = condition.value,
                            purpose = purpose.value,
                            detail = detail.value,
                            reflection = reflection.value,
                            taskReflections = taskReflections.value,
                        )
                        onDismiss()
                    },
                    coroutineScope = coroutineScope,
                )

                // 共通フォーム
                PracticeNoteFormContent(
                    note = null,
                    onDateChange = { selectedDate -> date.value = selectedDate },
                    onWeatherChange = { selectedWeather -> weather.value = selectedWeather },
                    onTemperatureChange = { selectedTemperature -> temperature.value = selectedTemperature },
                    onConditionChange = { updatedCondition -> condition.value = updatedCondition },
                    onPurposeChange = { updatePurpose -> purpose.value = updatePurpose },
                    onDetailChange = { updateDetail -> detail.value = updateDetail },
                    onReflectionChange = { updatedReflection -> reflection.value = updatedReflection },
                    onTaskReflectionsChange = { taskReflections.value = it },
                )
            }
        }
    }
}
