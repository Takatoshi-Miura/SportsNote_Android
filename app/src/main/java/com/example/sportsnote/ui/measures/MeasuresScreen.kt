package com.example.sportsnote.ui.measures

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.sportsnote.R
import com.example.sportsnote.ui.components.CustomAlertDialog
import com.example.sportsnote.ui.components.CustomSpacerColumn
import com.example.sportsnote.ui.components.SingleLineTextInputField
import com.example.sportsnote.ui.components.header.NavigationScreenHeader
import com.example.sportsnote.ui.memo.components.RelatedMemoList
import kotlinx.coroutines.launch

/**
 * 対策画面
 *
 * @param measuresID 対策ID
 * @param onBack 前画面に戻る処理
 * @param appBarNavigationIcon TopBar左のボタンを変更するために必要
 * @param appBarRightIcon TopBar右のボタンを変更するために必要
 */
@Composable
fun MeasuresScreen(
    measuresID: String,
    onBack: () -> Unit,
    appBarNavigationIcon: MutableState<(@Composable () -> Unit)?>,
    appBarRightIcon: MutableState<(@Composable () -> Unit)?>,
) {
    val measuresViewModel = MeasuresViewModel()
    val measures = measuresViewModel.getMeasuresById(measuresID)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // 入力データ
    var title by remember { mutableStateOf(measures!!.title) }
    val showDialog = remember { mutableStateOf(false) }

    val inputFields: List<@Composable () -> Unit> =
        listOf(
            // タイトル
            {
                SingleLineTextInputField(
                    title = stringResource(R.string.title),
                    onTextChanged = { updatedText -> title = updatedText },
                    initialText = title,
                )
            },
            // ノート（メモ）
            {
                RelatedMemoList(measuresID = measuresID)
            },
        )

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
                    // 保存処理
                    measuresViewModel.saveMeasures(
                        measuresId = measuresID,
                        taskId = measures!!.taskID,
                        title = title,
                        created_at = measures.created_at,
                    )
                },
                onDelete = {
                    showDialog.value = true
                },
                onEdit = null,
                updateAppBar = { navigationIcon, rightIcon ->
                    appBarNavigationIcon.value = navigationIcon
                    appBarRightIcon.value = rightIcon
                },
            )

            // 共通フォーム
            CustomSpacerColumn(items = inputFields)
        }
    }

    if (showDialog.value) {
        // 削除確認ダイアログの表示
        CustomAlertDialog(
            title = stringResource(R.string.deleteMeasures),
            message = stringResource(R.string.deleteMeasuresMessage),
            confirmButtonText = stringResource(R.string.delete),
            onConfirm = {
                coroutineScope.launch {
                    // 削除処理
                    measuresViewModel.deleteMeasures(measuresID)
                    showDialog.value = false
                    onBack()
                }
            },
            showDialog = showDialog,
        )
    }
}
