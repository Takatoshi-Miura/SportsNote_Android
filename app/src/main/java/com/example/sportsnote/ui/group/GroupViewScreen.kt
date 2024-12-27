package com.example.sportsnote.ui.group

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

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
    AddGroupContent(
        viewModel = viewModel,
        groupId = groupId,
        onDismiss = { onBack() },
        updateAppBar = { navigationIcon, rightIcon ->
            appBarNavigationIcon.value = navigationIcon
            appBarRightIcon.value = rightIcon
        },
        isDialog = false
    )
}