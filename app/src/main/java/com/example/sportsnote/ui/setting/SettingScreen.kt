package com.example.sportsnote.ui.setting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.example.sportsnote.ui.components.SectionData
import com.example.sportsnote.ui.components.SectionedColumn

/**
 * 設定画面を作成
 */
@Composable
fun SettingScreen() {
    val sections = listOf(
        SectionData("データ", listOf("データの引継ぎ")),
        SectionData("ヘルプ", listOf("アプリの使い方", "お問い合わせ")),
        SectionData("システム情報", listOf("アプリバージョン"))
    )
    val selectedItem = remember { mutableStateOf("") }

    SectionedColumn(
        sections = sections,
        onItemClick = { item ->
            selectedItem.value = item
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingScreen() {
    SettingScreen()
}

