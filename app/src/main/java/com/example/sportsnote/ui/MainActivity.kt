package com.example.sportsnote.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.sportsnote.model.RealmManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Realmの初期化
        RealmManager.initRealm(this)

        setContent {
            MainScreen()
        }
    }
}
