package com.example.sportsnote.ui.setting

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.sportsnote.R
import kotlinx.coroutines.CoroutineScope

/**
 * ログイン画面
 *
 * @param onDismiss 閉じる時の処理
 */
@Composable
fun LoginScreen(
    onDismiss: () -> Unit,
) {
//    val viewModel:
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    // 入力データの状態管理
    var loginId = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.primary)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // アプリアイコン表示
                Icon(
                    painter = painterResource(R.drawable.sportnoteicon),
                    contentDescription = "App Icon",
                    tint = Color.White,
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.CenterHorizontally)
                )

                // アプリ名表示
                Text(
                    text = "SportsNote",
                    color = Color.White,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ログイン状態によるメッセージ表示
                Text(
                    text = stringResource(R.string.notLoginMessage),
                    color = Color.White,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // メールアドレス入力欄
                CustomTextField(
                    value = loginId.value,
                    onValueChange = { loginId.value = it },
                    label = stringResource(R.string.mailAddress)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // パスワード入力欄
                CustomTextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = stringResource(R.string.password),
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomButton(
                    text = stringResource(R.string.login),
                    onClick = { /* TODO: ログイン処理 */ }
                )

                Spacer(modifier = Modifier.height(8.dp))

                CustomButton(
                    text = stringResource(R.string.passwordReset),
                    onClick = { /* TODO: パスワードリセット処理 */ }
                )

                Spacer(modifier = Modifier.height(8.dp))

                CustomButton(
                    text = stringResource(R.string.createAccount),
                    onClick = { /* TODO: アカウント作成処理 */ }
                )

                Spacer(modifier = Modifier.height(8.dp))

                CustomButton(
                    text = stringResource(R.string.deleteAccount),
                    onClick = { /* TODO: アカウント削除処理 */ }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // キャンセルボタン
                CustomButton(
                    text = stringResource(R.string.cancel),
                    onClick = onDismiss,
                    backgroundColor = Color.Gray
                )
            }
        }
    }
}

/**
 * TextFieldの共通コンポーネント
 *
 * @param value 入力文字列
 * @param onValueChange 文字列変更時の処理
 * @param label 項目名
 * @param visualTransformation パスワードの伏字など
 */
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label, color = Color.Gray) },
        singleLine = true,
        visualTransformation = visualTransformation,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Color.Black,
            cursorColor = Color.Black,
            focusedBorderColor = Color.Gray,
            unfocusedBorderColor = Color.LightGray,
            focusedLabelColor = Color.Gray,
            unfocusedLabelColor = Color.Gray
        )
    )
}

/**
 * ボタンの共通コンポーネント
 *
 * @param text ボタン名
 * @param onClick 押下時の処理
 * @param backgroundColor 背景色
 * @param textColor テキスト色
 * @param modifier Modifier
 */
@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    backgroundColor: Color = MaterialTheme.colors.secondary,
    textColor: Color = Color.White,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor)
    ) {
        Text(text = text, color = textColor)
    }
}