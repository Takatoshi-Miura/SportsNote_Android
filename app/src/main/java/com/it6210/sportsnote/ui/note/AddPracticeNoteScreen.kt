package com.it6210.sportsnote.ui.note

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.it6210.sportsnote.R
import com.it6210.sportsnote.model.TaskListData
import com.it6210.sportsnote.ui.components.header.AddScreenHeader
import com.it6210.sportsnote.ui.note.components.PracticeNoteFormContent
import com.it6210.sportsnote.utils.Weather
import com.it6210.sportsnote.viewModel.NoteViewModel
import java.util.Date

/**
 * 練習ノート追加画面
 *
 * @param onDismiss 閉じる時の処理
 */
@Composable
fun AddPracticeNoteScreen(onDismiss: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // 入力データの状態管理
    val date = remember { mutableStateOf(Date()) }
    val weather = remember { mutableIntStateOf(Weather.SUNNY.id) }
    val temperature = remember { mutableIntStateOf(20) }
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
                            weather = weather.intValue,
                            temperature = temperature.intValue,
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

                // スクロール可能なコンテンツ領域
                Column(
                    modifier =
                        Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                ) {
                    // 共通フォーム
                    PracticeNoteFormContent(
                        modifier = Modifier.verticalScroll(scrollState),
                        note = null,
                        onDateChange = { selectedDate -> date.value = selectedDate },
                        onWeatherChange = { selectedWeather -> weather.intValue = selectedWeather },
                        onTemperatureChange = { selectedTemperature -> temperature.intValue = selectedTemperature },
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
}
