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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
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
import com.example.sportsnote.model.Group
import com.example.sportsnote.ui.components.ColorPickerField
import com.example.sportsnote.ui.components.CustomSpacerColumn
import com.example.sportsnote.ui.components.MultiLineTextInputField
import com.example.sportsnote.utils.Color
import kotlinx.coroutines.launch

/**
 * Group登録画面
 *
 * @param viewModel GroupViewModel
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
            onDismiss = { onDismiss() },
            updateAppBar = { _, _ -> },
            isDialog = true
        )
    }
}

/**
 * Groupの情報入力画面のUI
 *
 * @param viewModel GroupViewModel
 * @param groupId groupId(編集時のみ必須)
 * @param onDismiss 前画面に戻る処理
 * @param updateAppBar TopBarを編集するために必要(編集時のみ必須)
 * @param isDialog ダイアログ表示かどうか
 */
@Composable
fun AddGroupContent(
    viewModel: GroupViewModel,
    groupId: String? = null,
    onDismiss: () -> Unit,
    updateAppBar: (@Composable (() -> Unit)?, @Composable (() -> Unit)?) -> Unit,
    isDialog: Boolean
) {
    val coroutineScope = rememberCoroutineScope()

    // 入力データの状態管理
    val title = remember { mutableStateOf("") }
    val color = remember { mutableStateOf(Color.RED.id) }

    // グループ詳細画面の場合、対象のGroupを取得
    val group: Group? = if (groupId != null) {
        viewModel.getGroupById(groupId)
    } else {
        null
    }

    // Groupが取得できた場合に初期値を設定
    LaunchedEffect(group) {
        group?.let {
            title.value = it.title
            color.value = it.color
        }
    }

    // 入力項目
    val inputFields: List<@Composable () -> Unit> = listOf (
        {
            MultiLineTextInputField(
                title = stringResource(R.string.title),
                onTextChanged = { updatedText -> title.value = updatedText },
                initialText = group?.title ?: ""
            )
        },
        {
            ColorPickerField(
                onColorSelected = { selectedColor -> color.value = selectedColor },
                initialColor = group?.color ?: Color.RED.id
            )
        }
    )

    if (isDialog == false) {
        // CustomTopAppBar の設定を更新
        SideEffect {
            updateAppBar(
                {
                    IconButton(onClick = {
                        // 更新処理
                        coroutineScope.launch {
                            viewModel.saveGroup(
                                groupId = group?.groupID!!,
                                title = title.value,
                                colorId = color.value,
                                order = group.order
                            )
                        }
                        onDismiss()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                {
                    IconButton(onClick = {
                        // TODO: 削除処理
                        onDismiss()
                    }) {
                        Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
                    }
                }
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (isDialog) {
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
                                    colorId = color.value,
                                    order = null
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