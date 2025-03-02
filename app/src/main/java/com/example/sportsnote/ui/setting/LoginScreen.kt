package com.example.sportsnote.ui.setting

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.sportsnote.R
import com.example.sportsnote.model.PreferencesManager
import com.example.sportsnote.ui.components.CustomAlertDialog
import com.example.sportsnote.ui.components.DialogType
import com.example.sportsnote.ui.components.LoadingIndicator
import com.example.sportsnote.ui.setting.components.CustomButton
import com.example.sportsnote.ui.setting.components.CustomTextField
import kotlinx.coroutines.launch

/**
 * ログイン画面
 *
 * @param onDismiss 閉じる時の処理
 */
@Composable
fun LoginScreen(onDismiss: () -> Unit) {
    val viewModel = LoginViewModel()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // 入力データの状態管理
    val email = remember { mutableStateOf(PreferencesManager.get(PreferencesManager.Keys.ADDRESS, "")) }
    val password = remember { mutableStateOf(PreferencesManager.get(PreferencesManager.Keys.PASSWORD, "")) }
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val message by viewModel.message.collectAsState()
    val showDialog = remember { mutableStateOf(false) }
    var dialogType by remember { mutableStateOf(DialogType.None) }
    val isLoading = remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties =
            DialogProperties(
                usePlatformDefaultWidth = false,
            ),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.primary),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp),
            ) {
                // アプリアイコン表示
                Icon(
                    painter = painterResource(R.drawable.sportnoteicon),
                    contentDescription = "App Icon",
                    tint = Color.White,
                    modifier =
                        Modifier
                            .size(120.dp)
                            .align(Alignment.CenterHorizontally),
                )

                // アプリ名表示
                Text(
                    text = "SportsNote",
                    color = Color.White,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ログイン状態によるメッセージ表示
                Text(
                    text =
                        if (isLoggedIn) {
                            stringResource(R.string.loginMessage)
                        } else {
                            stringResource(R.string.notLoginMessage)
                        },
                    color = Color.White,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )

                Spacer(modifier = Modifier.height(16.dp))

                // メールアドレス入力欄
                CustomTextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    label = stringResource(R.string.mailAddress),
                )

                Spacer(modifier = Modifier.height(8.dp))

                // パスワード入力欄
                CustomTextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = stringResource(R.string.password),
                    visualTransformation = PasswordVisualTransformation(),
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ログイン・ログアウトボタン
                CustomButton(
                    text = if (isLoggedIn) stringResource(R.string.logout) else stringResource(R.string.login),
                    onClick = {
                        coroutineScope.launch {
                            isLoading.value = true
                            if (isLoggedIn) {
                                email.value = ""
                                password.value = ""
                                viewModel.logout(context)
                                isLoading.value = false
                                onDismiss()
                            } else {
                                viewModel.login(
                                    email = email.value,
                                    password = password.value,
                                    onSuccess = {
                                        isLoading.value = false
                                        onDismiss()
                                    },
                                    onFailure = {
                                        isLoading.value = false
                                    },
                                    context = context,
                                )
                            }
                        }
                    },
                    backgroundColor = if (isLoggedIn) Color.Red else MaterialTheme.colors.secondary,
                )

                Spacer(modifier = Modifier.height(8.dp))

                CustomButton(
                    text = stringResource(R.string.passwordReset),
                    onClick = {
                        dialogType = DialogType.PasswordReset
                        showDialog.value = true
                    },
                )

                Spacer(modifier = Modifier.height(8.dp))

                CustomButton(
                    text = stringResource(R.string.createAccount),
                    onClick = {
                        dialogType = DialogType.CreateAccount
                        showDialog.value = true
                    },
                )

                Spacer(modifier = Modifier.height(8.dp))

                CustomButton(
                    text = stringResource(R.string.deleteAccount),
                    onClick = {
                        dialogType = DialogType.DeleteAccount
                        showDialog.value = true
                    },
                )

                Spacer(modifier = Modifier.height(16.dp))

                // キャンセルボタン
                CustomButton(
                    text = stringResource(R.string.cancel),
                    onClick = onDismiss,
                    backgroundColor = Color.Gray,
                )
            }

            // メッセージ表示
            if (!message.isNullOrEmpty()) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                viewModel.resetMessage()
            }

            // ローディングインジケータ表示
            if (isLoading.value) {
                LoadingIndicator.Show()
            }

            // ダイアログ表示
            if (!showDialog.value) {
                return@Dialog
            }

            // パスワードリセット
            if (dialogType == DialogType.PasswordReset) {
                CustomAlertDialog(
                    title = stringResource(R.string.passwordReset),
                    message = stringResource(R.string.confirmSendPasswordResetMail),
                    onConfirm = {
                        coroutineScope.launch {
                            isLoading.value = true
                            viewModel.sendPasswordResetEmail(email.value, context)
                            dialogType = DialogType.None
                            showDialog.value = false
                            isLoading.value = false
                        }
                    },
                    showDialog = showDialog,
                )
            }

            // アカウント作成
            if (dialogType == DialogType.CreateAccount) {
                CustomAlertDialog(
                    title = stringResource(R.string.createAccount),
                    message = stringResource(R.string.createAccountMessage),
                    onConfirm = {
                        coroutineScope.launch {
                            isLoading.value = true
                            viewModel.createAccount(
                                email = email.value,
                                password = password.value,
                                onSuccess = {
                                    isLoading.value = false
                                    onDismiss()
                                },
                                onFailure = {
                                    isLoading.value = false
                                },
                                context = context,
                            )
                            dialogType = DialogType.None
                            showDialog.value = false
                        }
                    },
                    showDialog = showDialog,
                )
            }

            // アカウント削除
            if (dialogType == DialogType.DeleteAccount) {
                CustomAlertDialog(
                    title = stringResource(R.string.deleteAccount),
                    message = stringResource(R.string.deleteAccountMessage),
                    onConfirm = {
                        coroutineScope.launch {
                            isLoading.value = true
                            viewModel.deleteAccount(
                                onSuccess = {
                                    email.value = ""
                                    password.value = ""
                                    isLoading.value = false
                                },
                                onFailure = {
                                    isLoading.value = false
                                },
                                context = context,
                            )
                            dialogType = DialogType.None
                            showDialog.value = false
                        }
                    },
                    showDialog = showDialog,
                )
            }
        }
    }
}
