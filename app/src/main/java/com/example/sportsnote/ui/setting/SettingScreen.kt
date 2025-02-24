package com.example.sportsnote.ui.setting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.sportsnote.R
import com.example.sportsnote.ui.components.DialogType
import com.example.sportsnote.ui.components.ItemData
import com.example.sportsnote.ui.components.SectionData
import com.example.sportsnote.ui.components.SectionedColumn
import com.example.sportsnote.utils.AppInfo
import launchMailer

/**
 * 設定画面を作成
 *
 * @param onDismiss 画面を閉じる処理
 */
@Composable
fun SettingScreen(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val appVersion = AppInfo.getAppVersion(context)
    val androidVersion = AppInfo.getAndroidVersionInfo()
    val deviceName = AppInfo.getDeviceName()
    var isDialogVisible by remember { mutableStateOf(false) }
    var dialogType by remember { mutableStateOf(DialogType.None) }

    val sections =
        listOf(
            // データ
            SectionData(
                title = stringResource(R.string.data),
                items =
                    listOf(
                        // データの引継ぎ
                        ItemData(
                            title = stringResource(R.string.data_transfer),
                            iconRes = R.drawable.outline_cloud_upload_24,
                        ) {
                            // ログイン画面を表示
                            isDialogVisible = true
                            dialogType = DialogType.Login
                        },
                    ),
            ),
            // ヘルプ
            SectionData(
                title = stringResource(R.string.help),
                items =
                    listOf(
                        // アプリの使い方
                        ItemData(
                            title = stringResource(R.string.how_to_use_this_app),
                            iconRes = R.drawable.baseline_question_mark_24,
                        ) {
                            // チュートリアル画面を表示
                            isDialogVisible = true
                            dialogType = DialogType.Tutorial
                        },
                        // お問い合わせ
                        ItemData(
                            title = stringResource(R.string.inquiry),
                            iconRes = R.drawable.baseline_mail_outline_24,
                        ) {
                            // メーラーを表示
                            val email = "SportsNote開発者<it6210ge@gmail.com>"
                            val subject = "お問い合わせ"
                            val body =
                                """
                                お問い合わせ内容をご記入下さい。
                                
                                
                                以下は削除しないでください。
                                ■ご利用端末:
                                 $deviceName
                                ■OSバージョン:
                                 $androidVersion
                                ■アプリバージョン:
                                 $appVersion
                                """.trimIndent()
                            launchMailer(context, email, subject, body)
                        },
                    ),
            ),
            // システム情報
            SectionData(
                title = stringResource(R.string.system_info),
                items =
                    listOf(
                        // アプリバージョン
                        ItemData(
                            title = stringResource(R.string.app_version),
                            subTitle = appVersion,
                            iconRes = R.drawable.baseline_info_outline_24,
                        ) { },
                    ),
            ),
        )
    SectionedColumn(sections = sections)

    // ダイアログでフルスクリーンモーダルを表示
    if (!isDialogVisible) return
    if (dialogType == DialogType.Login) {
        LoginScreen(
            onDismiss = {
                isDialogVisible = false
                onDismiss()
            },
        )
    } else if (dialogType == DialogType.Tutorial) {
        TutorialScreen(
            onDismiss = { isDialogVisible = false },
        )
    }
}
