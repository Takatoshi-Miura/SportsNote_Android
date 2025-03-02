package com.example.sportsnote.ui.note.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Slider
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
        // タイトルラベル
        ItemLabel(title = stringResource(R.string.temperature, temperature.toInt()))
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
