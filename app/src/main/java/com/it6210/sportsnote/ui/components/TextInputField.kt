package com.it6210.sportsnote.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.times
import com.it6210.sportsnote.ui.components.items.ItemLabel

/**
 * 1行固定のテキスト入力欄
 *
 * @param title タイトル
 * @param placeholder プレースホルダー
 * @param onTextChanged テキスト変更時の処理
 * @param initialText 初期値
 */
@Composable
fun SingleLineTextInputField(
    title: String,
    placeholder: String = "$title を入力してください",
    onTextChanged: (String) -> Unit,
    initialText: String = "",
) {
    var text by remember { mutableStateOf(initialText) }

    Column(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
    ) {
        if (title.isNotBlank()) {
            ItemLabel(title = title)
        }
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                onTextChanged(text)
            },
            placeholder = { Text(placeholder) },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
            singleLine = true,
        )
    }
}

/**
 * 複数行のテキスト入力欄
 *
 * @param title タイトル
 * @param placeholder プレースホルダー
 * @param defaultLines デフォルトの行数
 * @param onTextChanged テキスト変更時の処理
 * @param initialText 初期値
 */
@Composable
fun MultiLineTextInputField(
    title: String,
    placeholder: String = "$title を入力してください",
    defaultLines: Int = 1,
    onTextChanged: (String) -> Unit,
    initialText: String = "",
) {
    var text by remember { mutableStateOf(initialText) }
    val textHeight = remember { mutableIntStateOf(0) }
    val lineHeight = 56.dp
    val minTextHeight = lineHeight * defaultLines

    Column(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
    ) {
        if (title.isNotBlank()) {
            ItemLabel(title = title)
        }
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                onTextChanged(text)
                // 入力されたテキストの行数を取得
                textHeight.intValue = it.split("\n").size
            },
            placeholder = { Text(placeholder) },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = lineHeight * defaultLines)
                    .padding(4.dp),
            singleLine = false,
        )
        // 動的に高さを更新する
        LaunchedEffect(textHeight.intValue) {
            val newHeight = (textHeight.intValue * lineHeight)
            Modifier.heightIn(min = max(newHeight, minTextHeight))
        }
    }
}
