package com.example.sportsnote.model

import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmObject
import io.realm.Sort
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
     * 汎用的なデータ保存メソッド
     *
     * @param T RealmObjectを継承したデータ型
     * @param item 保存するデータ
     */
    suspend fun <T : RealmObject> saveItem(item: T) {
        realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
            realmTransaction.insert(item)
        }
    }

    /**
     * 汎用的なデータ取得メソッド
     *
     * @param clazz 取得するデータ型のクラス
     * @return 条件に一致するデータのリスト
     */
    fun <T : RealmObject> getDataList(clazz: Class<T>): List<T> {
        return realm.where(clazz)
            .equalTo("isDeleted", false)
            .apply {
                // `order` フィールドが存在する場合、昇順で並び替え
                if (realm.schema[clazz.simpleName]?.hasField("order") == true) {
                    sort("order", Sort.ASCENDING)
                }
            }
            .findAll()
            .let { realm.copyFromRealm(it) }
    }

    /**
     * 任意のRealmObjectでisDeletedがfalseのデータ数を取得
     *
     * @param clazz RealmObjectのクラス型
     * @return Int データ数
     */
    fun <T : RealmObject> getCount(clazz: Class<T>): Int {
        return realm.where(clazz)
            .equalTo("isDeleted", false)
            .count()
            .toInt()
    }

    /**
     * Realmインスタンスを閉じる
     */
    fun close() {
        realm.close()
    }
}