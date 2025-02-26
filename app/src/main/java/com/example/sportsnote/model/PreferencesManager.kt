package com.example.sportsnote.model

import android.content.Context
import android.content.SharedPreferences
import java.util.UUID

/**
 * SharedPreferencesを一元管理するオブジェクト
 */
object PreferencesManager {
    private lateinit var sharedPreferences: SharedPreferences

    // SharedPreferencesのファイル名
    private const val PREF_NAME = "SportsNote_preferences"

    // 保存するためのキー一覧
    object Keys {
        const val FIRST_LAUNCH = "firstLaunch" // 初回起動判定
        const val USER_ID = "userID" // アカウント持ちならFirebaseID、なければ端末のUID
        const val ADDRESS = "address" // アカウントのメールアドレス
        const val PASSWORD = "password" // アカウントのパスワード
        const val IS_LOGIN = "isLogin" // ログイン状態
        const val AGREE = "agree" // 利用規約への同意状況
    }

    /**
     * ロード処理
     * 他のアプリからアクセスできないようにするため、Context.MODE_PRIVATEを使用
     *
     * @param context Context
     */
    fun load(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    private fun getEditor(): SharedPreferences.Editor = sharedPreferences.edit()

    /**
     * 保存処理
     *
     * @param key キー（PreferencesManager.Keys）
     * @param value 値
     */
    fun <T> set(
        key: String,
        value: T,
    ) {
        when (value) {
            is Int -> getEditor().putInt(key, value).apply()
            is Float -> getEditor().putFloat(key, value).apply()
            is Double -> getEditor().putString(key, value.toString()).apply()
            is Boolean -> getEditor().putBoolean(key, value).apply()
            is String -> getEditor().putString(key, value).apply()
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }

    /**
     * 取得処理
     *
     * @param key キー（PreferencesManager.Keys）
     * @param defaultValue デフォルト値
     * @return 保存された値
     */
    fun <T> get(
        key: String,
        defaultValue: T,
    ): T {
        return when (defaultValue) {
            is Int -> sharedPreferences.getInt(key, defaultValue) as T
            is Float -> sharedPreferences.getFloat(key, defaultValue) as T
            is Double -> sharedPreferences.getString(key, defaultValue.toString())?.toDouble() as T
            is Boolean -> sharedPreferences.getBoolean(key, defaultValue) as T
            is String -> sharedPreferences.getString(key, defaultValue) as T
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }

    /**
     * 削除処理
     *
     * @param key キー（PreferencesManager.Keys）
     */
    fun remove(key: String) {
        getEditor().remove(key).apply()
    }

    /**
     * SharedPreferencesの全データを削除する
     */
    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }

    /**
     * アカウント情報をリセット
     *
     * @param userID ユーザID
     */
    fun resetUserInfo(userID: String = UUID.randomUUID().toString()) {
        // ユーザIDを再生成
        set(Keys.USER_ID, userID)
    }
}
