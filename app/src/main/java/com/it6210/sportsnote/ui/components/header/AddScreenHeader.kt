package com.it6210.sportsnote.ui.components.header

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.it6210.sportsnote.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * ヘッダーコンポーネント
 *
 * @param title ヘッダーのタイトル
 * @param onCancel キャンセル時の処理
 * @param onSave 保存時の処理
 * @param coroutineScope CoroutineScope（非同期処理に使用）
 */
@Composable
fun AddScreenHeader(
    title: String,
    onCancel: () -> Unit,
    onSave: suspend () -> Unit,
    coroutineScope: CoroutineScope,
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(MaterialTheme.colors.primary),
    ) {
        // キャンセルボタン
        Button(
            onClick = onCancel,
            modifier =
                Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp),
        ) {
            Text(stringResource(R.string.cancel))
        }

        // タイトル
        Text(
            text = title,
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.align(Alignment.Center),
        )

        // 保存ボタン
        Button(
            onClick = {
                coroutineScope.launch {
                    onSave()
                }
            },
            modifier =
                Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp),
        ) {
            Text(stringResource(R.string.save))
        }
    }
}
