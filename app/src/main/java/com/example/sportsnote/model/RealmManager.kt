package com.example.sportsnote.model

import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.executeTransactionAwait
import kotlinx.coroutines.Dispatchers

object RealmConstants {
    const val DATABASE_NAME = "sportsnote.realm"
    const val SCHEMA_VERSION = 1L
}

class RealmManager {

    private val realm: Realm = Realm.getDefaultInstance()

    companion object {

        /**
         * Realmを初期化
         *
         * @param context Context
         */
        fun initRealm(context: Context) {
            // Realmの初期化
            Realm.init(context)

            // Realm設定
            val config = RealmConfiguration.Builder()
                .name(RealmConstants.DATABASE_NAME)
                .schemaVersion(RealmConstants.SCHEMA_VERSION)
                .deleteRealmIfMigrationNeeded() // マイグレーションが必要な場合、データ削除
                .build()
            Realm.setDefaultConfiguration(config)
        }
    }

    /**
     * Noteリストを取得
     *
     * @return List<Note>
     */
    fun selectNoteList(): List<Note> {
        val notes = realm.where(Note::class.java)
            .equalTo("isDeleted", false)
            .findAll()
        return realm.copyFromRealm(notes)
    }

    /**
     * Noteを保存する処理
     */
    suspend fun saveNote(note: Note) {
        realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
            realmTransaction.insert(note)
        }
    }

    /**
     * Realmインスタンスを閉じる
     */
    fun close() {
        realm.close()
    }
}