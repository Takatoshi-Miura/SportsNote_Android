package com.it6210.sportsnote.ui.note.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.it6210.sportsnote.R
import com.it6210.sportsnote.model.TaskListData
import com.it6210.sportsnote.ui.components.CustomAlertDialog
import com.it6210.sportsnote.ui.components.items.ItemLabel
import com.it6210.sportsnote.ui.components.items.MultiLineTextInputField

/**
 * 取り組んだ課題のセクション表示
 *
 * @param title セクションタイトル
 * @param onAddClick ＋ボタン押下時の処理
 */
@Composable
fun TaskHeader(
    title: String,
    onAddClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // タイトルラベル
        ItemLabel(
            title = title,
            modifier = Modifier.weight(1f),
        )
        // ＋ボタン
        IconButton(onClick = onAddClick) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Task",
            )
        }
    }
}

/**
 * 取り組んだ課題セルのコンポーネント
 *
 * @param taskDataList 取り組んだ課題データとそのメモのMap
 * @param onTaskRemoved 課題削除処理
 * @param onReflectionChanged メモ変更時の処理
 */
@Composable
fun TaskListInput(
    taskDataList: Map<TaskListData, String>,
    onTaskRemoved: (TaskListData) -> Unit,
    onReflectionChanged: (TaskListData, String) -> Unit,
) {
    val showDialog = remember { mutableStateOf(false) }
    val selectedTask = remember { mutableStateOf<TaskListData?>(null) }

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Divider()
        taskDataList.forEach { (taskData, reflectionText) ->
            TaskInputItem(
                taskData = taskData,
                initialReflection = reflectionText,
                onOptionClick = {
                    selectedTask.value = taskData
                    showDialog.value = true
                },
                onReflectionChanged = { updatedText ->
                    onReflectionChanged(taskData, updatedText)
                },
            )
            Divider()
        }
    }

    // 削除ダイアログ
    if (showDialog.value) {
        CustomAlertDialog(
            title = stringResource(R.string.deleteTaskFromNote),
            message = stringResource(R.string.deleteTaskFromNoteMessage),
            onConfirm = {
                selectedTask.value?.let { onTaskRemoved(it) }
                showDialog.value = false
            },
            showDialog = showDialog,
        )
    }
}

/**
 * 取り組んだ課題セルのコンポーネント
 *
 * @param taskData 課題データ
 * @param initialReflection メモの初期値
 * @param onOptionClick オプションボタン押下時の処理
 * @param onReflectionChanged メモ変更時の処理
 */
@Composable
fun TaskInputItem(
    taskData: TaskListData,
    initialReflection: String,
    onOptionClick: () -> Unit,
    onReflectionChanged: (String) -> Unit,
) {
    val reflectionText = remember { mutableStateOf(initialReflection) }

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier =
                Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // グループカラー
            Box(
                modifier =
                    Modifier
                        .padding(start = 12.dp)
                        .width(20.dp)
                        .height(50.dp)
                        .background(taskData.groupColor),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f),
            ) {
                // 課題タイトル
                Text(
                    text = taskData.title,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                // 最優先の対策
                Text(
                    text = stringResource(R.string.measuresLabel, taskData.measures),
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            // オプションボタン
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Options",
                modifier =
                    Modifier
                        .padding(end = 12.dp)
                        .clickable { onOptionClick() },
            )
        }
        // メモ欄
        MultiLineTextInputField(
            title = "",
            placeholder = "入力してください",
            onTextChanged = { text ->
                reflectionText.value = text
                onReflectionChanged(text)
            },
            initialText = reflectionText.value,
        )
    }
}
