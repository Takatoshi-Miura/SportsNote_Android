package com.it6210.sportsnote.ui.setting.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

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
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor),
    ) {
        Text(text = text, color = textColor)
    }
}
