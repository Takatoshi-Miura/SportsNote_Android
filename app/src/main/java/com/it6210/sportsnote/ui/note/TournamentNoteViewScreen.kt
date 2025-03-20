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
import com.it6210.sportsnote.model.Note
import com.it6210.sportsnote.ui.components.CustomAlertDialog
import com.it6210.sportsnote.ui.components.header.NavigationScreenHeader
import com.it6210.sportsnote.ui.components.items.AutoSaveTimestamp
import com.it6210.sportsnote.ui.note.components.TournamentNoteFormContent
import com.it6210.sportsnote.utils.Weather
import com.it6210.sportsnote.viewModel.NoteViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date

/**
 * 大会ノート詳細画面
 */
@Composable
fun TournamentNoteViewScreen(
    viewModel: NoteViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    noteId: String,
    onBack: () -> Unit,
    appBarNavigationIcon: MutableState<(@Composable () -> Unit)?>,
    appBarRightIcon: MutableState<(@Composable () -> Unit)?>,
) {
    val note: Note? = viewModel.getNoteById(noteId)
    val coroutineScope = rememberCoroutineScope()

    // 入力データの状態管理
    var date by remember { mutableStateOf(note?.date ?: Date()) }
    var weather by remember { mutableIntStateOf(note?.weather ?: Weather.SUNNY.id) }
    var temperature by remember { mutableIntStateOf(note?.temperature ?: 20) }
    var condition by remember { mutableStateOf(note?.condition ?: "") }
    var target by remember { mutableStateOf(note?.target ?: "") }
    var consciousness by remember { mutableStateOf(note?.condition ?: "") }
    var result by remember { mutableStateOf(note?.result ?: "") }
    var reflection by remember { mutableStateOf(note?.reflection ?: "") }
    var lastSavedAt by remember { mutableStateOf(note?.updated_at ?: Date()) }
    val showDialog = remember { mutableStateOf(false) }

    // 自動保存処理
    LaunchedEffect(date, weather, temperature, condition, target, consciousness, result, reflection) {
        delay(1000)
        val now = Date()
        viewModel.saveTournamentNote(
            noteId = noteId,
            date = date,
            weather = weather,
            temperature = temperature,
            condition = condition,
            target = target,
            consciousness = consciousness,
            result = result,
            reflection = reflection,
            created_at = note?.created_at ?: now,
        )
        lastSavedAt = now
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
                    viewModel.saveTournamentNote(
                        noteId = noteId,
                        date = date,
                        weather = weather,
                        temperature = temperature,
                        condition = condition,
                        target = target,
                        consciousness = consciousness,
                        result = result,
                        reflection = reflection,
                        created_at = note!!.created_at,
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
            TournamentNoteFormContent(
                note = note,
                onDateChange = { date = it },
                onWeatherChange = { weather = it },
                onTemperatureChange = { temperature = it },
                onConditionChange = { condition = it },
                onTargetChange = { target = it },
                onConsciousnessChange = { consciousness = it },
                onResultChange = { result = it },
                onReflectionChange = { reflection = it },
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
