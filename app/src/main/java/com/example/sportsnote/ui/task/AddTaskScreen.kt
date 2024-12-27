package com.example.sportsnote.ui.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.sportsnote.R
import com.example.sportsnote.ui.components.CustomSpacerColumn
import com.example.sportsnote.ui.components.MultiLineTextInputField
import kotlinx.coroutines.launch

/**
 * 課題追加画面
 *
 * @param viewModel TaskViewModel
 * @param onDismiss 画面閉じる時の処理
 */
@Composable
fun AddTaskScreen(
    viewModel: TaskViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        AddTaskContent(
            viewModel = viewModel,
            onDismiss = { onDismiss() },
        )
    }
}

/**
 * 課題追加画面のUI
 *
 * @param viewModel TaskViewModel
 * @param onDismiss 閉じる時の処理
 */
@Composable
fun AddTaskContent(
    viewModel: TaskViewModel,
    onDismiss: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    // 入力データの状態管理
    val title = remember { mutableStateOf("") }

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
                onTextChanged = { updatedText -> title.value = updatedText },
                initialText = title.value
            )
        },
        // 対策
        {
            MultiLineTextInputField(
                title = stringResource(R.string.measures),
                onTextChanged = { updatedText -> title.value = updatedText },
                initialText = title.value
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(MaterialTheme.colors.primary)
            ) {
                // キャンセル
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 8.dp)
                ) {
                    Text(stringResource(R.string.cancel))
                }
                // タイトル
                Text(
                    text = "課題の追加",
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.align(Alignment.Center)
                )
                // 保存
                Button(
                    onClick = {
                        coroutineScope.launch {
                            // TODO: 保存処理
                        }
                        onDismiss()
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 8.dp)
                ) {
                    Text(stringResource(R.string.save))
                }
            }
        }
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