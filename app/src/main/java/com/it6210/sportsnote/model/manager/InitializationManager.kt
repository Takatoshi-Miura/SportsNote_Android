package com.it6210.sportsnote.model.manager

import android.content.Context
import com.it6210.sportsnote.ui.group.GroupViewModel
import com.it6210.sportsnote.ui.note.NoteViewModel

class InitializationManager(
    private val context: Context,
) {
    /**
     * アプリ全体の初期化を実行
     *
     * @param isLogin ログイン済みかどうか
     */
    suspend fun initializeApp(isLogin: Boolean = false) {
        initializePreferences()
        initializeRealm()

        val isFirstLaunch = PreferencesManager.get(PreferencesManager.Keys.FIRST_LAUNCH, true)
        if (isFirstLaunch) {
            PreferencesManager.clearAll()
            PreferencesManager.resetUserInfo()
            PreferencesManager.set(PreferencesManager.Keys.FIRST_LAUNCH, false)
        }

        if (!isLogin) {
            // 初期データを作成
            createFreeNote()
            createUncategorizedGroup()
        }
    }

    /**
     * SharedPreferences を初期化
     */
    private fun initializePreferences() {
        PreferencesManager.load(context)
    }

    /**
     * Realm を初期化
     */
    private fun initializeRealm() {
        RealmManager.initRealm(context)
    }

    /**
     * フリーノートを作成
     */
    private suspend fun createFreeNote() {
        val noteViewModel = NoteViewModel()
        noteViewModel.createFreeNote(context)
    }

    /**
     * 未分類グループを作成
     */
    private suspend fun createUncategorizedGroup() {
        val groupViewModel = GroupViewModel()
        groupViewModel.createUncategorizedGroup(context)
    }

    /**
     * データを全削除
     */
    suspend fun deleteAllData() {
        RealmManager.clearAll()
        PreferencesManager.clearAll()
    }
}
