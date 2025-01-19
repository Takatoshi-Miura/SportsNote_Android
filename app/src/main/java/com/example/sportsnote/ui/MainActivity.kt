package com.example.sportsnote.ui

import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.sportsnote.model.PreferencesManager
import com.example.sportsnote.model.RealmManager
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // アクションバーを無効化
        window.requestFeature(Window.FEATURE_NO_TITLE)
        actionBar?.hide()

        // アプリの初期化
        val initializationManager = InitializationManager(applicationContext)
        initializationManager.initializeApp()

        setContent {
            SportsNoteTheme{
                MainScreen()
            }
        }
    }
}
