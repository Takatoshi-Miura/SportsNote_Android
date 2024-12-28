package com.example.sportsnote.ui.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import com.example.sportsnote.R

/**
 * 汎用ダイアログ
 *
 * @param title ダイアログのタイトル
 * @param message ダイアログのメッセージ
 * @param confirmButtonText 確認ボタンのテキスト
 * @param onConfirm 確認ボタンの処理
 * @param dismissButtonText キャンセルボタンのテキスト
 * @param onDismiss キャンセルボタンの処理
 * @param showDialog ダイアログの表示状態
 */
@Composable
fun CustomAlertDialog(
    title: String ,
    message: String,
    confirmButtonText: String = "OK",
    onConfirm: () -> Unit,
    dismissButtonText: String = stringResource(R.string.cancel),
    showDialog: MutableState<Boolean>
) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                }
            ) {
                Text(confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    showDialog.value = false
                }
            ) {
                Text(dismissButtonText)
            }
        }
    )
}