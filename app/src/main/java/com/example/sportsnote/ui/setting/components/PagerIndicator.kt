package com.example.sportsnote.ui.setting.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
