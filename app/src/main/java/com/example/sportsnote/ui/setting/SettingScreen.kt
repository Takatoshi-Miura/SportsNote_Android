package com.example.sportsnote.ui.setting

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.sportsnote.R
import com.example.sportsnote.ui.components.ItemData
import com.example.sportsnote.ui.components.SectionData
import com.example.sportsnote.ui.components.SectionedColumn
import com.example.sportsnote.utils.AppInfo

/**
 * 設定画面を作成
 */
@Composable
fun SettingScreen() {
    val context = LocalContext.current
    val appVersion = AppInfo.getAppVersion(context)
    val buildNo = AppInfo.getBuildNo(context)

    val sections = listOf(
        // データ
        SectionData(
            title = stringResource(R.string.data),
            items = listOf(
                // データの引継ぎ
                ItemData(
                    title = stringResource(R.string.data_transfer),
                    iconRes = R.drawable.outline_cloud_upload_24
                ) {
                    println("データの引継ぎ")
                    // ログイン画面を表示
                }
            )
        ),
        // ヘルプ
        SectionData(
            title = stringResource(R.string.help),
            items = listOf(
                // アプリの使い方
                ItemData(
                    title = stringResource(R.string.how_to_use_this_app),
                    iconRes = R.drawable.baseline_question_mark_24
                ) {
                    // チュートリアル画面を表示
                },
                // お問い合わせ
                ItemData(
                    title = stringResource(R.string.inquiry),
                    iconRes = R.drawable.baseline_mail_outline_24
                ) {
                    // メーラーを表示
                }
            )
        ),
        // システム情報
        SectionData(
            title = stringResource(R.string.system_info),
            items = listOf(
                // アプリバージョン
                ItemData(
                    title = stringResource(R.string.app_version),
                    subTitle = "$appVersion（$buildNo）",
                    iconRes = R.drawable.baseline_info_outline_24
                ) { }
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