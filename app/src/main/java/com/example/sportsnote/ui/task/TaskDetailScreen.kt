package com.example.sportsnote.ui.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.sportsnote.R
import com.example.sportsnote.ui.LocalNavController
import com.example.sportsnote.ui.components.CustomAlertDialog
import com.example.sportsnote.ui.components.CustomFloatingActionButton
import com.example.sportsnote.ui.components.CustomSpacerColumn
import com.example.sportsnote.ui.components.DialogType
import com.example.sportsnote.ui.components.MultiLineTextInputField
import com.example.sportsnote.ui.components.TextInputDialog
import com.example.sportsnote.ui.components.header.NavigationScreenHeader
import com.example.sportsnote.ui.measures.MeasuresListContent
import com.example.sportsnote.ui.measures.MeasuresViewModel
import kotlinx.coroutines.launch

/**
 * Task詳細画面
 *
 * @param taskId taskId
 * @param onBack 前画面に戻る処理
 * @param appBarNavigationIcon TopBar左のボタンを変更するために必要
 * @param appBarRightIcon TopBar右のボタンを変更するために必要
 */
@Composable
fun TaskDetailScreen(
    taskId: String,
    onBack: () -> Unit,
    appBarNavigationIcon: MutableState<(@Composable () -> Unit)?>,
    appBarRightIcon: MutableState<(@Composable () -> Unit)?>
) {
    val taskViewModel = TaskViewModel()
    val measuresViewModel = MeasuresViewModel()
    val taskDetail = taskViewModel.getTaskByTaskId(taskId)
    val coroutineScope = rememberCoroutineScope()
    val navController = LocalNavController.current

    // 入力データ
    var title by remember { mutableStateOf(taskDetail.task.title) }
    var cause by remember { mutableStateOf(taskDetail.task.cause) }
    var isComplete by remember { mutableStateOf(taskDetail.task.isComplete) }
    val showDialog = remember { mutableStateOf(false) }
    var dialogType by remember { mutableStateOf(DialogType.None) }
    val inputText = remember { mutableStateOf("") }

    val inputFields: List<@Composable () -> Unit> = listOf(
        // タイトル
        {
            MultiLineTextInputField(
                title = stringResource(R.string.title),
                onTextChanged = { updatedText -> title = updatedText },
                initialText = title
            )
        },
        // 原因
        {
            MultiLineTextInputField(
                title = stringResource(R.string.cause),
                defaultLines = 3,
                onTextChanged = { updatedText -> cause = updatedText },
                initialText = cause
            )
        },
        // 対策
        {
            Text(
                text = stringResource(R.string.measures),
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(8.dp)
            )
            MeasuresListContent(
                measuresList = taskDetail.measuresList,
                onOrderChanged = {
                    // TODO: 対策の並び順を更新
                },
                onItemClick = { measuresID ->
                    coroutineScope.launch {
                        // 非同期でsaveTaskを実行
                        taskViewModel.saveTask(
                            taskId = taskId,
                            title = title,
                            cause = cause,
                            groupId = taskDetail.task.groupID,
                            isComplete = isComplete,
                            created_at = taskDetail.task.created_at
                        )
                        navController.navigate("measures/${measuresID}")
                    }
                }
            )
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // ヘッダー
            NavigationScreenHeader(
                onDismiss = onBack,
                onSave = {
                    // 保存処理
                    taskViewModel.saveTask(
                        taskId = taskId,
                        title = title,
                        cause = cause,
                        groupId = taskDetail.task.groupID,
                        isComplete = isComplete,
                        created_at = taskDetail.task.created_at
                    )
                },
                onDelete = {
                    dialogType = DialogType.Delete
                    showDialog.value = true
                },
                onEdit = {
                    dialogType = DialogType.CompleteTask
                    showDialog.value = true
                },
                updateAppBar = { navigationIcon, rightIcon ->
                    appBarNavigationIcon.value = navigationIcon
                    appBarRightIcon.value = rightIcon
                }
            )

            // 共通フォーム
            CustomSpacerColumn(items = inputFields)
        }

        // +ボタン
        CustomFloatingActionButton {
            coroutineScope.launch {
                dialogType = DialogType.AddMeasure
                showDialog.value = true
            }
        }
    }

    if (!showDialog.value) return
    if (dialogType == DialogType.Delete) {
        // 削除確認ダイアログの表示
        CustomAlertDialog(
            title = stringResource(R.string.deleteTask),
            message = stringResource(R.string.deleteTaskMessage),
            confirmButtonText = stringResource(R.string.delete),
            onConfirm = {
                coroutineScope.launch {
                    // 削除処理
                    taskViewModel.deleteTaskData(taskId)
                    showDialog.value = false
                    onBack()
                }
            },
            showDialog = showDialog
        )
    } else if (dialogType == DialogType.AddMeasure) {
        // 対策追加ダイアログ
        TextInputDialog(
            title = stringResource(R.string.addMeasures),
            message = stringResource(R.string.addMeasuresMessage),
            inputTextState = inputText,
            onConfirm = { input ->
                coroutineScope.launch {
                    // 対策追加
                    measuresViewModel.saveMeasures(
                        title = input,
                        taskId = taskId
                    )
                    // TODO: UIに反映
                    showDialog.value = false
                }
            },
            showDialog = showDialog
        )
    } else if (dialogType == DialogType.CompleteTask) {
        val message = if (isComplete) {
            stringResource(R.string.notCompleteTask)
        } else {
            stringResource(R.string.completeTaskMessage)
        }
        // 完了確認ダイアログの表示
        CustomAlertDialog(
            title = stringResource(R.string.taskProgress),
            message = message,
            onConfirm = {
                coroutineScope.launch {
                    // 完了処理
                    taskViewModel.saveTask(
                        taskId = taskId,
                        title = title,
                        cause = cause,
                        groupId = taskDetail.task.groupID,
                        isComplete = !isComplete,
                        created_at = taskDetail.task.created_at
                    )
                    showDialog.value = false
                    onBack()
                }
            },
            showDialog = showDialog
        )
    }
}