package com.it6210.sportsnote.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.it6210.sportsnote.R
import com.it6210.sportsnote.model.TaskListData

/**
 * ダイアログタイプ
 */
enum class DialogType {
    None,
    Delete,
    CompleteTask,
    AddMeasure,
    AddTournamentNote,
    AddPracticeNote,
    Login,
    Tutorial,
    PasswordReset,
    CreateAccount,
    DeleteAccount,
    AddYearTarget,
    AddMonthTarget,
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
    title: String,
    message: String,
    confirmButtonText: String = "OK",
    onConfirm: () -> Unit,
    dismissButtonText: String = stringResource(R.string.cancel),
    showDialog: MutableState<Boolean>,
) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                },
            ) {
                Text(confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    showDialog.value = false
                },
            ) {
                Text(dismissButtonText)
            }
        },
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
    showDialog: MutableState<Boolean>,
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
                    placeholder = { Text("対策を入力してください") },
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(inputTextState.value)
                    showDialog.value = false
                },
            ) {
                Text(confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    showDialog.value = false
                },
            ) {
                Text(dismissButtonText)
            }
        },
    )
}

/**
 * 取り組んだ課題を追加するダイアログ
 *
 * @param tasks 取り組んだ課題データ
 * @param onTaskSelected 課題選択時の処理
 * @param onDismiss 閉じるボタンの処理
 */
@Composable
fun TaskSelectionDialog(
    tasks: List<TaskListData>,
    onTaskSelected: (TaskListData) -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.addDoneTask)) },
        text = {
            Column {
                if (tasks.isEmpty()) {
                    Text(stringResource(R.string.noAddTask))
                } else {
                    Column {
                        tasks.forEach { task ->
                            TextButton(
                                onClick = { onTaskSelected(task) },
                            ) {
                                Text(
                                    text = task.title,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.close))
            }
        },
    )
}
