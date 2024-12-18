package com.example.sportsnote.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.times

/**
 * 複数行のテキスト入力欄
 *
 * @param title タイトル
 * @param placeholder プレースホルダー
 * @param defaultLines デフォルトの行数
 * @param onTextChanged テキスト変更時の処理
 */
@Composable
fun MultiLineTextInputField(
    title: String,
    placeholder: String = "$title を入力してください",
    defaultLines: Int = 1,
    onTextChanged: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    val textHeight = remember { mutableStateOf(0) } // テキストの高さを動的に管理
    val lineHeight = 56.dp
    val minTextHeight = lineHeight * defaultLines

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        androidx.compose.material.OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                onTextChanged(text)
                // 入力されたテキストの行数を取得
                textHeight.value = it.split("\n").size
            },
            placeholder = { Text(placeholder) },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = lineHeight * defaultLines)
                .padding(4.dp),
            singleLine = false
        )
        // 動的に高さを更新する
        LaunchedEffect(textHeight.value) {
            val newHeight = (textHeight.value * lineHeight)
            Modifier.heightIn(min = max(newHeight, minTextHeight))
        }
    }
}