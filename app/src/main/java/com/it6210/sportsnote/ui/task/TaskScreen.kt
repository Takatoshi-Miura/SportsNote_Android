package com.it6210.sportsnote.ui.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.it6210.sportsnote.model.PreferencesManager
import com.it6210.sportsnote.model.SyncManager
import com.it6210.sportsnote.ui.LocalNavController
import com.it6210.sportsnote.ui.components.ActionBottomSheetContent
import com.it6210.sportsnote.ui.components.CustomFloatingActionButton
import com.it6210.sportsnote.ui.group.AddGroupScreen
import com.it6210.sportsnote.ui.group.GroupViewModel
import com.it6210.sportsnote.ui.task.components.TaskListContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 課題一覧画面
 *
 * @param reloadTrigger リロードトリガー
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TaskScreen(reloadTrigger: Int) {
    val taskViewModel = TaskViewModel()
    val groupViewModel = GroupViewModel()
    val taskLists by taskViewModel.taskLists.collectAsState()
    val groups by groupViewModel.groups.collectAsState()
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    val navController = LocalNavController.current
    var groupDialogVisible by remember { mutableStateOf(false) }
    var taskDialogVisible by remember { mutableStateOf(false) }
    val isTaskLoading by taskViewModel.isLoading.collectAsState()
    val isGroupLoading by groupViewModel.isLoading.collectAsState()
    val isRefreshing = isTaskLoading || isGroupLoading
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)

    // 一覧のリフレッシュ処理
    val onRefresh = {
        coroutineScope.launch {
            if (PreferencesManager.get(PreferencesManager.Keys.IS_LOGIN, false)) {
                withContext(Dispatchers.IO) {
                    SyncManager.syncAllData()
                }
            }
            groupViewModel.loadData()
            taskViewModel.loadData()
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
                    stringResource(R.string.addGroupAction) to {
                        groupDialogVisible = true
                        coroutineScope.launch { sheetState.hide() }
                    },
                    stringResource(R.string.addTaskAction) to {
                        taskDialogVisible = true
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
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.surface),
        ) {
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = { onRefresh() },
            ) {
                // 課題一覧
                TaskListContent(
                    groups = groups,
                    taskList = taskLists,
                    onInfoButtonClick = { group ->
                        // GroupViewScreenに遷移する
                        navController.navigate("group_view_screen/${group.groupID}")
                    },
                )
            }

            // +ボタン
            CustomFloatingActionButton {
                coroutineScope.launch { sheetState.show() }
            }
        }
    }

    // グループ追加モーダル表示
    if (groupDialogVisible) {
        AddGroupScreen(
            onDismiss = {
                groupDialogVisible = false
                onRefresh()
            },
        )
    }
    // 課題追加モーダル表示
    if (taskDialogVisible) {
        AddTaskScreen(
            onDismiss = {
                taskDialogVisible = false
                onRefresh()
            },
        )
    }
}
