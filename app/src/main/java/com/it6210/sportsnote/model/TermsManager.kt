package com.it6210.sportsnote.model

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import com.it6210.sportsnote.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 利用規約・プライバシーポリシーを管理するオブジェクト
 */
object TermsManager {
    var termsDialogShown = false

    object URL {
        const val TERMS_OF_SERVICE_AND_PRIVACY_POLICY = "https://sportnote-b2c92.firebaseapp.com/"
        const val TERMS_OF_SERVICE = "https://sportnote-b2c92.firebaseapp.com/"
        const val PRIVACY_POLICY = "https://sportnote-b2c92.firebaseapp.com/"
    }

    /**
     * 利用規約・プライバシーポリシー確認ダイアログを表示
     */
    suspend fun showTermsDialog(activity: ComponentActivity) {
        withContext(Dispatchers.Main) {
            if (activity.isFinishing || activity.isDestroyed) return@withContext
            termsDialogShown = true
            val builder = AlertDialog.Builder(activity)
            builder.setTitle(activity.getString(R.string.termsOfServiceTitle))
            builder.setMessage(activity.getString(R.string.termsOfServiceMessage))
            builder.setPositiveButton(activity.getString(R.string.agree)) { _, _ ->
                PreferencesManager.set(PreferencesManager.Keys.AGREE, true)
                termsDialogShown = false
            }
            builder.setNegativeButton(activity.getString(R.string.checkTermsOfService)) { _, _ ->
                navigateToUrl(URL.TERMS_OF_SERVICE_AND_PRIVACY_POLICY, activity)
            }
            builder.setCancelable(false)
            builder.setOnDismissListener {
                termsDialogShown = false
            }
            builder.show()
        }
    }

    /**
     * 利用規約ページに遷移
     *
     * @param context Context
     */
    fun navigateToTermsOfService(context: Context) {
        navigateToUrl(URL.TERMS_OF_SERVICE, context)
    }

    /**
     * プライバシーポリシーページに遷移
     *
     * @param context Context
     */
    fun navigateToPrivacyPolicy(context: Context) {
        navigateToUrl(URL.PRIVACY_POLICY, context)
    }

    /**
     * 指定したURLに遷移
     *
     * @param url URL
     * @param context Context
     */
    private fun navigateToUrl(
        url: String,
        context: Context,
    ) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }
}
