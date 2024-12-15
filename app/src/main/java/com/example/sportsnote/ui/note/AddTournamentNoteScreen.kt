package com.example.sportsnote.ui.note

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

/**
 * 大会ノート追加画面
 */
@Composable
fun AddTournamentNoteScreen(onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false // デフォルトの幅制限を無効化
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .background(MaterialTheme.colors.surface)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // ヘッダー
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(MaterialTheme.colors.primary)
                ) {
                    Text(
                        text = "大会ノート追加",
                        color = MaterialTheme.colors.onPrimary,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 16.dp)
                    )
                }

                // コンテンツ
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(text = "大会ノートの詳細を入力してください")
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            // ノート保存などの処理
                            onDismiss()
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("保存")
                    }
                }
            }
        }
    }
}