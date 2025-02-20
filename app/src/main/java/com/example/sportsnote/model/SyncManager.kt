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
    var updated_at: Date
}

object SyncManager {

    private val realmManager = RealmManager()

    /**
     * Firebase と Realm のデータを同期する汎用メソッド
     *
     * @param T Syncable を実装したデータ型
     * @param getFirebaseData Firebase からデータを取得する関数
     * @param getRealmData Realm からデータを取得する関数
     * @param saveToFirebase Firebase にデータを保存する関数
     * @param updateFirebase Firebase のデータを更新する関数
     */
    suspend fun <T> syncData(
        getFirebaseData: suspend () -> List<T>,
        getRealmData: suspend () -> List<T>,
        saveToFirebase: suspend (T) -> Unit,
        updateFirebase: suspend (T) -> Unit
    ) where T : Syncable, T : RealmObject {
        // Firebase と Realm のデータを取得
        val firebaseArray = withContext(Dispatchers.IO) { getFirebaseData() }
        val realmArray = withContext(Dispatchers.IO) { getRealmData() }

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
            getFirebaseData = { FirebaseManager.getAllGroup() },
            getRealmData = { realmManager.getDataList(Group::class.java) },
            saveToFirebase = { FirebaseManager.saveGroup(it) },
            updateFirebase = { FirebaseManager.updateGroup(it) }
        )
    }

    /**
     * Task を同期
     */
    suspend fun syncTask() {
        syncData(
            getFirebaseData = { FirebaseManager.getAllTask() },
            getRealmData = { realmManager.getDataList(TaskData::class.java) },
            saveToFirebase = { FirebaseManager.saveTask(it) },
            updateFirebase = { FirebaseManager.updateTask(it) }
        )
    }

    /**
     * Measures を同期
     */
    suspend fun syncMeasures() {
        syncData(
            getFirebaseData = { FirebaseManager.getAllMeasures() },
            getRealmData = { realmManager.getDataList(Measures::class.java) },
            saveToFirebase = { FirebaseManager.saveMeasures(it) },
            updateFirebase = { FirebaseManager.updateMeasures(it) }
        )
    }

    /**
     * Memo を同期
     */
    suspend fun syncMemo() {
        syncData(
            getFirebaseData = { FirebaseManager.getAllMemo() },
            getRealmData = { realmManager.getDataList(Memo::class.java) },
            saveToFirebase = { FirebaseManager.saveMemo(it) },
            updateFirebase = { FirebaseManager.updateMemo(it) }
        )
    }

    /**
     * Target を同期
     */
    suspend fun syncTarget() {
        syncData(
            getFirebaseData = { FirebaseManager.getAllTarget() },
            getRealmData = { realmManager.getDataList(Target::class.java) },
            saveToFirebase = { FirebaseManager.saveTarget(it) },
            updateFirebase = { FirebaseManager.updateTarget(it) }
        )
    }
}