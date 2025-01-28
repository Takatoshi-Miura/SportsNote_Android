package com.example.sportsnote.ui.task

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sportsnote.R
import com.example.sportsnote.model.Group
import com.example.sportsnote.model.TaskListData
import com.example.sportsnote.ui.LocalNavController
import com.example.sportsnote.ui.components.ActionBottomSheetContent
import com.example.sportsnote.ui.components.CustomFloatingActionButton
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
        groupViewModel.loadData()
        taskViewModel.loadData()
    }

    LaunchedEffect(Unit) {
        onRefresh()
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            val actionItems = listOf(
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
                TaskListScreen(
                    groups = groups,
                    taskList = taskLists,
                    onInfoButtonClick = { group ->
                        // GroupViewScreenに遷移する
                        navController.navigate("group_view_screen/${group.groupID}")
                    }
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
            }
        )
    }
    // 課題追加モーダル表示
    if (taskDialogVisible) {
        AddTaskScreen(
            onDismiss = {
                taskDialogVisible = false
                onRefresh()
            }
        )
    }
}

/**
 * 課題一覧のリスト表示
 *
 * @param groups グループリスト
 * @param taskList 課題リスト
 * @param onInfoButtonClick グループのInfoボタンの処理
 */
@Composable
fun TaskListScreen(
    groups: List<Group>,
    taskList: List<TaskListData>,
    onInfoButtonClick: (Group) -> Unit
) {
    val navController = LocalNavController.current

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        items(groups) { group ->
            // グループ表示
            GroupHeaderView(
                title = group.title,
                colorId = group.color,
                onInfoButtonClick = { onInfoButtonClick(group) }
            )
            Divider()

            // 課題セルのリスト
            val groupTasks = taskList
                .filter { it.groupID == group.groupID }
                .sortedBy { it.order }
            groupTasks.forEach { task ->
                TaskCell(task)
                Divider()
            }

            // 完了した課題セル
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clickable {
                        // CompletedTaskScreenに遷移する
                        navController.navigate("completed_task/${group.groupID}")
                    }
            ) {
                Text(
                    text = stringResource(R.string.completedTask),
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 8.dp)
                )
            }
            Divider()
        }
    }
}