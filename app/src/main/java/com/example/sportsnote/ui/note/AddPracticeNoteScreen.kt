package com.example.sportsnote.ui.note

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.sportsnote.R
import com.example.sportsnote.ui.components.header.AddScreenHeader
import com.example.sportsnote.utils.Weather
import java.util.Date

/**
 * 練習ノート追加画面
 *
 * @param onDismiss 閉じる時の処理
 */
@Composable
fun AddPracticeNoteScreen(
    onDismiss: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val noteViewModel = NoteViewModel()

    // 入力データの状態管理
    var date = remember { mutableStateOf(Date()) }
    var weather = remember { mutableStateOf(Weather.SUNNY.id) }
    var temperature = remember { mutableStateOf(20) }
    var condition = remember { mutableStateOf("") }
    var purpose = remember { mutableStateOf("") }
    var detail = remember { mutableStateOf("") }
    var reflection = remember { mutableStateOf("") }

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
                // ヘッダー
                AddScreenHeader(
                    title = stringResource(R.string.addPracticeNote),
                    onCancel = onDismiss,
                    onSave = {
                        // 保存処理

                        onDismiss()
                    },
                    coroutineScope = coroutineScope
                )

                // 共通フォーム

            }
        }
    }
}