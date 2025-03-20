package com.it6210.sportsnote.ui.setting.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.it6210.sportsnote.viewModel.TutorialPage

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
