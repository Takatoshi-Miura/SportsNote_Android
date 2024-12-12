package com.example.sportsnote.ui.note

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sportsnote.ui.components.LazyNonSectionedColumn

/**
 * ノート一覧画面
 */
@Composable
fun NoteScreen(noteViewModel: NoteViewModel = viewModel()) {
    val items by noteViewModel.items.collectAsState()
    LazyNonSectionedColumn(items = items)
}
