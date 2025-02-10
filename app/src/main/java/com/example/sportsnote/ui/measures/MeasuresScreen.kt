package com.example.sportsnote.ui.measures

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.sportsnote.R
import com.example.sportsnote.model.MeasuresMemo
import com.example.sportsnote.ui.LocalNavController
import com.example.sportsnote.ui.components.CustomAlertDialog
import com.example.sportsnote.ui.components.CustomSpacerColumn
import com.example.sportsnote.ui.components.MultiLineTextInputField
import com.example.sportsnote.ui.components.header.NavigationScreenHeader
import com.example.sportsnote.ui.memo.MemoViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

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
    val memoViewModel = MemoViewModel()
    val memos = memoViewModel.getMemosByMeasuresID(measuresID)
    val coroutineScope = rememberCoroutineScope()
    val navController = LocalNavController.current

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
            // 関連するメモを表示
            LazyColumn {
                items(memos.size) { index ->
                    val memo = memos[index]
                    MemoItem(
                        memo = memo,
                        onClick = {
                            navController.navigate("practice_note_view/${memo.noteID}")
                        }
                    )
                }
            }
            Divider()
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
                onEdit = null,
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

/**
 * 対策に関連するメモを表示
 *
 * @param memo MeasuresMemo
 * @param onClick メモをタップしたときの処理
 */
@Composable
fun MemoItem(
    memo: MeasuresMemo,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Divider()
        Text(
            text = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(memo.date),
            style = MaterialTheme.typography.body2,
            color = Color.Gray
        )
        Text(
            text = memo.detail,
            style = MaterialTheme.typography.body1
        )
    }
}