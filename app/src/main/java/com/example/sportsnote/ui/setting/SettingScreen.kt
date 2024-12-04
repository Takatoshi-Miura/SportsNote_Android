package com.example.sportsnote.ui.setting

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
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
            title = stringResource(R.string.data),
            items = listOf(
                ItemData(stringResource(R.string.data_transfer), R.drawable.ic_home_black_24dp) { println("データの引継ぎ") }
            )
        ),
        SectionData(
            title = stringResource(R.string.help),
            items = listOf(
                ItemData(stringResource(R.string.how_to_use_this_app), R.drawable.ic_home_black_24dp) { println("アプリの使い方") },
                ItemData(stringResource(R.string.inquiry), R.drawable.ic_home_black_24dp) { println("お問い合わせ") }
            )
        ),
        SectionData(
            title = stringResource(R.string.system_info),
            items = listOf(
                ItemData(stringResource(R.string.app_version), R.drawable.ic_home_black_24dp) { println("アプリバージョン") }
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