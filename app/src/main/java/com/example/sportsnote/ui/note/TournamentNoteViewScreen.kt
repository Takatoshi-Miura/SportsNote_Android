package com.example.sportsnote.ui.note

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.sportsnote.model.Note
import com.example.sportsnote.ui.components.header.NavigationScreenHeader
import com.example.sportsnote.utils.Weather
import java.util.Date

/**
 * 大会ノート追加画面
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

    // 入力データの状態管理
    var date = remember { mutableStateOf(Date()) }
    var weather = remember { mutableStateOf(Weather.SUNNY.id) }
    var temperature = remember { mutableStateOf(20) }
    var condition = remember { mutableStateOf("") }
    var target = remember { mutableStateOf("") }
    var consciousness = remember { mutableStateOf("") }
    var result = remember { mutableStateOf("") }
    var reflection = remember { mutableStateOf("") }

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
                        noteId = note!!.noteID,
                        date.value,
                        weather.value,
                        temperature.value,
                        condition.value,
                        target.value,
                        consciousness.value,
                        result.value,
                        reflection.value
                    )
                },
                onDelete = {
//                    viewModel.de
                },
                updateAppBar = { navigationIcon, rightIcon ->
                    appBarNavigationIcon.value = navigationIcon
                    appBarRightIcon.value = rightIcon
                }
            )

            // 共通フォーム
            TournamentNoteFormContent(
                note = note,
                onDateChange = { date.value = it },
                onWeatherChange = { weather.value = it },
                onTemperatureChange = { temperature.value = it },
                onConditionChange = { condition.value = it },
                onTargetChange = { target.value = it },
                onConsciousnessChange = { consciousness.value = it },
                onResultChange = { result.value = it },
                onReflectionChange = { reflection.value = it }
            )
        }
    }
}