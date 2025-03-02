package com.example.sportsnote.ui.note.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.sportsnote.R

/**
 * 検索バーのコンポーネント
 *
 * @param query 検索文字列
 * @param onQueryChanged 文字列変化時の処理
 */
@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(MaterialTheme.colors.background, shape = MaterialTheme.shapes.medium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // 虫眼鏡アイコン
        Icon(
            painter = painterResource(id = R.drawable.search_24px),
            contentDescription = null,
            modifier =
                Modifier
                    .padding(start = 4.dp)
                    .size(24.dp),
            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
        )

        // テキスト入力フィールド
        TextField(
            value = query,
            onValueChange = onQueryChanged,
            modifier = Modifier.weight(1f),
            placeholder = {
                Text(text = stringResource(R.string.searchNote))
            },
            colors =
                TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
            singleLine = true,
        )
    }
}
