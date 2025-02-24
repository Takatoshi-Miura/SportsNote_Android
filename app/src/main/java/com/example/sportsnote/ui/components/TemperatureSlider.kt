package com.example.sportsnote.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp

/**
 * 気温入力欄
 *
 * @param initialTemperature 初期値
 * @param onTemperatureSelected 気温選択時の処理
 */
@Composable
fun TemperatureSlider(
    initialTemperature: Int = 20,
    onTemperatureSelected: (Int) -> Unit,
) {
    var temperature by remember { mutableStateOf(initialTemperature.toFloat()) }

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "気温: ${temperature.toInt()}℃",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(end = 8.dp),
        )
        Spacer(modifier = Modifier.width(16.dp))
        // 気温スライダー
        Slider(
            value = temperature,
            onValueChange = {
                temperature = it
                onTemperatureSelected(temperature.toInt())
            },
            valueRange = -40f..40f,
            modifier = Modifier.weight(1f),
        )
    }
}
