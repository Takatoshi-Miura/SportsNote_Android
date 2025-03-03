package com.example.sportsnote.ui.task.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.sportsnote.R
import com.example.sportsnote.model.Group
import com.example.sportsnote.model.TaskListData
import com.example.sportsnote.ui.LocalNavController
import com.example.sportsnote.ui.components.AdMobBanner
import com.example.sportsnote.ui.group.components.GroupHeaderView

/**
 * 課題一覧のリスト表示
 *
 * @param groups グループリスト
 * @param taskList 課題リスト
 * @param onInfoButtonClick グループのInfoボタンの処理
 */
@Composable
fun TaskListContent(
    groups: List<Group>,
    taskList: List<TaskListData>,
    onInfoButtonClick: (Group) -> Unit,
) {
    val navController = LocalNavController.current

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.background),
        ) {
            items(groups) { group ->
                // グループ表示
                GroupHeaderView(
                    title = group.title,
                    colorId = group.color,
                    onInfoButtonClick = { onInfoButtonClick(group) },
                )
                Divider()

                // 課題セルのリスト
                val groupTasks =
                    taskList
                        .filter { it.groupID == group.groupID }
                        .sortedBy { it.order }
                groupTasks.forEach { task ->
                    TaskCell(task)
                    Divider()
                }

                // 完了した課題セル
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .clickable {
                                // CompletedTaskScreenに遷移する
                                navController.navigate("completed_task/${group.groupID}")
                            },
                ) {
                    Text(
                        text = stringResource(R.string.completedTask),
                        color = MaterialTheme.colors.primary,
                        style = MaterialTheme.typography.body2,
                        modifier =
                            Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 8.dp),
                    )
                }
                Divider()
            }
        }

        // バナー広告を最下位に表示
        AdMobBanner()
    }
}
