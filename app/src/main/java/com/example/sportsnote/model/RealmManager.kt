package com.example.sportsnote.model

import io.realm.Realm
import io.realm.kotlin.executeTransactionAwait
import kotlinx.coroutines.Dispatchers

class RealmManager {

    private val realm: Realm = Realm.getDefaultInstance()

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