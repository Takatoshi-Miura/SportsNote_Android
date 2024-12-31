package com.example.sportsnote.ui.measures

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.sportsnote.R
import com.example.sportsnote.ui.components.CustomAlertDialog
import com.example.sportsnote.ui.components.CustomSpacerColumn
import com.example.sportsnote.ui.components.MultiLineTextInputField
import com.example.sportsnote.ui.components.header.NavigationScreenHeader
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
    appBarRightIcon: MutableState<(@Composable () -> Unit)?>
) {
    val measuresViewModel = MeasuresViewModel()
    val measures = measuresViewModel.getMeasuresById(measuresID)
    val coroutineScope = rememberCoroutineScope()

    // 入力データ
    var title by remember { mutableStateOf(measures!!.title) }
    val showDialog = remember { mutableStateOf(false) }

    val inputFields: List<@Composable () -> Unit> = listOf(
        // タイトル
        {
            MultiLineTextInputField(
                title = stringResource(R.string.title),
                onTextChanged = { updatedText -> title = updatedText },
                initialText = title
            )
        },
        // ノート（メモ）
        {
            Text(
                text = stringResource(R.string.note),
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(8.dp)
            )
            // TODO: 関連するメモを取得
        }
    )

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
                    // 保存処理
                    measuresViewModel.saveMeasures(
                        measuresId = measuresID,
                        taskId = measures!!.taskID,
                        title = title,
                    )
                },
                onDelete = {
                    showDialog.value = true
                },
                updateAppBar = { navigationIcon, rightIcon ->
                    appBarNavigationIcon.value = navigationIcon
                    appBarRightIcon.value = rightIcon
                }
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
            showDialog = showDialog
        )
    }
}