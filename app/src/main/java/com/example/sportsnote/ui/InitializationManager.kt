package com.example.sportsnote.ui

import android.content.Context
import com.example.sportsnote.R
import com.example.sportsnote.model.PreferencesManager
import com.example.sportsnote.model.RealmManager
import com.example.sportsnote.ui.note.NoteViewModel
import java.util.UUID

class InitializationManager(
    private val context: Context,
) {
    /**
     * アプリ全体の初期化を実行
     */
    suspend fun initializeApp() {
        initializePreferences()
        initializeRealm()

        val isFirstLaunch = PreferencesManager.get(PreferencesManager.Keys.FIRST_LAUNCH, true)
        if (isFirstLaunch) {
            initializeUserId()
            PreferencesManager.set(PreferencesManager.Keys.FIRST_LAUNCH, false)
        }
        createFreeNote()
    }

    /**
     * SharedPreferences を初期化
     */
    private fun initializePreferences() {
        PreferencesManager.init(context)
    }

    /**
     * Realm を初期化
     */
    private fun initializeRealm() {
        RealmManager.initRealm(context)
    }

    /**
     * ユーザーIDを生成
     */
    private fun initializeUserId() {
        PreferencesManager.set(PreferencesManager.Keys.USER_ID, UUID.randomUUID().toString())
    }

    /**
     * フリーノートを作成
     */
    private suspend fun createFreeNote() {
        val noteViewModel = NoteViewModel()
        val freeNote = noteViewModel.getFreeNote()
        if (freeNote != null) return
        noteViewModel.saveFreeNote(
            title = context.getString(R.string.freeNote),
            detail = context.getString(R.string.defaltFreeNoteDetail)
        )
    }
}