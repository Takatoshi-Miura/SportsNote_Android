package com.example.sportsnote.ui.task

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.sportsnote.R
import com.example.sportsnote.model.TaskListData
import com.example.sportsnote.ui.LocalNavController

/**
 * 課題セル
 *
 * @param task TaskListData
 */
@Composable
fun TaskCell(task: TaskListData) {
    val navController = LocalNavController.current

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable {
                    // TaskDetailScreenに遷移する
                    navController.navigate("detail_task/${task.taskID}")
                },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
        ) {
            // タイトル
            Text(
                text = task.title,
                style = MaterialTheme.typography.body1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            // 対策
            Text(
                text = stringResource(R.string.measuresLabel, task.measures),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
