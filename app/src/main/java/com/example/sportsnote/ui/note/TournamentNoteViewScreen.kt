package com.example.sportsnote.ui.note

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.sportsnote.R
import com.example.sportsnote.model.Note
import com.example.sportsnote.ui.components.CustomAlertDialog
import com.example.sportsnote.ui.components.header.NavigationScreenHeader
import com.example.sportsnote.utils.Weather
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
    appBarRightIcon: MutableState<(@Composable () -> Unit)?>
) {
    val note: Note? = viewModel.getNoteById(noteId)
    val coroutineScope = rememberCoroutineScope()

    // 入力データの状態管理
    var date  by remember { mutableStateOf(Date()) }
    var weather by remember { mutableStateOf(Weather.SUNNY.id) }
    var temperature by remember { mutableStateOf(20) }
    var condition by remember { mutableStateOf("") }
    var target by remember { mutableStateOf("") }
    var consciousness by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var reflection by remember { mutableStateOf("") }
    val showDialog = remember { mutableStateOf(false) }

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
                    viewModel.saveTournamentNote(
                        noteId = noteId,
                        date = date,
                        weather = weather,
                        temperature = temperature,
                        condition = condition,
                        target = target,
                        consciousness = consciousness,
                        result = result,
                        reflection = reflection
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
            TournamentNoteFormContent(
                note = note,
                onDateChange = { date = it },
                onWeatherChange = { weather = it },
                onTemperatureChange = { temperature = it },
                onConditionChange = { condition = it },
                onTargetChange = { target = it },
                onConsciousnessChange = { consciousness = it },
                onResultChange = { result = it },
                onReflectionChange = { reflection = it }
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