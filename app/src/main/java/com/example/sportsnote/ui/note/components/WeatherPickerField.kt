package com.example.sportsnote.ui.note.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.sportsnote.R
import com.example.sportsnote.ui.components.items.ItemLabel
import com.example.sportsnote.utils.Weather

/**
 * 天気選択欄
 *
 * @param initialWeather 初期値
 * @param onWeatherSelected 天気選択時の処理
 */
@Composable
fun WeatherPickerField(
    initialWeather: Int = Weather.SUNNY.id,
    onWeatherSelected: (Int) -> Unit,
) {
    val weatherOptions = Weather.entries
    var selectedWeather by remember { mutableStateOf(initialWeather) }
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // タイトルラベル
        ItemLabel(title = stringResource(R.string.weather))
        Spacer(modifier = Modifier.width(16.dp))

        // 天気選択ドロップダウン
        Button(
            onClick = { expanded = true },
            modifier = Modifier.weight(1f),
        ) {
            Text(Weather.fromInt(selectedWeather).getTitle())
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            weatherOptions.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        selectedWeather = option.id
                        onWeatherSelected(option.id)
                        expanded = false
                    },
                ) {
                    Text(option.getTitle())
                }
            }
        }
    }
}
