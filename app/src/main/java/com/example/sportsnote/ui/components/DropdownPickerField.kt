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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color as AndroidColor

/**
 * 汎用的な選択フィールド
 *
 * @param fieldName 項目名
 * @param items 選択可能なアイテムのリスト
 * @param initialSelected 初期選択アイテム
 * @param onItemSelected アイテム選択時のコールバック
 * @param displayText アイテムのテキスト表示方法
 * @param buttonColor アイテムの背景色を取得する関数
 */
@Composable
fun <T> DropdownPickerField(
    fieldName: String,
    items: List<T>,
    initialSelected: T,
    onItemSelected: (T) -> Unit,
    displayText: (T) -> String,
    buttonColor: @Composable (T) -> AndroidColor = { MaterialTheme.colors.primary },
) {
    var selectedItem by remember { mutableStateOf(initialSelected) }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
    ) {
        // 項目名
        Text(
            text = fieldName,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(end = 8.dp),
        )

        // 選択ボタン
        Button(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            colors =
                ButtonDefaults.buttonColors(
                    backgroundColor = buttonColor(selectedItem),
                    contentColor = AndroidColor.White,
                ),
        ) {
            Text(displayText(selectedItem))
        }

        // 選択項目
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        selectedItem = item
                        onItemSelected(item)
                        expanded = false
                    },
                ) {
                    Text(displayText(item))
                }
            }
        }
    }
}
