package com.it6210.sportsnote.ui.note

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.it6210.sportsnote.R
import com.it6210.sportsnote.model.manager.PreferencesManager
import com.it6210.sportsnote.model.manager.SyncManager
import com.it6210.sportsnote.ui.LocalNavController
import com.it6210.sportsnote.ui.components.ActionBottomSheetContent
import com.it6210.sportsnote.ui.components.DialogType
import com.it6210.sportsnote.ui.components.items.CustomFloatingActionButton
import com.it6210.sportsnote.ui.note.components.NoteListContent
import com.it6210.sportsnote.ui.note.components.SearchBar
import com.it6210.sportsnote.utils.NoteType
import com.it6210.sportsnote.viewModel.NoteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ノート一覧画面
 *
 * @param reloadTrigger リロードトリガー
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NoteScreen(reloadTrigger: Int) {
    val noteViewModel = NoteViewModel()
    val notes by noteViewModel.noteListItems.collectAsState()
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    val navController = LocalNavController.current
    val isLoading by noteViewModel.isLoading.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)
    var isDialogVisible by remember { mutableStateOf(false) }
    var dialogType by remember { mutableStateOf(DialogType.None) }
    var searchQuery by remember { mutableStateOf("") }

    // 一覧のリフレッシュ処理
    val onRefresh = {
        coroutineScope.launch {
            if (PreferencesManager.get(PreferencesManager.Keys.IS_LOGIN, false)) {
                withContext(Dispatchers.IO) {
                    SyncManager.syncAllData()
                }
            }
            noteViewModel.searchNotesByQuery(searchQuery)
        }
    }

    LaunchedEffect(Unit, reloadTrigger) {
        onRefresh()
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            val actionItems =
                listOf(
                    stringResource(R.string.addPracticeNoteAction) to {
                        isDialogVisible = true
                        dialogType = DialogType.AddPracticeNote
                        coroutineScope.launch { sheetState.hide() }
                    },
                    stringResource(R.string.addTournamentNoteAction) to {
                        isDialogVisible = true
                        dialogType = DialogType.AddTournamentNote
                        coroutineScope.launch { sheetState.hide() }
                    },
                    stringResource(R.string.cancel) to {
                        coroutineScope.launch { sheetState.hide() }
                    },
                )
            ActionBottomSheetContent(items = actionItems)
        },
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.surface),
        ) {
            Column {
                // 検索バー
                SearchBar(
                    query = searchQuery,
                    onQueryChanged = {
                        searchQuery = it
                        onRefresh()
                    },
                )
                SwipeRefresh(
                    state = swipeRefreshState,
                    onRefresh = { onRefresh() },
                ) {
                    // ノート一覧
                    NoteListContent(
                        notes = notes,
                        onNoteClick = { note ->
                            when (NoteType.fromInt(note.noteType)) {
                                NoteType.FREE -> {
                                    navController.navigate("free_note_view/${note.noteID}")
                                }
                                NoteType.PRACTICE -> {
                                    navController.navigate("practice_note_view/${note.noteID}")
                                }
                                NoteType.TOURNAMENT -> {
                                    navController.navigate("tournament_note_view/${note.noteID}")
                                }
                            }
                        },
                    )
                }
            }

            // +ボタン
            CustomFloatingActionButton {
                coroutineScope.launch { sheetState.show() }
            }
        }
    }

    // ダイアログでフルスクリーンモーダルを表示
    if (!isDialogVisible) return
    if (dialogType == DialogType.AddPracticeNote) {
        AddPracticeNoteScreen(
            onDismiss = {
                isDialogVisible = false
                onRefresh()
            },
        )
    } else if (dialogType == DialogType.AddTournamentNote) {
        AddTournamentNoteScreen(
            onDismiss = {
                isDialogVisible = false
                onRefresh()
            },
        )
    }
}
