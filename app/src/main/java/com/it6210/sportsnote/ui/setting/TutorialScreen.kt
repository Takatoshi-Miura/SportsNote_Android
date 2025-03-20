package com.it6210.sportsnote.ui.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.it6210.sportsnote.R
import com.it6210.sportsnote.ui.setting.components.PagerIndicator
import com.it6210.sportsnote.ui.setting.components.TutorialPageContent
import com.it6210.sportsnote.viewModel.TutorialViewModel

/**
 * アプリの使い方画面
 *
 * @param onDismiss 閉じる時の処理
 */
@Composable
fun TutorialScreen(onDismiss: () -> Unit) {
    val viewModel = TutorialViewModel()
    val pages = viewModel.pages

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

@Preview
@Composable
fun PreviewTutorialScreen() {
    TutorialScreen { }
}
