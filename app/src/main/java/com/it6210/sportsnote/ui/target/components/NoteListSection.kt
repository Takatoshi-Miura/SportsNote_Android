package com.it6210.sportsnote.ui.target.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.it6210.sportsnote.model.NoteListItem
import com.it6210.sportsnote.ui.components.AdMobBanner
import com.it6210.sportsnote.ui.note.components.NoteEmptyItem
import com.it6210.sportsnote.ui.note.components.NoteItem

/**
 * ノートリストを縦にスクロール表示するコンポーネント
 *
 * @param notes ノートリスト
 * @param onNoteClick ノート押下時の処理
 */
@Composable
fun NoteListSection(
    notes: List<NoteListItem>,
    onNoteClick: (NoteListItem) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.background)
                    .padding(8.dp),
        ) {
            if (notes.isEmpty()) {
                item {
                    NoteEmptyItem()
                }
            }
            items(notes) { note ->
                NoteItem(
                    note = note,
                    onClick = { onNoteClick(note) },
                )
                Divider()
            }
        }
        // バナー広告
        AdMobBanner()
    }
}
