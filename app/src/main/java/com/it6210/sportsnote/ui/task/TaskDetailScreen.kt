package com.it6210.sportsnote.ui.task

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.it6210.sportsnote.R
import com.it6210.sportsnote.ui.LocalNavController
import com.it6210.sportsnote.ui.components.CustomAlertDialog
import com.it6210.sportsnote.ui.components.DialogType
import com.it6210.sportsnote.ui.components.TextInputDialog
import com.it6210.sportsnote.ui.components.header.NavigationScreenHeader
import com.it6210.sportsnote.ui.components.items.AutoSaveTimestamp
import com.it6210.sportsnote.ui.components.items.CustomFloatingActionButton
import com.it6210.sportsnote.ui.components.items.CustomSpacerColumn
import com.it6210.sportsnote.ui.components.items.GroupPickerField
import com.it6210.sportsnote.ui.components.items.MultiLineTextInputField
import com.it6210.sportsnote.ui.group.GroupViewModel
import com.it6210.sportsnote.ui.measures.MeasuresViewModel
import com.it6210.sportsnote.ui.measures.components.MeasuresListContent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date

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
    appBarRightIcon: MutableState<(@Composable () -> Unit)?>,
) {
    val taskViewModel = TaskViewModel()
    val groupViewModel = GroupViewModel()
    val measuresViewModel = MeasuresViewModel()
    val taskDetail = taskViewModel.getTaskByTaskId(taskId)
    val coroutineScope = rememberCoroutineScope()
    val navController = LocalNavController.current
    val context = LocalContext.current

    // 入力データ
    var title by remember { mutableStateOf(taskDetail.task.title) }
    var cause by remember { mutableStateOf(taskDetail.task.cause) }
    var groupId by remember { mutableStateOf(taskDetail.task.groupID) }
    val isComplete by remember { mutableStateOf(taskDetail.task.isComplete) }
    val showDialog = remember { mutableStateOf(false) }
    var dialogType by remember { mutableStateOf(DialogType.None) }
    val inputText = remember { mutableStateOf("") }
    val measuresList = remember { mutableStateListOf(*taskDetail.measuresList.toTypedArray()) }
    val groups by groupViewModel.groups.collectAsState()
    val selectedGroup = groupViewModel.getGroupById(groupId)
    var lastSavedAt by remember { mutableStateOf(taskDetail.task.updated_at) }

    val inputFields: List<@Composable () -> Unit> =
        listOf(
            // タイトル
            {
                MultiLineTextInputField(
                    title = stringResource(R.string.title),
                    onTextChanged = { updatedText -> title = updatedText },
                    initialText = title,
                )
            },
            // 原因
            {
                MultiLineTextInputField(
                    title = stringResource(R.string.cause),
                    defaultLines = 3,
                    onTextChanged = { updatedText -> cause = updatedText },
                    initialText = cause,
                )
            },
            // グループ選択
            {
                GroupPickerField(
                    groups = groups,
                    onGroupSelected = { selectedGroup -> groupId = selectedGroup.groupID },
                    initialGroup = selectedGroup!!,
                )
            },
            // 対策
            {
                Text(
                    text = stringResource(R.string.measures) + stringResource(R.string.sortByPriority),
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(8.dp),
                )
                Divider()
                MeasuresListContent(
                    measuresList = measuresList,
                    onOrderChanged = { updatedList ->
                        coroutineScope.launch {
                            // 並び順を更新
                            updatedList.forEach { measure ->
                                measuresViewModel.saveMeasures(
                                    measuresId = measure.measuresID,
                                    taskId = measure.taskID,
                                    title = measure.title,
                                    order = measure.order,
                                    created_at = measure.created_at,
                                )
                            }
                        }
                    },
                    onItemClick = { measuresID ->
                        coroutineScope.launch {
                            // 非同期でsaveTaskを実行
                            taskViewModel.saveTask(
                                taskId = taskId,
                                title = title,
                                cause = cause,
                                groupId = groupId,
                                isComplete = isComplete,
                                created_at = taskDetail.task.created_at,
                            )
                            navController.navigate("measures/$measuresID")
                        }
                    },
                )
            },
        )

    // 自動保存処理
    LaunchedEffect(title, cause, groupId, measuresList.toList()) {
        delay(1000)
        if (title.isNotEmpty()) {
            taskViewModel.saveTask(
                taskId = taskId,
                title = title,
                cause = cause,
                groupId = groupId,
                isComplete = isComplete,
                created_at = taskDetail.task.created_at,
            )
            lastSavedAt = Date()
        }
    }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // ヘッダー
            NavigationScreenHeader(
                onDismiss = onBack,
                onSave = {
                    if (title.isEmpty()) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.emptyTitle),
                            Toast.LENGTH_LONG,
                        ).show()
                        return@NavigationScreenHeader
                    }
                    // 保存処理
                    taskViewModel.saveTask(
                        taskId = taskId,
                        title = title,
                        cause = cause,
                        groupId = groupId,
                        isComplete = isComplete,
                        created_at = taskDetail.task.created_at,
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
                },
            )

            AutoSaveTimestamp(lastSavedAt)

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
            showDialog = showDialog,
        )
    } else if (dialogType == DialogType.AddMeasure) {
        // 対策追加ダイアログ
        TextInputDialog(
            title = stringResource(R.string.addMeasures),
            message = stringResource(R.string.addMeasuresMessage),
            inputTextState = inputText,
            onConfirm = { input ->
                coroutineScope.launch {
                    if (input.isEmpty()) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.emptyTitle),
                            Toast.LENGTH_LONG,
                        ).show()
                        return@launch
                    }
                    // 対策追加
                    val newMeasure =
                        measuresViewModel.saveMeasures(
                            title = input,
                            taskId = taskId,
                        )
                    newMeasure.let { measuresList.add(it) }
                    showDialog.value = false
                }
            },
            showDialog = showDialog,
        )
    } else if (dialogType == DialogType.CompleteTask) {
        val message =
            if (isComplete) {
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
                        created_at = taskDetail.task.created_at,
                    )
                    showDialog.value = false
                    onBack()
                }
            },
            showDialog = showDialog,
        )
    }
}
