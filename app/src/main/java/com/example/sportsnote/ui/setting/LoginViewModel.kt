package com.example.sportsnote.ui.setting

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportsnote.R
import com.example.sportsnote.model.PreferencesManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
     */
    fun login(email: String, password: String, context: Context) {
        if (email.isBlank() || password.isBlank()) {
            _message.value = context.getString(R.string.emptyTextError)
            return
        }
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _isLoggedIn.value = true
                        _message.value = context.getString(R.string.loginSuccess)
                        PreferencesManager.set(key = PreferencesManager.Keys.ADDRESS, value = email)
                        PreferencesManager.set(key = PreferencesManager.Keys.PASSWORD, value = password)
                    } else {
                        _message.value = task.exception?.message ?: context.getString(R.string.loginError)
                    }
                }
            } catch (e: Exception) {
                _message.value = e.message
            }
        }
    }

    /**
     * ログアウト処理
     */
    fun logout(context: Context) {
        auth.signOut()
        _isLoggedIn.value = false
        _message.value = context.getString(R.string.logoutSuccess)
        PreferencesManager.remove(PreferencesManager.Keys.ADDRESS)
        PreferencesManager.remove(PreferencesManager.Keys.PASSWORD)
    }

    /**
     * アカウント作成
     *
     * @param email メールアドレス
     * @param password パスワード
     */
    fun createAccount(email: String, password: String) {
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _isLoggedIn.value = true
                        _message.value = "アカウント作成に成功しました"
                    } else {
                        _message.value = task.exception?.message ?: "アカウント作成に失敗しました"
                    }
                }
            } catch (e: Exception) {
                _message.value = e.message
            }
        }
    }

    /**
     * アカウント削除
     */
    fun deleteAccount() {
        val user = auth.currentUser
        if (user != null) {
            user.delete().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _isLoggedIn.value = false
                    _message.value = "アカウントが削除されました"
                } else {
                    _message.value = task.exception?.message ?: "アカウント削除に失敗しました"
                }
            }
        } else {
            _message.value = "ログインしていません"
        }
    }

    /**
     * パスワード変更
     *
     * @param newPassword 新しいパスワード
     */
    fun changePassword(newPassword: String) {
        val user = auth.currentUser
        if (user != null) {
            user.updatePassword(newPassword).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _message.value = "パスワードが変更されました"
                } else {
                    _message.value = task.exception?.message ?: "パスワード変更に失敗しました"
                }
            }
        } else {
            _message.value = "ログインしていません"
        }
    }
}