package com.example.sportsnote.ui.note

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sportsnote.ui.components.LazyNonSectionedColumn
import kotlinx.coroutines.launch

/**
 * ノート一覧画面
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NoteScreen(noteViewModel: NoteViewModel = viewModel()) {
    val items by noteViewModel.items.collectAsState()
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    val BOTTOM_NAVIGATION_HEIGHT = 56.dp

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            BottomSheetContent(
                onPracticeNoteSelected = {
//                    noteViewModel.addPracticeNote()
                    coroutineScope.launch { sheetState.hide() }
                },
                onTournamentNoteSelected = {
//                    noteViewModel.addTournamentNote()
                    coroutineScope.launch { sheetState.hide() }
                },
                onCancel = {
                    coroutineScope.launch { sheetState.hide() }
                }
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // ノート一覧
            LazyNonSectionedColumn(items = items)

            // +ボタン
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch { sheetState.show() }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        end = 16.dp,
                        bottom = 16.dp + BOTTOM_NAVIGATION_HEIGHT
                    )
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Note")
            }
        }
    }
}

@Composable
fun BottomSheetContent(
    onPracticeNoteSelected: () -> Unit,
    onTournamentNoteSelected: () -> Unit,
    onCancel: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(
            text = "練習ノートを追加",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onPracticeNoteSelected() }
                .padding(vertical = 16.dp)
        )
        Divider()
        Text(
            text = "大会ノートを追加",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onTournamentNoteSelected() }
                .padding(vertical = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Text(
            text = "キャンセル",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onCancel() }
                .padding(vertical = 16.dp),
        )
    }
}

