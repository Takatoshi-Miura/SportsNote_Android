package com.example.sportsnote.model

import io.realm.RealmObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

/**
 * 同期可能なデータの共通インターフェース
 */
interface Syncable {
    fun getId(): String
    val updated_at: Date
}

object SyncManager {

    private val realmManager = RealmManager()

    /**
     * Firebase と Realm のデータを同期する汎用メソッド
     *
     * @param T Syncable を実装したデータ型
     * @param clazz Realm から取得するデータ型のクラス
     * @param getFirebaseData Firebase からデータを取得する関数
     * @param saveToFirebase Firebase にデータを保存する関数
     * @param updateFirebase Firebase のデータを更新する関数
     */
    suspend fun <T> syncData(
        clazz: Class<T>,
        getFirebaseData: suspend () -> List<T>,
        saveToFirebase: suspend (T) -> Unit,
        updateFirebase: suspend (T) -> Unit
    ) where T : Syncable, T : RealmObject {
        // Firebase と Realm のデータを取得
        val firebaseArray = withContext(Dispatchers.IO) { getFirebaseData() }
        val realmArray = withContext(Dispatchers.IO) { realmManager.getDataList(clazz) }

        // ID をキーとしたマップを作成
        val firebaseMap = firebaseArray.associateBy { it.getId() }
        val realmMap = realmArray.associateBy { it.getId() }

        // Firebase もしくは Realm にしか存在しないデータを取得
        val onlyFirebaseID = firebaseMap.keys - realmMap.keys
        val onlyRealmID = realmMap.keys - firebaseMap.keys

        // データの同期処理
        withContext(Dispatchers.IO) {
            // Realm にしかないデータを Firebase に保存
            onlyRealmID.forEach { id ->
                realmMap[id]?.let { saveToFirebase(it) }
            }

            // Firebase にしかないデータを Realm に保存
            onlyFirebaseID.forEach { id ->
                firebaseMap[id]?.let { realmManager.saveItem(it) }
            }

            // 両方に存在するデータの更新日時を比較し、新しい方に更新
            (firebaseMap.keys intersect realmMap.keys).forEach { id ->
                val realmItem = realmMap[id]
                val firebaseItem = firebaseMap[id]
                when {
                    realmItem != null && firebaseItem != null -> {
                        if (realmItem.updated_at > firebaseItem.updated_at) {
                            updateFirebase(realmItem)
                        } else if (firebaseItem.updated_at > realmItem.updated_at) {
                            realmManager.saveItem(firebaseItem)
                        }
                    }
                }
            }
        }
    }

    /**
     * Group を同期
     */
    suspend fun syncGroup() {
        syncData(
            clazz = Group::class.java,
            getFirebaseData = { FirebaseManager.getAllGroup() },
            saveToFirebase = { FirebaseManager.saveGroup(it) },
            updateFirebase = { FirebaseManager.updateGroup(it) }
        )
    }

}