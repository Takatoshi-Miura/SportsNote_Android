package com.example.sportsnote.ui.note

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.sportsnote.R
import com.example.sportsnote.ui.components.CustomSpacerColumn
import com.example.sportsnote.ui.components.DatePickerField
import com.example.sportsnote.ui.components.MultiLineTextInputField
import com.example.sportsnote.ui.components.TemperatureSlider
import com.example.sportsnote.ui.components.WeatherPickerField
import com.example.sportsnote.utils.Weather
import kotlinx.coroutines.launch
import java.util.Date

/**
 * 大会ノート追加画面
 */
@Composable
fun AddTournamentNoteScreen(
    viewModel: NoteViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    isNavigation: Boolean = false,
    onDismiss: () -> Unit,
) {
    if (isNavigation) {
        TournamentNoteContent(
            viewModel = viewModel,
            isNavigation = isNavigation,
            onDismiss = { onDismiss() }
        )
    } else {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            TournamentNoteContent(
                viewModel = viewModel,
                isNavigation = isNavigation,
                onDismiss = { onDismiss() }
            )
        }
    }
}

@Composable
fun TournamentNoteContent(
    viewModel: NoteViewModel,
    isNavigation: Boolean,
    onDismiss: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    // 入力データの状態管理
    var date = remember { mutableStateOf(Date()) }
    var weather = remember { mutableStateOf(Weather.SUNNY.id) }
    var temperature = remember { mutableStateOf(20) }
    var condition = remember { mutableStateOf("") }
    var target = remember { mutableStateOf("") }
    var consciousness = remember { mutableStateOf("") }
    var result = remember { mutableStateOf("") }
    var reflection = remember { mutableStateOf("") }

    // 入力項目
    val inputFields: List<@Composable () -> Unit> = listOf(
        { DatePickerField {selectedDate -> date.value = selectedDate} },
        { WeatherPickerField { selectedWeather -> weather.value = selectedWeather } },
        { TemperatureSlider { selectedTemperature -> temperature.value = selectedTemperature } },
        { MultiLineTextInputField("体調") { input -> condition.value = input } },
        { MultiLineTextInputField("目標") { input -> target.value = input } },
        { MultiLineTextInputField("意識すること") { input -> consciousness.value = input } },
        { MultiLineTextInputField("結果") { input -> result.value = input } },
        { MultiLineTextInputField("反省") { input -> reflection.value = input } }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // ヘッダー
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(MaterialTheme.colors.primary)
            ) {
                // 戻る
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 8.dp)
                ) {
                    val buttonName = if (isNavigation) "戻る" else stringResource(R.string.cancel)
                    Text(buttonName)
                }

                // タイトル
                Text(
                    text = "大会ノートの追加",
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.align(Alignment.Center)
                )

                // 保存or削除
                Button(
                    onClick = {
                        if (isNavigation) {
                            // TODO: 削除処理
                        } else {
                            coroutineScope.launch {
                                // 保存処理
                                viewModel.saveTournamentNote(
                                    date.value,
                                    weather.value,
                                    temperature.value,
                                    condition.value,
                                    target.value,
                                    consciousness.value,
                                    result.value,
                                    reflection.value
                                )
                            }
                        }
                        onDismiss()
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 8.dp)
                ) {
                    val buttonName = if (isNavigation) "削除" else stringResource(R.string.save)
                    Text(buttonName)
                }
            }

            // ノート内容
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                CustomSpacerColumn(
                    items = inputFields
                )
            }
        }
    }
}
