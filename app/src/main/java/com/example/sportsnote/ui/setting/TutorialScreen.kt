package com.example.sportsnote.ui.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.sportsnote.R

data class TutorialPage(
    val title: String,
    val description: String,
    val imageRes: Int,
)

/**
 * アプリの使い方画面
 *
 * @param onDismiss 閉じる時の処理
 */
@Composable
fun TutorialScreen(onDismiss: () -> Unit) {
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

    Dialog(
        onDismissRequest = onDismiss,
        properties =
            DialogProperties(
                usePlatformDefaultWidth = false,
            ),
    ) {
        val pagerState = rememberPagerState { pages.size }

        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.primary),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // ページ表示
                HorizontalPager(
                    beyondViewportPageCount = pages.size,
                    state = pagerState,
                    modifier = Modifier.weight(1f),
                ) { page ->
                    TutorialPageContent(page = pages[page])
                }

                // ページインジケータ
                PagerIndicator(
                    pageCount = pages.size,
                    currentPage = pagerState.currentPage,
                    modifier = Modifier.padding(16.dp),
                )
            }

            // 閉じるボタン
            Button(
                onClick = onDismiss,
                modifier =
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
            ) {
                Text(stringResource(R.string.close))
            }
        }
    }
}

/**
 * ページインジケーター
 *
 * @param pageCount ページ数
 * @param currentPage 現在のページ
 * @param modifier Modifier
 */
@Composable
fun PagerIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(pageCount) { index ->
            val color = if (index == currentPage) Color.White else Color.Gray
            Box(
                modifier =
                    Modifier
                        .size(10.dp)
                        .background(color, shape = androidx.compose.foundation.shape.CircleShape)
                        .padding(4.dp),
            )
        }
    }
}

/**
 * チュートリアルページのコンポーネント
 *
 * @param page TutorialPage
 */
@Composable
fun TutorialPageContent(page: TutorialPage) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        // タイトル
        Text(
            text = page.title,
            color = Color.White,
            style = MaterialTheme.typography.h5.copy(fontSize = 25.sp),
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .padding(top = 60.dp)
                    .padding(bottom = 8.dp),
        )

        // 説明文
        Text(
            text = page.description,
            color = Color.White,
            style = MaterialTheme.typography.body1.copy(fontSize = 14.sp),
            textAlign = TextAlign.Center,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        // 画像
        Image(
            painter = painterResource(id = page.imageRes),
            contentDescription = page.title,
            modifier =
                Modifier
                    .padding(bottom = 50.dp),
        )
    }
}

@Preview
@Composable
fun PreviewTutorialScreen() {
    TutorialScreen { }
}
