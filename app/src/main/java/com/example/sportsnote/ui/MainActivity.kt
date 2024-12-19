package com.example.sportsnote.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.sportsnote.model.PreferencesManager
import com.example.sportsnote.model.RealmManager
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // SharedPreferencesの初期化
        PreferencesManager.init(applicationContext)

        // Realmの初期化
        RealmManager.initRealm(this)

        // 初回起動の場合はユーザIDを作成
        val isFirstLaunch = PreferencesManager.get(PreferencesManager.Keys.FIRST_LAUNCH, true)
        if (isFirstLaunch) {
            val uuid = UUID.randomUUID().toString()
            PreferencesManager.set(PreferencesManager.Keys.USER_ID, uuid)
            PreferencesManager.set(PreferencesManager.Keys.FIRST_LAUNCH, false)
        }

        setContent {
            MainScreen()
        }
    }
}
