package com.example.sportsnote.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.sportsnote.R
import com.example.sportsnote.utils.Color

/**
 * カラー選択欄
 *
 * @param onColorSelected カラー選択時
 * @param initialColor 初期値
 */
@Composable
fun ColorPickerField(
    onColorSelected: (Int) -> Unit,
    initialColor: Int = Color.RED.id
) {
    val context = LocalContext.current

    DropdownPickerField(
        fieldName = stringResource(R.string.color),
        items = Color.entries,
        initialSelected = Color.entries.first { it.id == initialColor },
        onItemSelected = { onColorSelected(it.id) },
        displayText = { it.getTitle(context = context) },
        buttonColor = { it.toComposeColor() }
    )
}