package com.example.sportsnote.ui

import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // アクションバーを無効化
        window.requestFeature(Window.FEATURE_NO_TITLE)
        actionBar?.hide()

        // アプリの初期化
        val initializationManager = InitializationManager(applicationContext)
        lifecycleScope.launch {
            initializationManager.initializeApp()
        }

        setContent {
            SportsNoteTheme{
                MainScreen()
            }
        }
    }
}
