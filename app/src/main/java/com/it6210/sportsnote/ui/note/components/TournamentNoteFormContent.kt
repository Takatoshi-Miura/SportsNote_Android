package com.it6210.sportsnote.ui.note.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.it6210.sportsnote.R
import com.it6210.sportsnote.model.Note
import com.it6210.sportsnote.ui.components.CustomSpacerColumn
import com.it6210.sportsnote.ui.components.MultiLineTextInputField
import com.it6210.sportsnote.utils.Weather
import java.util.Date

/**
 * 大会ノートの共通フォーム
 *
 * @param note Noteオブジェクト（nullならデフォルト値を設定）
 * @param onDateChange 日付変更時の処理
 * @param onWeatherChange 天気変更時の処理
 * @param onTemperatureChange 温度変更時の処理
 * @param onConditionChange 体調変更時の処理
 * @param onTargetChange 目標変更時の処理
 * @param onConsciousnessChange 意識すること変更時の処理
 * @param onResultChange 結果変更時の処理
 * @param onReflectionChange 反省変更時の処理
 */
@Composable
fun TournamentNoteFormContent(
    note: Note? = null,
    onDateChange: (Date) -> Unit,
    onWeatherChange: (Int) -> Unit,
    onTemperatureChange: (Int) -> Unit,
    onConditionChange: (String) -> Unit,
    onTargetChange: (String) -> Unit,
    onConsciousnessChange: (String) -> Unit,
    onResultChange: (String) -> Unit,
    onReflectionChange: (String) -> Unit,
) {
    val date = remember { mutableStateOf(note?.date ?: Date()) }
    val weather = remember { mutableStateOf(note?.weather ?: Weather.SUNNY.id) }
    val temperature = remember { mutableStateOf(note?.temperature ?: 20) }
    val condition = remember { mutableStateOf(note?.condition ?: "") }
    val target = remember { mutableStateOf(note?.target ?: "") }
    val consciousness = remember { mutableStateOf(note?.consciousness ?: "") }
    val result = remember { mutableStateOf(note?.result ?: "") }
    val reflection = remember { mutableStateOf(note?.reflection ?: "") }

    val inputFields: List<@Composable () -> Unit> =
        listOf(
            // 日付
            {
                DatePickerField(
                    initialDate = date.value,
                    onDateSelected = { selectedDate -> date.value = selectedDate },
                )
            },
            // 天気
            {
                WeatherPickerField(
                    initialWeather = weather.value,
                    onWeatherSelected = { selectedWeather -> weather.value = selectedWeather },
                )
            },
            // 気温
            {
                TemperatureSlider(
                    initialTemperature = temperature.value,
                    onTemperatureSelected = { selectedTemperature -> temperature.value = selectedTemperature },
                )
            },
            // 体調
            {
                MultiLineTextInputField(
                    title = stringResource(R.string.condition),
                    onTextChanged = { input -> condition.value = input },
                    initialText = condition.value,
                )
            },
            // 目標
            {
                MultiLineTextInputField(
                    title = stringResource(R.string.target),
                    onTextChanged = { input -> target.value = input },
                    initialText = target.value,
                )
            },
            // 意識すること
            {
                MultiLineTextInputField(
                    title = stringResource(R.string.consciousness),
                    onTextChanged = { input -> consciousness.value = input },
                    initialText = consciousness.value,
                )
            },
            // 結果
            {
                MultiLineTextInputField(
                    title = stringResource(R.string.result),
                    onTextChanged = { input -> result.value = input },
                    initialText = result.value,
                )
            },
            // 反省
            {
                MultiLineTextInputField(
                    title = stringResource(R.string.reflection),
                    onTextChanged = { input -> reflection.value = input },
                    initialText = reflection.value,
                )
            },
        )

    // 入力フォーム
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
    ) {
        CustomSpacerColumn(items = inputFields)

        onDateChange(date.value)
        onWeatherChange(weather.value)
        onTemperatureChange(temperature.value)
        onConditionChange(condition.value)
        onTargetChange(target.value)
        onConsciousnessChange(consciousness.value)
        onResultChange(result.value)
        onReflectionChange(reflection.value)
    }
}
