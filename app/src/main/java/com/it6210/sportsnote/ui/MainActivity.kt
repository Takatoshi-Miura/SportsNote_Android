package com.it6210.sportsnote.ui

import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.MobileAds
import com.it6210.sportsnote.model.manager.InitializationManager
import com.it6210.sportsnote.model.manager.PreferencesManager
import com.it6210.sportsnote.model.manager.TermsManager
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // アクションバーを無効化
        window.requestFeature(Window.FEATURE_NO_TITLE)
        actionBar?.hide()

        // EdgeToEdgeを有効化
        enableEdgeToEdge()

        // アプリの初期化
        val initializationManager = InitializationManager(applicationContext)
        lifecycleScope.launch {
            initializationManager.initializeApp()
        }

        // AdMobを初期化
        MobileAds.initialize(this@MainActivity) {}

        // 利用規約ダイアログを表示
        checkAndShowTermsDialog()

        setContent {
            SportsNoteTheme {
                MainScreen()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkAndShowTermsDialog()
    }

    /**
     * 利用規約に同意状況確認＆利用規約ダイアログ表示
     */
    private fun checkAndShowTermsDialog() {
        val agreeStatus = PreferencesManager.get(PreferencesManager.Keys.AGREE, false)
        if (!agreeStatus && !TermsManager.termsDialogShown) {
            lifecycleScope.launch {
                TermsManager.showTermsDialog(this@MainActivity)
                TermsManager.termsDialogShown = true
            }
        }
    }
}
