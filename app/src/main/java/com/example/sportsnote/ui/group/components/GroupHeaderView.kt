package com.example.sportsnote.ui.group.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sportsnote.utils.Color

/**
 * 課題一覧用のGroupヘッダーUI
 */
@Composable
fun GroupHeaderView(
    title: String,
    colorId: Int,
    onInfoButtonClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(MaterialTheme.colors.surface),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        // 背景色を指定した部品
        Box(
            modifier =
                Modifier
                    .fillMaxHeight()
                    .size(25.dp)
                    .background(
                        Color.fromInt(colorId).toComposeColor(),
                    ),
        )

        // タイトル
        Text(
            text = title,
            style = MaterialTheme.typography.body1,
            modifier =
                Modifier
                    .padding(start = 8.dp)
                    .weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        // Info button
        IconButton(
            onClick = onInfoButtonClick,
            modifier =
                Modifier
                    .padding(end = 4.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Info",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGroupHeaderView() {
    GroupHeaderView(
        title = "グループ名グループ名グループ名グループ名グループ名グループ名グループ名",
        colorId = Color.RED.id,
        onInfoButtonClick = {},
    )
}
