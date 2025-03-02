package com.example.sportsnote.ui.note

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
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
import com.example.sportsnote.R
import com.example.sportsnote.model.PreferencesManager
import com.example.sportsnote.model.SyncManager
import com.example.sportsnote.ui.LocalNavController
import com.example.sportsnote.ui.components.ActionBottomSheetContent
import com.example.sportsnote.ui.components.CustomFloatingActionButton
import com.example.sportsnote.ui.components.DialogType
import com.example.sportsnote.ui.note.components.NoteListContent
import com.example.sportsnote.ui.note.components.SearchBar
import com.example.sportsnote.utils.NoteType
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
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
