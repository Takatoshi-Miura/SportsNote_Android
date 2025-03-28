package com.it6210.sportsnote.ui.group

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.it6210.sportsnote.R
import com.it6210.sportsnote.model.Group
import com.it6210.sportsnote.ui.components.CustomAlertDialog
import com.it6210.sportsnote.ui.components.header.NavigationScreenHeader
import com.it6210.sportsnote.ui.components.items.AutoSaveTimestamp
import com.it6210.sportsnote.ui.group.components.GroupFormContent
import com.it6210.sportsnote.utils.Color
import com.it6210.sportsnote.viewModel.GroupViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date

/**
 * Group詳細画面
 *
 * @param groupId groupId
 * @param onBack 前画面に戻る処理
 * @param appBarNavigationIcon TopBar左のボタンを変更するために必要
 * @param appBarRightIcon TopBar右のボタンを変更するために必要
 */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GroupViewScreen(
    groupId: String,
    onBack: () -> Unit,
    appBarNavigationIcon: MutableState<(@Composable () -> Unit)?>,
    appBarRightIcon: MutableState<(@Composable () -> Unit)?>,
) {
    val viewModel = GroupViewModel()
    val group: Group? = viewModel.getGroupById(groupId)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // 入力データの状態管理
    var title by remember { mutableStateOf(group?.title ?: "") }
    var color by remember { mutableIntStateOf(group?.color ?: Color.RED.id) }
    val groupsState = remember { mutableStateOf<List<Group>>(emptyList()) }
    var lastSavedAt by remember { mutableStateOf(group?.updated_at ?: Date()) }
    val showDialog = remember { mutableStateOf(false) }

    // 自動保存処理
    LaunchedEffect(title, color, groupsState.value) {
        delay(1000)
        if (title.isNotEmpty()) {
            val now = Date()
            viewModel.saveGroup(
                groupId = groupId,
                title = title,
                colorId = color,
                order = group?.order,
                created_at = group?.created_at ?: now,
            )
            lastSavedAt = now
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
                    viewModel.saveGroup(
                        groupId = groupId,
                        title = title,
                        colorId = color,
                        order = group?.order,
                        created_at = group?.created_at ?: Date(),
                    )
                },
                onDelete = {
                    showDialog.value = true
                },
                onEdit = null,
                updateAppBar = { navigationIcon, rightIcon ->
                    appBarNavigationIcon.value = navigationIcon
                    // グループが2つ以上ある場合のみ削除ボタンを表示
                    if (viewModel.getGroupCount() > 1) {
                        appBarRightIcon.value = rightIcon
                    }
                },
            )

            AutoSaveTimestamp(lastSavedAt)

            // 共通フォーム
            GroupFormContent(
                group = group,
                onTitleChange = { title = it },
                onColorChange = { color = it },
                onGroupsChange = { updatedGroups -> groupsState.value = updatedGroups },
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
            showDialog = showDialog,
        )
    }
}
