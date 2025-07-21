package com.it6210.sportsnote.ui.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.it6210.sportsnote.R
import com.it6210.sportsnote.ui.setting.components.PagerIndicator
import com.it6210.sportsnote.ui.setting.components.TutorialPageContent
import com.it6210.sportsnote.viewModel.TutorialViewModel
import androidx.compose.material3.MaterialTheme as Material3Theme

/**
 * アプリの使い方画面（Material3 Scaffold使用）
 *
 * @param onDismiss 閉じる時の処理
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialScreen(onDismiss: () -> Unit) {
    val viewModel = TutorialViewModel()
    val pages = viewModel.pages

    Dialog(
        onDismissRequest = onDismiss,
        properties =
            DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = true,
            ),
    ) {
        val pagerState = rememberPagerState { pages.size }

        // Material2からMaterial3への色変換
        val material3ColorScheme =
            lightColorScheme(
                primary = Color(MaterialTheme.colors.primary.value),
                surface = Color(MaterialTheme.colors.surface.value),
                background = Color(MaterialTheme.colors.primary.value),
                onPrimary = Color(MaterialTheme.colors.onPrimary.value),
                onSurface = Color(MaterialTheme.colors.onSurface.value),
            )

        Material3Theme(colorScheme = material3ColorScheme) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = material3ColorScheme.primary,
                contentWindowInsets = WindowInsets.systemBars,
                topBar = {
                    TopAppBar(
                        title = { },
                        actions = {
                            Button(
                                onClick = onDismiss,
                                modifier = Modifier.padding(end = 8.dp),
                            ) {
                                Text(
                                    text = stringResource(R.string.close),
                                    color = Color.White,
                                )
                            }
                        },
                        colors =
                            TopAppBarDefaults.topAppBarColors(
                                containerColor = material3ColorScheme.primary,
                            ),
                    )
                },
            ) { paddingValues ->
                // メインコンテンツエリア（Box内で手動レイアウト）
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(bottom = 80.dp),
                ) {
                    // HorizontalPager
                    HorizontalPager(
                        beyondViewportPageCount = pages.size,
                        state = pagerState,
                        modifier = Modifier.fillMaxSize(),
                    ) { page ->
                        TutorialPageContent(page = pages[page])
                    }

                    // ページインジケータ（手動配置）
                    Box(
                        modifier =
                            Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        PagerIndicator(
                            pageCount = pages.size,
                            currentPage = pagerState.currentPage,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewTutorialScreen() {
    TutorialScreen { }
}
