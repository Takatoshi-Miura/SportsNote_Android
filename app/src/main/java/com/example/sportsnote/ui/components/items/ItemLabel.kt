package com.example.sportsnote.ui.components.items

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * 入力項目の共通タイトルラベル
 *
 * @param stringResourceId 文字列リソースID
 */
@Composable
fun ItemLabel(stringResourceId: Int) {
    Text(
        text = stringResource(stringResourceId),
        style = MaterialTheme.typography.body1,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(8.dp),
    )
}
