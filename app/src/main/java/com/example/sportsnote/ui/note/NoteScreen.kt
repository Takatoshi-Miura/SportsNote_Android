package com.example.sportsnote.ui.note

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sportsnote.ui.LocalNavController
import com.example.sportsnote.ui.components.ActionBottomSheetContent
import com.example.sportsnote.ui.components.LazyNonSectionedColumn
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
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
    var isDialogVisible by remember { mutableStateOf(false) } // ダイアログの表示フラグ
    val navController = LocalNavController.current
    val navigateToAddNote by noteViewModel.navigateToAddNote.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)

    // ナビゲーション遷移が要求された場合
    if (navigateToAddNote) {
        LaunchedEffect(Unit) {
            navController.navigate("add_tournament_note")
            noteViewModel.onNavigationHandled()
        }
    }

    // ノート一覧のリフレッシュ処理
    val onRefresh = {
        noteViewModel.loadNotes()
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            val actionItems = listOf(
                "練習ノートを追加" to {
                    // 練習ノート追加処理（仮）
                    coroutineScope.launch { sheetState.hide() }
                },
                "大会ノートを追加" to {
                    isDialogVisible = true
                    coroutineScope.launch { sheetState.hide() }
                },
                "キャンセル" to {
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
                LazyNonSectionedColumn(items = items)
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
            isNavigation = false,
            onDismiss = { isDialogVisible = false }
        )
    }
}
