package com.example.sportsnote.ui.components

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.example.sportsnote.ui.setting.SettingScreen
import kotlinx.coroutines.launch

/**
 * ハンバーガーメニューを作成
 *
 * @param scaffoldState ScaffoldState
 */
@Composable
fun DrawerContent(scaffoldState: ScaffoldState) {
    val coroutineScope = rememberCoroutineScope()

    // 設定画面を描画
    SettingScreen(
        onDismiss = {
            coroutineScope.launch {
                scaffoldState.drawerState.close()
            }
        },
    )
}
