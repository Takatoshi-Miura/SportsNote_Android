package com.example.sportsnote.ui.note

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sportsnote.R
import com.example.sportsnote.model.Note
import com.example.sportsnote.ui.LocalNavController
import com.example.sportsnote.ui.components.ActionBottomSheetContent
import com.example.sportsnote.ui.components.TextListItem
import com.example.sportsnote.utils.NoteType
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

/**
 * ノート一覧画面
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NoteScreen(noteViewModel: NoteViewModel = viewModel()) {
    val notes by noteViewModel.notes.collectAsState()
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    val BOTTOM_NAVIGATION_HEIGHT = 56.dp
    var isDialogVisible by remember { mutableStateOf(false) } // ダイアログの表示フラグ
    val navController = LocalNavController.current
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)

    // ノート一覧のリフレッシュ処理
    val onRefresh = {
        noteViewModel.loadNotes()
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            val actionItems = listOf(
                stringResource(R.string.addPracticeNoteAction) to {
                    // 練習ノート追加処理（仮）
                    coroutineScope.launch { sheetState.hide() }
                },
                stringResource(R.string.addTournamentNoteAction) to {
                    isDialogVisible = true
                    coroutineScope.launch { sheetState.hide() }
                },
                stringResource(R.string.cancel) to {
                    coroutineScope.launch { sheetState.hide() }
                }
            )
            ActionBottomSheetContent(items = actionItems)
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = onRefresh
            ) {
                // ノート一覧
                NoteListScreen(
                    notes = notes,
                    onNoteClick = { note ->
                        when(NoteType.fromInt(note.noteType)) {
                            NoteType.FREE -> { }
                            NoteType.PRACTICE -> { }
                            NoteType.TOURNAMENT -> {
                                navController.navigate("tournament_note_view/${note.noteID}")
                            }
                        }
                    }
                )
            }

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

    // ダイアログでフルスクリーンモーダルを表示
    if (isDialogVisible) {
        AddTournamentNoteScreen(
            onDismiss = { isDialogVisible = false }
        )
    }
}

@Composable
fun NoteListScreen(notes: List<Note>, onNoteClick: (Note) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        items(notes) { note ->
            val displayText = when (NoteType.fromInt(note.noteType)) {
                NoteType.FREE -> note.title
                NoteType.PRACTICE -> note.detail
                NoteType.TOURNAMENT -> note.result
            }
            TextListItem(
                title = displayText,
                onClick = { onNoteClick(note) }
            )
            Divider()
        }
    }
}
