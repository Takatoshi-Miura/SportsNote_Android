package com.it6210.sportsnote.ui.setting

import androidx.lifecycle.ViewModel
import com.it6210.sportsnote.R

data class TutorialPage(
    val title: String,
    val description: String,
    val imageRes: Int,
)

/**
 * アプリの使い方ページ用ViewModel
 */
class TutorialViewModel : ViewModel() {
    // 使い方ページデータ
    val pages =
        listOf(
            TutorialPage(
                title = "SportsNoteとは",
                description =
                    """
                    課題解決に特化したノートアプリです。
                    原因と対策を考えて実践し、反省を通して
                    解決を目指すことができます。
                    """.trimIndent(),
                imageRes = R.drawable.screenshot_1,
            ),
            TutorialPage(
                title = "課題の管理①",
                description =
                    """
                    課題を一覧で管理できます。
                    グループを作成することで課題を分類して
                    管理することができます。
                    """.trimIndent(),
                imageRes = R.drawable.screenshot_2,
            ),
            TutorialPage(
                title = "課題の管理②",
                description =
                    """
                    課題毎に原因と対策を登録できます。
                    優先度が最も高い対策が
                    ノートに読み込まれるようになります。
                    """.trimIndent(),
                imageRes = R.drawable.screenshot_3,
            ),
            TutorialPage(
                title = "ノートを作成",
                description =
                    """
                    練習ノートを作成できます。
                    ノートには登録した課題が読み込まれ、
                    課題への取り組みを記録しておくことができます。
                    """.trimIndent(),
                imageRes = R.drawable.screenshot_4,
            ),
            TutorialPage(
                title = "振り返り",
                description =
                    """
                    記録した内容はノートで振り返ることができます。
                    課題＞対策へと進めば、その課題への取り組み内容を
                    まとめて振り返ることもできます。
                    """.trimIndent(),
                imageRes = R.drawable.screenshot_5,
            ),
            TutorialPage(
                title = "課題を完了にする",
                description =
                    """
                    解決した課題は完了にすることで
                    ノートへ読み込まれなくなります。完了にしても
                    完了した課題からいつでも振り返ることができます。
                    """.trimIndent(),
                imageRes = R.drawable.screenshot_6,
            ),
        )
}
