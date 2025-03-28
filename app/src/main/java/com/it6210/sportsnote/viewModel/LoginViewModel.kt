package com.it6210.sportsnote.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.it6210.sportsnote.R
import com.it6210.sportsnote.model.manager.InitializationManager
import com.it6210.sportsnote.model.manager.PreferencesManager
import com.it6210.sportsnote.model.manager.RealmManager
import com.it6210.sportsnote.model.manager.SyncManager
import com.it6210.sportsnote.utils.Network
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
     * @param onFailure ログイン失敗時の処理
     * @param context Context
     */
    suspend fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit,
        context: Context,
    ) {
        if (email.isBlank() || password.isBlank()) {
            _message.value = context.getString(R.string.emptyTextError)
            onFailure()
            return
        }
        if (!Network.isOnline()) {
            _message.value = context.getString(R.string.internetError)
            onFailure()
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
                PreferencesManager.set(PreferencesManager.Keys.IS_LOGIN, true)
                initializationManager.initializeApp(isLogin = true)

                // データ同期
                SyncManager.syncAllData()

                // TOPに戻る
                _isLoggedIn.value = true
                _message.value = context.getString(R.string.loginSuccess)
                delay(2000)
                onSuccess()
            } catch (e: Exception) {
                _message.value =
                    when (e) {
                        is FirebaseAuthInvalidCredentialsException -> context.getString(R.string.invalidCredentialsError)
                        is FirebaseAuthInvalidUserException -> context.getString(R.string.invalidUserError)
                        is FirebaseNetworkException -> context.getString(R.string.networkError)
                        else -> e.message ?: context.getString(R.string.loginError)
                    }
                onFailure()
            }
        }
    }

    /**
     * ログアウト処理
     *
     * @param context Context
     */
    suspend fun logout(context: Context) {
        if (!Network.isOnline()) {
            _message.value = context.getString(R.string.internetError)
            return
        }

        auth.signOut()

        // データを全削除＆初期化
        val initializationManager = InitializationManager(context)
        initializationManager.deleteAllData()
        initializationManager.initializeApp()

        _isLoggedIn.value = false
        _message.value = context.getString(R.string.logoutSuccess)
        delay(2000)
    }

    /**
     * アカウント作成
     *
     * @param email メールアドレス
     * @param password パスワード
     * @param onSuccess アカウント作成成功時の処理
     * @param onFailure アカウント作成失敗時の処理
     * @param context Context
     */
    suspend fun createAccount(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit,
        context: Context,
    ) {
        if (email.isBlank() || password.isBlank()) {
            _message.value = context.getString(R.string.emptyTextError)
            onFailure()
            return
        }
        if (!Network.isOnline()) {
            _message.value = context.getString(R.string.internetError)
            onFailure()
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
                    PreferencesManager.set(PreferencesManager.Keys.IS_LOGIN, true)

                    // RealmデータのuserIDを新しいIDに更新
                    val realmManager = RealmManager.getInstance()
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
                    onFailure()
                }
            } catch (e: Exception) {
                _message.value =
                    when (e) {
                        is FirebaseAuthUserCollisionException -> context.getString(R.string.userCollisionError)
                        is FirebaseAuthWeakPasswordException -> context.getString(R.string.weakPasswordError)
                        is FirebaseAuthInvalidCredentialsException -> context.getString(R.string.invalidCredentialsError)
                        else -> e.message ?: context.getString(R.string.createAccountFailed)
                    }
                onFailure()
            }
        }
    }

    /**
     * アカウント削除
     *
     * @param onSuccess アカウント削除成功時の処理
     * @param onFailure アカウント削除失敗時の処理
     * @param context Context
     */
    suspend fun deleteAccount(
        onSuccess: () -> Unit,
        onFailure: () -> Unit,
        context: Context,
    ) {
        if (!Network.isOnline()) {
            _message.value = context.getString(R.string.internetError)
            onFailure()
            return
        }

        if (!_isLoggedIn.value) {
            _message.value = context.getString(R.string.pleaseLogin)
            onFailure()
            return
        }

        val user =
            auth.currentUser ?: run {
                _message.value = context.getString(R.string.pleaseLogin)
                onFailure()
                return
            }

        user.delete().addOnCompleteListener { task ->
            // 失敗
            if (!task.isSuccessful) {
                val exception = task.exception
                _message.value =
                    when (exception) {
                        is FirebaseAuthRecentLoginRequiredException -> context.getString(R.string.recentLoginRequiredError)
                        is FirebaseAuthInvalidCredentialsException -> context.getString(R.string.invalidCredentialsError)
                        else -> exception?.message ?: context.getString(R.string.deleteAccountFailed)
                    }
                onFailure()
                return@addOnCompleteListener
            }
            viewModelScope.launch {
                // データを全削除＆初期化
                val initializationManager = InitializationManager(context)
                initializationManager.deleteAllData()
                initializationManager.initializeApp()
                onSuccess()

                _isLoggedIn.value = false
                _message.value = context.getString(R.string.deleteAccountSuccess)
            }
        }
    }

    /**
     * パスワードリセットメールを送信
     *
     * @param email メールアドレス
     */
    suspend fun sendPasswordResetEmail(
        email: String,
        context: Context,
    ) {
        if (email.isBlank()) {
            _message.value = context.getString(R.string.emptyTextErrorPasswordReset)
            return
        }
        if (!Network.isOnline()) {
            _message.value = context.getString(R.string.internetError)
            return
        }

        viewModelScope.launch {
            try {
                auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _message.value = context.getString(R.string.sendMail)
                    } else {
                        val exception = task.exception as? FirebaseAuthException
                        _message.value =
                            when (exception?.errorCode) {
                                "invalid-email" -> context.getString(R.string.invalidCredentialsError)
                                "user-not-found" -> context.getString(R.string.invalidUserError)
                                "network-request-failed" -> context.getString(R.string.networkError)
                                else -> exception?.message ?: context.getString(R.string.sendMailFailed)
                            }
                    }
                }
            } catch (e: Exception) {
                _message.value = e.message
            }
        }
    }
}
