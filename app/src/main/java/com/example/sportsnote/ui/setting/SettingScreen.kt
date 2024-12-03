package com.example.sportsnote.ui.setting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.example.sportsnote.R
import com.example.sportsnote.ui.components.ItemData
import com.example.sportsnote.ui.components.SectionData
import com.example.sportsnote.ui.components.SectionedColumn

/**
 * 設定画面を作成
 */
@Composable
fun SettingScreen() {
    val sections = listOf(
        SectionData(
            title = "データ",
            items = listOf(
                ItemData("データの引継ぎ", R.drawable.ic_home_black_24dp) { println("データの引継ぎ") }
            )
        ),
        SectionData(
            title = "ヘルプ",
            items = listOf(
                ItemData("アプリの使い方", R.drawable.ic_home_black_24dp) { println("アプリの使い方") },
                ItemData("お問い合わせ", R.drawable.ic_home_black_24dp) { println("お問い合わせ") }
            )
        ),
        SectionData(
            title = "システム情報",
            items = listOf(
                ItemData("アプリバージョン", R.drawable.ic_home_black_24dp) { println("アプリバージョン") }
            )
        )
    )
    SectionedColumn(sections = sections)
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingScreen() {
    SettingScreen()
}

