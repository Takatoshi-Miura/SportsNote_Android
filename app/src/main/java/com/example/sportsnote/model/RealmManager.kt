package com.example.sportsnote.model

import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmObject
import io.realm.Sort
import io.realm.kotlin.executeTransactionAwait
import io.realm.kotlin.where
import kotlinx.coroutines.Dispatchers
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

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
     * groupIDに合致する完了した課題を取得
     *
     * @param groupID groupID
     * @return List<TaskData>
     */
    fun getCompletedTasksByGroupId(groupID: String): List<TaskData> {
        return realm.where(TaskData::class.java)
            .equalTo("groupID", groupID)
            .equalTo("isComplete", true)
            .equalTo("isDeleted", false)
            .sort("order", Sort.ASCENDING)
            .findAll()
            .toList()
    }

    /**
     * taskIDに合致する対策を取得
     *
     * @param taskID taskID
     * @return List<Measures>
     */
    fun getMeasuresByTaskID(taskID: String): List<Measures> {
        return realm.where(Measures::class.java)
            .equalTo("taskID", taskID)
            .equalTo("isDeleted", false)
            .sort("order", Sort.ASCENDING)
            .findAll()
            .toList()
    }

    /**
     * 指定した日付に合致するノートを取得
     *
     * @param selectedDate 日付
     * @return List<Note>
     */
    fun getNotesByDate(selectedDate: LocalDate): List<Note> {
        val realm = Realm.getDefaultInstance()
        val startOfDay = Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
        val endOfDay = Date.from(selectedDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant())

        return realm.where(Note::class.java)
            .equalTo("isDeleted", false)
            .greaterThanOrEqualTo("date", startOfDay)
            .lessThan("date", endOfDay)
            .findAll()
            .toList()
    }

    /**
     * 指定した年と月に合致し、削除されていない目標を取得
     *
     * @param year 取得したい目標の年
     * @param month 取得したい目標の月
     * @return 条件に一致する目標のリスト
     */
    fun fetchTargetsByYearMonth(year: Int, month: Int): List<Target> {
        return realm.where<Target>()
            .beginGroup()
            .equalTo("isYearlyTarget", false)
            .equalTo("year", year)
            .equalTo("month", month)
            .equalTo("isDeleted", false)
            .endGroup()
            .or()
            .beginGroup()
            .equalTo("isYearlyTarget", true)
            .equalTo("year", year)
            .equalTo("isDeleted", false)
            .endGroup()
            .findAll()
            .toList()
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
                when (it) {
                    is Group -> { it.isDeleted = true }
                    is Measures -> { it.isDeleted = true }
                    is Memo -> { it.isDeleted = true }
                    is Note -> { it.isDeleted = true }
                    is Target -> { it.isDeleted = true }
                    is TaskData -> { it.isDeleted = true }
                }
                realmTransaction.insertOrUpdate(it)

                // TaskDataを削除する場合、関連するMeasuresも論理削除
                if (it is TaskData) {
                    val relatedMeasures = realmTransaction.where(Measures::class.java)
                        .equalTo("taskID", id)
                        .findAll()
                    relatedMeasures.forEach { measure ->
                        measure.isDeleted = true
                        realmTransaction.insertOrUpdate(measure)
                    }
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