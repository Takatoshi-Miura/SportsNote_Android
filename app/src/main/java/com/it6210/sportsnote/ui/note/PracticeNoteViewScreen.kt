package com.it6210.sportsnote.ui.note

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.it6210.sportsnote.R
import com.it6210.sportsnote.model.PracticeNote
import com.it6210.sportsnote.model.TaskListData
import com.it6210.sportsnote.ui.components.CustomAlertDialog
import com.it6210.sportsnote.ui.components.header.NavigationScreenHeader
import com.it6210.sportsnote.ui.components.items.AutoSaveTimestamp
import com.it6210.sportsnote.ui.note.components.PracticeNoteFormContent
import com.it6210.sportsnote.viewModel.NoteViewModel
import kotlinx.coroutines.delay
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
    appBarRightIcon: MutableState<(@Composable () -> Unit)?>,
) {
    val viewModel = NoteViewModel()
    val note: PracticeNote = viewModel.getPracticeNote(noteId = noteId)
    val coroutineScope = rememberCoroutineScope()
    val showDialog = remember { mutableStateOf(false) }

    // 入力データの状態管理
    var date by remember { mutableStateOf(note.date) }
    var weather by remember { mutableIntStateOf(note.weather) }
    var temperature by remember { mutableIntStateOf(note.temperature) }
    var condition by remember { mutableStateOf(note.condition) }
    var purpose by remember { mutableStateOf(note.purpose) }
    var detail by remember { mutableStateOf(note.detail) }
    var reflection by remember { mutableStateOf(note.reflection) }
    var taskReflections by remember { mutableStateOf<Map<TaskListData, String>>(emptyMap()) }
    var lastSavedAt by remember { mutableStateOf(note.updated_at) }

    // 自動保存処理
    LaunchedEffect(date, weather, temperature, condition, purpose, detail, reflection, taskReflections) {
        delay(1000)
        viewModel.savePracticeNote(
            noteId = noteId,
            date = date,
            weather = weather,
            temperature = temperature,
            condition = condition,
            purpose = purpose,
            detail = detail,
            reflection = reflection,
            taskReflections = taskReflections,
            created_at = note.created_at,
        )
        lastSavedAt = Date()
    }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.surface),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // ヘッダー
            NavigationScreenHeader(
                onDismiss = onBack,
                onSave = {
                    viewModel.savePracticeNote(
                        noteId = noteId,
                        date = date,
                        weather = weather,
                        temperature = temperature,
                        condition = condition,
                        purpose = purpose,
                        detail = detail,
                        reflection = reflection,
                        taskReflections = taskReflections,
                        created_at = note.created_at,
                    )
                },
                onDelete = {
                    showDialog.value = true
                },
                onEdit = null,
                updateAppBar = { navigationIcon, rightIcon ->
                    appBarNavigationIcon.value = navigationIcon
                    appBarRightIcon.value = rightIcon
                },
            )

            AutoSaveTimestamp(lastSavedAt)

            // 共通フォーム
            PracticeNoteFormContent(
                note = note,
                onDateChange = { date = it },
                onWeatherChange = { weather = it },
                onTemperatureChange = { temperature = it },
                onConditionChange = { condition = it },
                onPurposeChange = { purpose = it },
                onDetailChange = { detail = it },
                onReflectionChange = { reflection = it },
                onTaskReflectionsChange = { taskReflections = it },
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
            showDialog = showDialog,
        )
    }
}
