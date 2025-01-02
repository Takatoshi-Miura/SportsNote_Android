package com.example.sportsnote.ui.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
                .background(MaterialTheme.colors.surface)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // フォーム

            }
        }
    }
}