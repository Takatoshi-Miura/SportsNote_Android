package com.example.sportsnote.ui.task

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sportsnote.model.Group
import com.example.sportsnote.ui.LocalNavController
import com.example.sportsnote.ui.components.ActionBottomSheetContent
import com.example.sportsnote.ui.group.AddGroupScreen
import com.example.sportsnote.ui.group.GroupHeaderView
import com.example.sportsnote.ui.group.GroupViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

/**
 * 課題一覧画面
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TaskScreen(
    taskViewModel: TaskViewModel = viewModel(),
    groupViewModel: GroupViewModel = viewModel()
) {
    val groups by groupViewModel.groups.collectAsState()
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    val navController = LocalNavController.current
    val BOTTOM_NAVIGATION_HEIGHT = 56.dp
    var isDialogVisible by remember { mutableStateOf(false) } // ダイアログの表示フラグ
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)

    // 一覧のリフレッシュ処理
    val onRefresh = {
        groupViewModel.loadData()
//        taskViewModel.loadNotes()
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            val actionItems = listOf(
                "グループを追加" to {
                    isDialogVisible = true
                    coroutineScope.launch { sheetState.hide() }
                },
                "課題を追加" to {
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
                // 課題一覧
                GroupListScreen(
                    groups = groups,
                    onInfoButtonClick = { group ->
                        // GroupViewScreenに遷移する
                        navController.navigate("group_view_screen/${group.groupID}")
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
                Icon(Icons.Filled.Add, contentDescription = "Add Task")
            }
        }
    }

    // ダイアログでフルスクリーンモーダルを表示
    if (isDialogVisible) {
        AddGroupScreen(
            onDismiss = { isDialogVisible = false }
        )
    }
}

@Composable
fun GroupListScreen(groups: List<Group>, onInfoButtonClick: (Group) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        groups.forEach { group ->
            GroupHeaderView(
                title = group.title,
                colorId = group.color,
                onInfoButtonClick = { onInfoButtonClick(group) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLazySectionedColumn() {
    TaskScreen()
}