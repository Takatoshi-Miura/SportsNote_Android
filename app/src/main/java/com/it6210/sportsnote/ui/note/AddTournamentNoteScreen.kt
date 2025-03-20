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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.it6210.sportsnote.R
import com.it6210.sportsnote.ui.components.header.AddScreenHeader
import com.it6210.sportsnote.ui.note.components.TournamentNoteFormContent
import com.it6210.sportsnote.utils.Weather
import com.it6210.sportsnote.viewModel.NoteViewModel
import java.util.Date

/**
 * 大会ノート追加画面
 *
 * @param viewModel NoteViewModel
 * @param onDismiss 閉じる時の処理
 */
@Composable
fun AddTournamentNoteScreen(
    viewModel: NoteViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onDismiss: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    
    // 入力データの状態管理
    val date = remember { mutableStateOf(Date()) }
    val weather = remember { mutableStateOf(Weather.SUNNY.id) }
    val temperature = remember { mutableStateOf(20) }
    val condition = remember { mutableStateOf("") }
    val target = remember { mutableStateOf("") }
    val consciousness = remember { mutableStateOf("") }
    val result = remember { mutableStateOf("") }
    val reflection = remember { mutableStateOf("") }
    
    Dialog(
        onDismissRequest = onDismiss,
        properties =
            DialogProperties(
                usePlatformDefaultWidth = false,
            ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.surface),
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // ヘッダー
                AddScreenHeader(
                    title = stringResource(R.string.addTournamentNote),
                    onCancel = onDismiss,
                    onSave = {
                        // 保存処理
                        viewModel.saveTournamentNote(
                            date = date.value,
                            weather = weather.value,
                            temperature = temperature.value,
                            condition = condition.value,
                            target = target.value,
                            consciousness = consciousness.value,
                            result = result.value,
                            reflection = reflection.value,
                        )
                        onDismiss()
                    },
                    coroutineScope = coroutineScope,
                )
                
                // スクロール可能なコンテンツ領域
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    // 共通フォーム
                    TournamentNoteFormContent(
                        modifier = Modifier.verticalScroll(scrollState),
                        note = null,
                        onDateChange = { selectedDate -> date.value = selectedDate },
                        onWeatherChange = { selectedWeather -> weather.value = selectedWeather },
                        onTemperatureChange = { selectedTemperature -> temperature.value = selectedTemperature },
                        onConditionChange = { updatedCondition -> condition.value = updatedCondition },
                        onTargetChange = { updatedTarget -> target.value = updatedTarget },
                        onConsciousnessChange = { updatedConsciousness -> consciousness.value = updatedConsciousness },
                        onResultChange = { updatedResult -> result.value = updatedResult },
                        onReflectionChange = { updatedReflection -> reflection.value = updatedReflection },
                    )
                }
            }
        }
    }
}
