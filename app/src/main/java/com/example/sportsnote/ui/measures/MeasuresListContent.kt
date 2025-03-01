package com.example.sportsnote.ui.measures

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.sportsnote.model.Measures
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

/**
 * 並び替え可能な対策リストUI
 *
 * @param measuresList 対策リスト
 * @param onOrderChanged 並び替え時の処理
 * @param onItemClick アイテムクリック時の処理
 */
@Composable
fun MeasuresListContent(
    measuresList: List<Measures>,
    onOrderChanged: (List<Measures>) -> Unit,
    onItemClick: (String) -> Unit,
) {
    // リストの状態を保持
    var list by remember { mutableStateOf(measuresList) }

    // 並び替えロジック
    val state =
        rememberReorderableLazyListState(onMove = { from, to ->
            list =
                list.toMutableList().apply {
                    add(to.index, removeAt(from.index))
                }
            onOrderChanged(list)
        })

    LazyColumn(
        state = state.listState,
        modifier =
            Modifier
                .fillMaxSize()
                .reorderable(state)
                .detectReorderAfterLongPress(state),
    ) {
        itemsIndexed(list, key = { _, item -> item.measuresID }) { _, measure ->
            ReorderableItem(state, key = measure.measuresID) { isDragging ->
                Divider()
                MeasureItem(
                    measure = measure,
                    modifier =
                        Modifier
                            .background(if (isDragging) MaterialTheme.colors.primary.copy(alpha = 0.1f) else MaterialTheme.colors.surface)
                            .fillMaxWidth()
                            .clickable {
                                onItemClick(measure.measuresID)
                            }
                            .padding(8.dp),
                )
            }
            if (list.size == 1) {
                Divider()
            }
        }
    }
}

@Composable
fun MeasureItem(
    measure: Measures,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // タイトル
        Text(
            text = measure.title,
            style = MaterialTheme.typography.body1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
        // 並び替えアイコン
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Drag Handle",
            modifier = Modifier.padding(end = 8.dp),
        )
    }
}
