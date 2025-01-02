package com.example.sportsnote.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import com.example.sportsnote.R

/**
 * ダイアログタイプ
 */
enum class DialogType {
    None,
    Delete,
    AddMeasure,
    AddTournamentNote,
    AddPracticeNote,
    Login
}

/**
 * 汎用ダイアログ
 *
 * @param title ダイアログのタイトル
 * @param message ダイアログのメッセージ
 * @param confirmButtonText 確認ボタンのテキスト
 * @param onConfirm 確認ボタンの処理
 * @param dismissButtonText キャンセルボタンのテキスト
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

/**
 * テキスト入力欄付き汎用ダイアログ
 *
 * @param title ダイアログのタイトル
 * @param message ダイアログのメッセージ
 * @param inputTextState 入力されたテキストの状態
 * @param confirmButtonText 確認ボタンのテキスト
 * @param onConfirm 確認ボタンの処理
 * @param dismissButtonText キャンセルボタンのテキスト
 * @param showDialog ダイアログの表示状態
 */
@Composable
fun TextInputDialog(
    title: String,
    message: String,
    inputTextState: MutableState<String>,
    confirmButtonText: String = "OK",
    onConfirm: (String) -> Unit,
    dismissButtonText: String = stringResource(R.string.cancel),
    showDialog: MutableState<Boolean>
) {
    AlertDialog(
        onDismissRequest = { showDialog.value = false },
        title = { Text(title) },
        text = {
            Column {
                Text(message)
                TextField(
                    value = inputTextState.value,
                    onValueChange = { inputTextState.value = it },
                    modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                    placeholder = { Text("対策を入力してください") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(inputTextState.value)
                    showDialog.value = false
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