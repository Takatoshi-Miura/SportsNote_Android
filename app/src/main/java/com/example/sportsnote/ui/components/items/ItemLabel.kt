package com.example.sportsnote.ui.components.items

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * 入力項目の共通タイトルラベル
 *
 * @param title タイトル文字列
 * @param modifier Modifier
 */
@Composable
fun ItemLabel(
    title: String,
    modifier: Modifier = Modifier.padding(8.dp),
) {
    Text(
        text = title,
        style = MaterialTheme.typography.body1,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier,
    )
}
