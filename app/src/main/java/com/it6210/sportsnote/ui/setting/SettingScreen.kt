package com.it6210.sportsnote.ui.setting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.it6210.sportsnote.R
import com.it6210.sportsnote.model.manager.TermsManager
import com.it6210.sportsnote.ui.components.DialogType
import com.it6210.sportsnote.ui.components.items.ItemData
import com.it6210.sportsnote.ui.components.items.SectionData
import com.it6210.sportsnote.ui.components.items.SectionedColumn
import com.it6210.sportsnote.utils.AppInfo
import openInquiryMailer

/**
 * 設定画面を作成
 *
 * @param onDismiss 画面を閉じる処理
 */
@Composable
fun SettingScreen(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val appVersion = AppInfo.getAppVersion(context)
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
                            openInquiryMailer(context)
                        },
                    ),
            ),
            // その他
            SectionData(
                title = stringResource(R.string.other),
                items =
                    listOf(
                        // 利用規約
                        ItemData(
                            title = stringResource(R.string.termsOfServiceTitle),
                            iconRes = R.drawable.ic_description_24dp,
                        ) {
                            TermsManager.navigateToTermsOfService(context)
                        },
                        // プライバシーポリシー
                        ItemData(
                            title = stringResource(R.string.privacyPolicy),
                            iconRes = R.drawable.ic_lock_24dp,
                        ) {
                            TermsManager.navigateToPrivacyPolicy(context)
                        },
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
    }
    if (dialogType == DialogType.Tutorial) {
        TutorialScreen(
            onDismiss = { isDialogVisible = false },
        )
    }
}
