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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.sportsnote.model.Group
import com.example.sportsnote.ui.components.header.NavigationScreenHeader
import com.example.sportsnote.utils.Color

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
    var title by remember { mutableStateOf("") }
    var color by remember { mutableStateOf(Color.RED.id) }
    val group: Group? = viewModel.getGroupById(groupId)

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
                        order = group?.order
                    )
                },
                onDelete = {
                    viewModel.deleteGroup(groupId)
                },
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
}