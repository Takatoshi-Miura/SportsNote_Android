package com.example.sportsnote.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sportsnote.R
import com.example.sportsnote.utils.Color
import androidx.compose.ui.graphics.Color as AndroidColor

/**
 * カラー選択欄
 *
 * @param onColorSelected カラー選択時
 */
@Composable
fun ColorPickerField(onColorSelected: (Int) -> Unit) {
    val colorOptions = Color.entries
    var selectedColor by remember { mutableStateOf(colorOptions.first()) }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = stringResource(R.string.color),
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(end = 8.dp)
        )
        Button(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = selectedColor.toComposeColor(),
                contentColor = AndroidColor.White
            )
        ) {
            Text(selectedColor.getTitle())
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            colorOptions.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        selectedColor = option
                        onColorSelected(option.id)
                        expanded = false
                    }
                ) {
                    Text(option.getTitle())
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewColorPickerField() {
    ColorPickerField { }
}