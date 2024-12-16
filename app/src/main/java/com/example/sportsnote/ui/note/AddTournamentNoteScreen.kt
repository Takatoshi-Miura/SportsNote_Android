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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.sportsnote.ui.components.CustomSpacerColumn
import com.example.sportsnote.ui.components.DatePickerField
import com.example.sportsnote.ui.components.MultiLineTextInputField
import com.example.sportsnote.ui.components.TemperatureSlider
import com.example.sportsnote.ui.components.WeatherPickerField

/**
 * 大会ノート追加画面
 */
@Composable
fun AddTournamentNoteScreen(onDismiss: () -> Unit) {
    // 入力欄間のスペース
    val spacerHeight = 4.dp

    // 入力項目
    val inputFields: List<@Composable () -> Unit> = listOf(
        { DatePickerField() },
        { WeatherPickerField() },
        { TemperatureSlider() },
        { MultiLineTextInputField("体調") },
        { MultiLineTextInputField("目標") },
        { MultiLineTextInputField("意識すること") },
        { MultiLineTextInputField("結果") },
        { MultiLineTextInputField("反省") }
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false // デフォルトの幅制限を無効化
        )
    ) {
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
                    // キャンセルボタン
                    Button(
                        onClick = { onDismiss() },
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 8.dp)
                    ) {
                        Text("キャンセル")
                    }

                    // タイトル
                    Text(
                        text = "大会ノートの追加",
                        color = MaterialTheme.colors.onPrimary,
                        modifier = Modifier.align(Alignment.Center)
                    )

                    // 保存ボタン
                    Button(
                        onClick = {
                            // TODO: 保存処理を呼び出し
                            onDismiss()
                        },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 8.dp)
                    ) {
                        Text("保存")
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    CustomSpacerColumn(
                        items = inputFields,
                        spacerHeight = spacerHeight
                    )
                }
            }
        }
    }
}
