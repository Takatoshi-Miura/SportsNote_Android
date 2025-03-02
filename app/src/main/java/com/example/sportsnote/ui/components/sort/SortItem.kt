package com.example.sportsnote.ui.components.sort

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * 並び替え可能なアイテム
 *
 * @param title タイトル文字列
 * @param modifier Modifier
 */
@Composable
fun SortItem(
    title: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .background(MaterialTheme.colors.surface)
                .height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // タイトル表示
        Text(
            text = title,
            style = MaterialTheme.typography.body1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
        // 並び替えアイコン
        SortIcon()
    }
}

/**
 * 並び替えアイコン表示
 */
@Composable
fun SortIcon() {
    Icon(
        imageVector = Icons.Default.Menu,
        contentDescription = "Drag Handle",
        modifier = Modifier.padding(end = 8.dp),
    )
}
