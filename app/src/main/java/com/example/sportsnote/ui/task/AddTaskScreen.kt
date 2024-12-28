package com.example.sportsnote.ui.task

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.sportsnote.R
import com.example.sportsnote.model.Group
import com.example.sportsnote.ui.components.CustomSpacerColumn
import com.example.sportsnote.ui.components.GroupPickerField
import com.example.sportsnote.ui.components.header.AddScreenHeader
import com.example.sportsnote.ui.components.MultiLineTextInputField
import com.example.sportsnote.ui.group.GroupViewModel

/**
 * 課題追加画面
 *
 * @param viewModel TaskViewModel
 * @param onDismiss 画面閉じる時の処理
 */
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AddTaskScreen(
    viewModel: TaskViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onDismiss: () -> Unit,
) {
    val groupViewModel: GroupViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val groups = groupViewModel.groups.value

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        AddTaskContent(
            viewModel = viewModel,
            groups = groups,
            onDismiss = { onDismiss() },
        )
    }
}

/**
 * 課題追加画面のUI
 *
 * @param viewModel TaskViewModel
 * @param groups Groupリスト
 * @param onDismiss 閉じる時の処理
 */
@Composable
fun AddTaskContent(
    viewModel: TaskViewModel,
    groups: List<Group>,
    onDismiss: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    // 入力データの状態管理
    val title = remember { mutableStateOf("") }
    val cause = remember { mutableStateOf("") }
    val measures = remember { mutableStateOf("") }
    val group = remember { mutableStateOf(groups.first()) }

    // 入力項目
    val inputFields: List<@Composable () -> Unit> = listOf (
        // タイトル
        {
            MultiLineTextInputField(
                title = stringResource(R.string.title),
                onTextChanged = { updatedText -> title.value = updatedText },
                initialText = title.value
            )
        },
        // 原因
        {
            MultiLineTextInputField(
                title = stringResource(R.string.cause),
                defaultLines = 3,
                onTextChanged = { updatedText -> cause.value = updatedText },
                initialText = cause.value
            )
        },
        // 対策
        {
            MultiLineTextInputField(
                title = stringResource(R.string.measures),
                onTextChanged = { updatedText -> measures.value = updatedText },
                initialText = measures.value
            )
        },
        // グループ
        {
            GroupPickerField(
                groups = groups,
                onGroupSelected = { updateGroup -> group.value = updateGroup },
                initialGroup = group.value
            )
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
    ) {
        AddScreenHeader(
            title = stringResource(R.string.addTask),
            onCancel = onDismiss,
            onSave = {
                // TODO: 保存処理を実装
//                viewModel.saveTask()
                onDismiss()
            },
            coroutineScope = coroutineScope
        )
        // 入力欄
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 56.dp, start = 16.dp, end = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            CustomSpacerColumn(
                items = inputFields
            )
        }
    }
}