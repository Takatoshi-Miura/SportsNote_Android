package com.example.sportsnote.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job

/**
 * アクションシート
 *
 * @param items リストの項目名とクリック動作のペア
 */
@Composable
fun ActionBottomSheetContent(items: List<Pair<String, () -> Job>>) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
    ) {
        items.forEachIndexed { index, item ->
            Text(
                text = item.first,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clickable { item.second() }
                        .padding(vertical = 16.dp),
            )
            if (index != items.lastIndex) {
                Divider() // 最後の項目以外は区切り線を表示
            }
        }
    }
}
