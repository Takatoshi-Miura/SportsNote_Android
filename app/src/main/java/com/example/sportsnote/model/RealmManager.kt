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

    internal val realm: Realm = Realm.getDefaultInstance()

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

        /**
         * Realmファイルのパスを出力
         */
        fun printRealmFilePath() {
            val realmFile = Realm.getDefaultInstance().configuration.path
            println("Realm file path: $realmFile")
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
            realmTransaction.insertOrUpdate(item)
        }
    }

    /**
     * 指定したクラスに対応するプライマリキーのプロパティ名を取得
     *
     * @param T RealmObjectを継承したデータ型
     * @return Tに対応するプライマリキーのプロパティ名
     * @throws IllegalArgumentException 対応していないクラスの場合にスローされる
     */
    private inline fun <reified T : RealmObject> getPrimaryKeyName(): String {
        return when (T::class) {
            Group::class -> "groupID"
            Measures::class -> "measuresID"
            Memo::class -> "memoID"
            Note::class -> "noteID"
            Target::class -> "targetID"
            TaskData::class -> "taskID"
            else -> throw IllegalArgumentException("Unsupported class")
        }
    }

    /**
     * 汎用的なデータ取得メソッド（ID指定）
     *
     * @param T RealmObjectを継承したデータ型
     * @param id 検索するID（文字列）
     * @return 取得データ（存在しない場合やエラーが発生した場合は`null`）
     */
    internal inline fun <reified T : RealmObject> getObjectById(id: String): T? {
        return try {
            realm.where(T::class.java).equalTo(getPrimaryKeyName<T>(), id).findFirst()
        } catch (e: Exception) {
            null
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
     * 汎用的な論理削除処理
     *
     * @param T RealmObjectを継承したデータ型
     * @param id 削除するデータのID
     */
    internal suspend inline fun <reified T : RealmObject> logicalDelete(id: String) {
        realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
            val item = realmTransaction.where(T::class.java).equalTo(getPrimaryKeyName<T>(), id).findFirst()
            item?.let {
                if (it is Group) {
                    it.isDeleted = true
                    realmTransaction.insertOrUpdate(it)
                }
            }
        }
    }

    /**
     * Realmインスタンスを閉じる
     */
    fun close() {
        realm.close()
    }
}