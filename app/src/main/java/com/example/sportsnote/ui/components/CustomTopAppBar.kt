package com.example.sportsnote.ui.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable

/**
 * TopBarのUI
 *
 * @param title タイトル
 * @param navigationIcon 左側のボタン
 * @param rightIcon 右側のボタン
 */
@Composable
fun CustomTopAppBar(
    title: String,
    navigationIcon: @Composable (() -> Unit)? = null,
    rightIcon: @Composable (() -> Unit)? = null,
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = navigationIcon,
        actions = {
            rightIcon?.invoke()
        },
        backgroundColor = MaterialTheme.colors.primary,
    )
}
