package com.example.sportsnote.ui.target

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@Composable
fun TargetScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
        val coroutineScope = rememberCoroutineScope()
        val BOTTOM_NAVIGATION_HEIGHT = 56.dp

        // リフレッシュ処理
        val onRefresh = {
        }

        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = onRefresh
        ) {
            // カレンダー
        }

        // +ボタン
        FloatingActionButton(
            onClick = {
                coroutineScope.launch {  }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = 16.dp,
                    bottom = 16.dp + BOTTOM_NAVIGATION_HEIGHT
                )
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add Target")
        }
    }
}
