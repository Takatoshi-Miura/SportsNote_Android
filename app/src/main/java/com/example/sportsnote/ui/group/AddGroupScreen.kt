package com.example.sportsnote.ui.group

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.sportsnote.R
import com.example.sportsnote.ui.components.header.AddScreenHeader
import com.example.sportsnote.utils.Color

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
    val coroutineScope = rememberCoroutineScope()
    var title by remember { mutableStateOf("") }
    var color by remember { mutableStateOf(Color.RED.id) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.surface)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // ヘッダー
                AddScreenHeader(
                    title = stringResource(R.string.addGroup),
                    onCancel = onDismiss,
                    onSave = {
                        viewModel.saveGroup(
                            title = title,
                            colorId = color,
                            order = null
                        )
                        onDismiss()
                    },
                    coroutineScope = coroutineScope
                )

                // 共通フォーム
                GroupFormContent(
                    initialTitle = title,
                    initialColor = color,
                    onTitleChange = { title = it },
                    onColorChange = { color = it }
                )
            }
        }
    }
}