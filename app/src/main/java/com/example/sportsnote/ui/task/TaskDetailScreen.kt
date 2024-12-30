package com.example.sportsnote.ui.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.sportsnote.R
import com.example.sportsnote.ui.components.CustomAlertDialog
import com.example.sportsnote.ui.components.CustomSpacerColumn
import com.example.sportsnote.ui.components.MultiLineTextInputField
import com.example.sportsnote.ui.components.header.NavigationScreenHeader
import com.example.sportsnote.ui.measures.MeasuresListContent
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
    val taskDetail = taskViewModel.getTaskByTaskId(taskId)
    val coroutineScope = rememberCoroutineScope()

    // 入力データ
    var title by remember { mutableStateOf(taskDetail.task.title) }
    var cause by remember { mutableStateOf(taskDetail.task.cause) }
    val showDialog = remember { mutableStateOf(false) }

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
                    taskDetail.measuresList.forEachIndexed { index, measure ->
                        measure.order = index
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
                    // TODO: 保存処理
                },
                onDelete = {
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
        FloatingActionButton(
            onClick = {
                coroutineScope.launch {
                    // TODO: 対策追加ダイアログ
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = 16.dp,
                    bottom = 16.dp + 56.dp
                )
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add Measures")
        }
    }

    // 削除確認ダイアログの表示
    if (showDialog.value) {
        CustomAlertDialog(
            title = stringResource(R.string.deleteTask),
            message = stringResource(R.string.deleteTaskMessage),
            confirmButtonText = stringResource(R.string.delete),
            onConfirm = {
                coroutineScope.launch {
                    // TODO: 削除処理
//                    viewModel.deleteGroup(groupId)
                    showDialog.value = false
                    onBack()
                }
            },
            showDialog = showDialog
        )
    }
}