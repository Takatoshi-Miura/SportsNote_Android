package com.example.sportsnote.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * 画面右下の＋ボタンのコンポーネント
 *
 * @param onClick クリック時の動作
 */
@Composable
fun CustomFloatingActionButton(onClick: () -> Unit) {
    val bottomNavigationHeight = 56.dp

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        FloatingActionButton(
            onClick = onClick,
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = Color.White,
            modifier = Modifier
                .padding(
                    end = 16.dp,
                    bottom = 16.dp + bottomNavigationHeight
                )
                .align(Alignment.BottomEnd)
        ) {
            Icon(Icons.Filled.Add, contentDescription = null)
        }
    }
}