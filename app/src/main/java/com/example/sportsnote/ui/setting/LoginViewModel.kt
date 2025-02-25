package com.example.sportsnote.ui.setting

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportsnote.R
import com.example.sportsnote.model.PreferencesManager
import com.example.sportsnote.model.RealmManager
import com.example.sportsnote.model.SyncManager
import com.example.sportsnote.ui.InitializationManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * Firebase Authentication を使用したログイン用 ViewModel
 */
class LoginViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // ユーザーのログイン状態
    private val _isLoggedIn = MutableStateFlow(auth.currentUser != null)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    // エラー/成功メッセージ
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    /**
     * メッセージをリセット
     */
    fun resetMessage() {
        _message.value = null
    }

    /**
     * ログイン処理
     *
     * @param email メールアドレス
     * @param password パスワード
     * @param onSuccess ログイン成功時の処理
     * @param context Context
     */
    suspend fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        context: Context,
    ) {
        if (email.isBlank() || password.isBlank()) {
            _message.value = context.getString(R.string.emptyTextError)
            return
        }
        viewModelScope.launch {
            try {
                // ログイン処理が完了するのを待機
                auth.signInWithEmailAndPassword(email, password).await()

                // データを全削除＆ログインユーザ情報に初期設定
                val initializationManager = InitializationManager(context)
                initializationManager.deleteAllData()
                PreferencesManager.set(PreferencesManager.Keys.FIRST_LAUNCH, false)
                PreferencesManager.set(PreferencesManager.Keys.USER_ID, auth.currentUser?.uid)
                PreferencesManager.set(PreferencesManager.Keys.ADDRESS, email)
                PreferencesManager.set(PreferencesManager.Keys.PASSWORD, password)
                initializationManager.initializeApp(isLogin = true)

                // データ同期
                SyncManager.syncAllData()

                // TOPに戻る
                _isLoggedIn.value = true
                _message.value = context.getString(R.string.loginSuccess)
                delay(2000)
                onSuccess()
            } catch (e: Exception) {
                _message.value = e.message ?: context.getString(R.string.loginError)
            }
        }
    }

    /**
     * ログアウト処理
     *
     * @param context Context
     */
    suspend fun logout(context: Context) {
        auth.signOut()
        _isLoggedIn.value = false
        _message.value = context.getString(R.string.logoutSuccess)

        // データを全削除＆初期化
        val initializationManager = InitializationManager(context)
        initializationManager.deleteAllData()
        initializationManager.initializeApp()
    }

    /**
     * アカウント作成
     *
     * @param email メールアドレス
     * @param password パスワード
     * @param onSuccess アカウント作成成功時の処理
     * @param context Context
     */
    fun createAccount(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        context: Context,
    ) {
        if (email.isBlank() || password.isBlank()) {
            _message.value = context.getString(R.string.emptyTextError)
            return
        }
        viewModelScope.launch {
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                if (result.user != null) {
                    // ログイン情報を保存
                    PreferencesManager.set(PreferencesManager.Keys.USER_ID, result.user?.uid)
                    PreferencesManager.set(PreferencesManager.Keys.ADDRESS, email)
                    PreferencesManager.set(PreferencesManager.Keys.PASSWORD, password)

                    // RealmデータのuserIDを新しいIDに更新
                    val realmManager = RealmManager()
                    realmManager.updateAllUserIds(userId = result.user!!.uid)

                    // データ同期
                    SyncManager.syncAllData()

                    // TOPに戻る
                    _isLoggedIn.value = true
                    _message.value = context.getString(R.string.createAccountSuccess)
                    delay(2000)
                    onSuccess()
                } else {
                    _message.value = context.getString(R.string.createAccountFailed)
                }
            } catch (e: Exception) {
                _message.value = e.message
            }
        }
    }

    /**
     * アカウント削除
     */
    fun deleteAccount(context: Context) {
        if (!_isLoggedIn.value) {
            _message.value = context.getString(R.string.pleaseLogin)
            return
        }
        val user = auth.currentUser
        if (user != null) {
            user.delete().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _isLoggedIn.value = false
                    _message.value = context.getString(R.string.deleteAccountSuccess)
                    PreferencesManager.remove(PreferencesManager.Keys.ADDRESS)
                    PreferencesManager.remove(PreferencesManager.Keys.PASSWORD)
                } else {
                    _message.value = task.exception?.message ?: context.getString(R.string.deleteAccountFailed)
                }
            }
        } else {
            _message.value = context.getString(R.string.pleaseLogin)
        }
    }

    /**
     * パスワードリセットメールを送信
     *
     * @param email メールアドレス
     */
    fun sendPasswordResetEmail(
        email: String,
        context: Context,
    ) {
        if (email.isBlank()) {
            _message.value = context.getString(R.string.emptyTextErrorPasswordReset)
            return
        }

        viewModelScope.launch {
            try {
                auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _message.value = context.getString(R.string.sendMail)
                    } else {
                        _message.value = task.exception?.message ?: context.getString(R.string.sendMailFailed)
                    }
                }
            } catch (e: Exception) {
                _message.value = e.message
            }
        }
    }
}
