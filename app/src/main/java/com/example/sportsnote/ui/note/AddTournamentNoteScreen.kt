package com.example.sportsnote.ui.note

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.sportsnote.ui.components.MultiLineTextInputField
import java.util.Calendar

/**
 * 大会ノート追加画面
 */
@Composable
fun AddTournamentNoteScreen(onDismiss: () -> Unit) {
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
                ) {
                    // 日付
                    DatePickerField()
                    Spacer(modifier = Modifier.height(4.dp))

                    // 天気
                    WeatherPickerField()
                    Spacer(modifier = Modifier.height(4.dp))

                    // 気温
                    TemperatureSlider()
                    Spacer(modifier = Modifier.height(4.dp))

                    // テキスト入力欄
                    MultiLineTextInputField("体調")
                    Spacer(modifier = Modifier.height(16.dp))

                    MultiLineTextInputField("目標")
                    Spacer(modifier = Modifier.height(16.dp))

                    MultiLineTextInputField("意識すること")
                    Spacer(modifier = Modifier.height(16.dp))

                    MultiLineTextInputField("結果")
                    Spacer(modifier = Modifier.height(16.dp))

                    MultiLineTextInputField("反省")
                }
            }
        }
    }
}

/**
 * 日付入力欄
 */
@Composable
fun DatePickerField() {
    // カレンダーのインスタンスを作成
    val calendar = Calendar.getInstance()

    // 初期状態として今日の日付を設定
    val initialDate = "${calendar.get(Calendar.YEAR)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.DAY_OF_MONTH)}"
    val selectedDate = remember { mutableStateOf(initialDate) }

    // ダイアログの表示状態
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "日付",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(end = 8.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        // 選択した日付の表示 or ボタン
        Button(
            onClick = {
                DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        // 日付が選択されたらフォーマットして表示
                        selectedDate.value = "$year/${month + 1}/$dayOfMonth"
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            },
            modifier = Modifier.weight(1f) // 右隣まで伸ばす
        ) {
            Text(text = selectedDate.value)
        }
    }
}

/**
 * 天気選択欄
 */
@Composable
fun WeatherPickerField() {
    val weatherOptions = listOf("晴れ", "くもり", "雨")
    var selectedWeather by remember { mutableStateOf(weatherOptions.first()) }
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "天気",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(end = 8.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // 天気選択ドロップダウン
        Button(
            onClick = { expanded = true },
            modifier = Modifier.weight(1f)
        ) {
            Text(selectedWeather)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            weatherOptions.forEach { option ->
                DropdownMenuItem(onClick = {
                    selectedWeather = option
                    expanded = false
                }) {
                    Text(option)
                }
            }
        }
    }
}

/**
 * 気温入力欄
 */
@Composable
fun TemperatureSlider() {
    var temperature by remember { mutableStateOf(0f) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "気温: ${temperature.toInt()}℃",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(end = 8.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        // 気温スライダー
        Slider(
            value = temperature,
            onValueChange = { temperature = it },
            valueRange = -40f..40f,
            modifier = Modifier.weight(1f) // 右隣まで伸ばす
        )
    }
}