package com.example.sportsnote.ui.components.header

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

/**
 * Navigation遷移時のヘッダー
 *
 * @param onDismiss 前画面に戻る処理
 * @param onSave データ保存処理
 * @param onDelete データ削除処理
 * @param updateAppBar CustomTopAppBarの設定更新に必要
 */
@Composable
fun NavigationScreenHeader(
    onDismiss: () -> Unit,
    onSave: suspend () -> Unit,
    onDelete: suspend () -> Unit,
    updateAppBar: (@Composable (() -> Unit)?, @Composable (() -> Unit)?) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    // CustomTopAppBarの設定を更新
    SideEffect {
        updateAppBar(
            // 更新＆前画面に戻るボタン
            {
                IconButton(onClick = {
                    coroutineScope.launch {
                        onSave()
                        onDismiss()
                    }
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            // 削除ボタン
            {
                IconButton(onClick = {
                    coroutineScope.launch {
                        onDelete()
                    }
                }) {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
                }
            }
        )
    }
}