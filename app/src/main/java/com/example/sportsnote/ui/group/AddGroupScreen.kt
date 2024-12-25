package com.example.sportsnote.ui.group

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
import com.example.sportsnote.ui.components.ColorPickerField
import com.example.sportsnote.ui.components.CustomSpacerColumn
import com.example.sportsnote.ui.components.MultiLineTextInputField
import com.example.sportsnote.utils.Color
import kotlinx.coroutines.launch

/**
 * グループ登録画面
 *
 * @param onDismiss 閉じる際の処理
 */
@Composable
fun AddGroupScreen(
    viewModel: GroupViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        AddGroupContent(
            viewModel = viewModel,
            onDismiss = { onDismiss() }
        )
    }
}

@Composable
fun AddGroupContent(
    viewModel: GroupViewModel,
    onDismiss: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    // 入力データの状態管理
    val title = remember { mutableStateOf("") }
    val color = remember { mutableStateOf(Color.RED.id) }

    // 入力項目
    val inputFields: List<@Composable () -> Unit> = listOf (
        { MultiLineTextInputField(stringResource(R.string.title)) { input -> title.value = input } },
        { ColorPickerField { selectedColor -> color.value = selectedColor } }
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
                    text = "グループの追加",
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.align(Alignment.Center)
                )
                // 保存
                Button(
                    onClick = {
                        coroutineScope.launch {
                            // 保存処理
                            viewModel.saveGroup(
                                title = title.value,
                                colorId = color.value
                            )
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
            // 入力欄
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                CustomSpacerColumn(
                    items = inputFields
                )
            }
        }
    }
}