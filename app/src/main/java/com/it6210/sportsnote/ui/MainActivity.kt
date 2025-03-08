package com.it6210.sportsnote.ui

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.MobileAds
import com.it6210.sportsnote.R
import com.it6210.sportsnote.model.PreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    private var termsDialogShown = false

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
        if (!agreeStatus && !termsDialogShown) {
            lifecycleScope.launch {
                showTermsOfServiceDialog()
            }
        }
    }

    /**
     * 利用規約ダイアログを表示
     */
    private suspend fun showTermsOfServiceDialog() {
        withContext(Dispatchers.Main) {
            if (isFinishing || isDestroyed) return@withContext
            termsDialogShown = true
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle(getString(R.string.termsOfServiceTitle))
            builder.setMessage(getString(R.string.termsOfServiceMessage))
            builder.setPositiveButton(getString(R.string.agree)) { _, _ ->
                PreferencesManager.set(PreferencesManager.Keys.AGREE, true)
                termsDialogShown = false
            }
            builder.setNegativeButton(getString(R.string.checkTermsOfService)) { _, _ ->
                openTermsOfServiceLink()
            }
            builder.setCancelable(false)
            builder.setOnDismissListener {
                termsDialogShown = false
                recreate()
            }
            builder.show()
        }
    }

    /**
     * 利用規約ページに遷移
     */
    private fun openTermsOfServiceLink() {
        val intent =
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://sportnote-b2c92.firebaseapp.com/"),
            )
        startActivity(intent)
    }
}
