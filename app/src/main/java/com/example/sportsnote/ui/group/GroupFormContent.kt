package com.example.sportsnote.ui.group

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.sportsnote.R
import com.example.sportsnote.ui.components.ColorPickerField
import com.example.sportsnote.ui.components.CustomSpacerColumn
import com.example.sportsnote.ui.components.MultiLineTextInputField
import com.example.sportsnote.utils.Color

/**
 * Groupの共通フォーム
 *
 * @param initialTitle タイトル初期値
 * @param initialColor カラー初期値
 * @param onTitleChange タイトル変更時の処理
 * @param onColorChange カラー変更時の処理
 */
@Composable
fun GroupFormContent(
    initialTitle: String = "",
    initialColor: Int = Color.RED.id,
    onTitleChange: (String) -> Unit,
    onColorChange: (Int) -> Unit
) {
    val title = remember { mutableStateOf(initialTitle) }
    val color = remember { mutableStateOf(initialColor) }

    val inputFields: List<@Composable () -> Unit> = listOf(
        // タイトル
        {
            MultiLineTextInputField(
                title = stringResource(R.string.title),
                onTextChanged = { updatedText -> title.value = updatedText },
                initialText = title.value
            )
        },
        // カラー
        {
            ColorPickerField(
                onColorSelected = { selectedColor -> color.value = selectedColor },
                initialColor = color.value
            )
        }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // 入力フィールド
        CustomSpacerColumn(items = inputFields)

        // 変更されたtitleとcolorを返す
        onTitleChange(title.value)
        onColorChange(color.value)
    }
}