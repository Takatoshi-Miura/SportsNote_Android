package com.example.sportsnote.ui.note

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.sportsnote.R
import com.example.sportsnote.model.Note
import com.example.sportsnote.model.TaskListData
import com.example.sportsnote.ui.components.CustomAlertDialog
import com.example.sportsnote.ui.components.header.NavigationScreenHeader
import com.example.sportsnote.utils.Weather
import kotlinx.coroutines.launch
import java.util.Date

/**
 * 練習ノート詳細画面
 *
 * @param noteId ノートID
 * @param onBack 前画面に戻る時の処理
 * @param appBarNavigationIcon ヘッダ左側のアイコン
 * @param appBarRightIcon ヘッダ右側のアイコン
 */
@Composable
fun PracticeNoteViewScreen(
    noteId: String,
    onBack: () -> Unit,
    appBarNavigationIcon: MutableState<(@Composable () -> Unit)?>,
    appBarRightIcon: MutableState<(@Composable () -> Unit)?>
) {
    val viewModel = NoteViewModel()
    val note: Note? = viewModel.getNoteById(noteId)
    val coroutineScope = rememberCoroutineScope()
    val showDialog = remember { mutableStateOf(false) }

    // 入力データの状態管理
    val date = remember { mutableStateOf(Date()) }
    val weather = remember { mutableIntStateOf(Weather.SUNNY.id) }
    val temperature = remember { mutableIntStateOf(20) }
    val condition = remember { mutableStateOf("") }
    val purpose = remember { mutableStateOf("") }
    val detail = remember { mutableStateOf("") }
    val reflection = remember { mutableStateOf("") }
    val taskReflections = remember { mutableStateOf<Map<TaskListData, String>>(emptyMap()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // ヘッダー
            NavigationScreenHeader(
                onDismiss = onBack,
                onSave = {
                    viewModel.savePracticeNote(
                        noteId = noteId,
                        date = date.value,
                        weather = weather.intValue,
                        temperature = temperature.intValue,
                        condition = condition.value,
                        purpose = purpose.value,
                        detail = detail.value,
                        reflection = reflection.value,
                        taskReflections = taskReflections.value
                    )
                },
                onDelete = {
                    showDialog.value = true
                },
                updateAppBar = { navigationIcon, rightIcon ->
                    appBarNavigationIcon.value = navigationIcon
                    appBarRightIcon.value = rightIcon
                }
            )

            // 共通フォーム
            PracticeNoteFormContent(
                note = note,
                onDateChange = { selectedDate -> date.value = selectedDate },
                onWeatherChange = { selectedWeather -> weather.intValue = selectedWeather },
                onTemperatureChange = { selectedTemperature -> temperature.intValue = selectedTemperature },
                onConditionChange = { updatedCondition -> condition.value = updatedCondition },
                onPurposeChange = { updatePurpose -> purpose.value = updatePurpose },
                onDetailChange = { updateDetail -> detail.value = updateDetail },
                onReflectionChange = { updatedReflection -> reflection.value = updatedReflection },
                onTaskReflectionsChange = { taskReflections.value = it }
            )
        }
    }

    // 削除確認ダイアログの表示
    if (showDialog.value) {
        CustomAlertDialog(
            title = stringResource(R.string.deleteNote),
            message = stringResource(R.string.deleteNoteMessage),
            confirmButtonText = stringResource(R.string.delete),
            onConfirm = {
                coroutineScope.launch {
                    viewModel.deleteNote(noteId)
                    showDialog.value = false
                    onBack()
                }
            },
            showDialog = showDialog
        )
    }
}