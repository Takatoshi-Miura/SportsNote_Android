package com.example.sportsnote.ui.group

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.sportsnote.R
import com.example.sportsnote.model.Group
import com.example.sportsnote.ui.components.CustomAlertDialog
import com.example.sportsnote.ui.components.header.NavigationScreenHeader
import com.example.sportsnote.utils.Color
import kotlinx.coroutines.launch
import java.util.Date

/**
 * Group詳細画面
 *
 * @param viewModel GroupViewModel
 * @param groupId groupId
 * @param onBack 前画面に戻る処理
 * @param appBarNavigationIcon TopBar左のボタンを変更するために必要
 * @param appBarRightIcon TopBar右のボタンを変更するために必要
 */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GroupViewScreen(
    viewModel: GroupViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    groupId: String,
    onBack: () -> Unit,
    appBarNavigationIcon: MutableState<(@Composable () -> Unit)?>,
    appBarRightIcon: MutableState<(@Composable () -> Unit)?>
) {
    val group: Group? = viewModel.getGroupById(groupId)
    val coroutineScope = rememberCoroutineScope()

    // 入力データの状態管理
    var title by remember { mutableStateOf("") }
    var color by remember { mutableStateOf(Color.RED.id) }
    val showDialog = remember { mutableStateOf(false) }

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
                    viewModel.saveGroup(
                        groupId = groupId,
                        title = title,
                        colorId = color,
                        order = group?.order,
                        created_at = group?.created_at ?: Date()
                    )
                },
                onDelete = {
                    showDialog.value = true
                },
                onEdit = null,
                updateAppBar = { navigationIcon, rightIcon ->
                    appBarNavigationIcon.value = navigationIcon
                    appBarRightIcon.value = rightIcon
                }
            )

            // 共通フォーム
            GroupFormContent(
                group = group,
                onTitleChange = { title = it },
                onColorChange = { color = it }
            )
        }
    }

    // 削除確認ダイアログの表示
    if (showDialog.value) {
        CustomAlertDialog(
            title = stringResource(R.string.deleteGroup),
            message = stringResource(R.string.deleteGroupMessage),
            confirmButtonText = stringResource(R.string.delete),
            onConfirm = {
                coroutineScope.launch {
                    viewModel.deleteGroup(groupId)
                    showDialog.value = false
                    onBack()
                }
            },
            showDialog = showDialog
        )
    }
}