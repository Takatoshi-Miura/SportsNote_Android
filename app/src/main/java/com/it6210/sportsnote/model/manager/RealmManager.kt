package com.it6210.sportsnote.model.manager

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.it6210.sportsnote.model.Group
import com.it6210.sportsnote.model.Measures
import com.it6210.sportsnote.model.Memo
import com.it6210.sportsnote.model.Note
import com.it6210.sportsnote.model.Target
import com.it6210.sportsnote.model.TaskData
import com.it6210.sportsnote.utils.NoteType
import io.realm.Case
import io.realm.DynamicRealm
import io.realm.DynamicRealmObject
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmObject
import io.realm.Sort
import io.realm.kotlin.where
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

object RealmConstants {
    const val DATABASE_NAME = "sportsnote.realm"
    const val SCHEMA_VERSION = 1L
}

class RealmManager private constructor() {
    companion object {
        /**
         * Realmを初期化（起動準備）
         *
         * @param context Context
         */
        fun initRealm(context: Context) {
            // Realmの初期化
            Realm.init(context)

            // Realm設定
            val config =
                RealmConfiguration.Builder()
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

        // シングルトンインスタンス
        private var instance: RealmManager? = null

        /**
         * インスタンス取得
         *
         * @return RealmManagerインスタンス
         */
        fun getInstance(): RealmManager {
            return instance ?: synchronized(this) {
                instance ?: RealmManager().also { instance = it }
            }
        }

        /**
         * Realmの全データを削除する
         */
        suspend fun clearAll() {
            withContext(Dispatchers.IO) {
                val realm = Realm.getDefaultInstance()
                try {
                    realm.executeTransaction { transactionRealm ->
                        transactionRealm.deleteAll()
                    }
                } finally {
                    realm.close()
                }
            }
        }
    }

    /**
     * 汎用的なデータ保存メソッド
     *
     * @param T RealmObjectを継承したデータ型
     * @param item 保存するデータ
     */
    suspend fun <T : RealmObject> saveItem(item: T) {
        withContext(Dispatchers.IO) {
            val realm = Realm.getDefaultInstance()
            try {
                realm.executeTransaction {
                    it.insertOrUpdate(item)
                }
            } finally {
                realm.close()
            }
        }
    }

    /**
     * すべてのデータの userID を指定した値に更新する
     *
     * @param userId 更新後の userID
     */
    suspend fun updateAllUserIds(userId: String) {
        withContext(Dispatchers.IO) {
            val realm = Realm.getDefaultInstance()
            try {
                val config = realm.configuration
                DynamicRealm.getInstance(config).use { dynamicRealm ->
                    dynamicRealm.executeTransaction { transactionRealm ->
                        val allSchemaClasses = transactionRealm.configuration.realmObjectClasses
                        allSchemaClasses.forEach { clazz ->
                            val results = transactionRealm.where(clazz.simpleName).findAll()
                            results.forEach { obj ->
                                val dynamicObj = obj as DynamicRealmObject
                                if (dynamicObj.hasField("userID")) {
                                    dynamicObj.set("userID", userId)
                                }
                            }
                        }
                    }
                }
            } finally {
                realm.close()
            }
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
        val realm = Realm.getDefaultInstance()
        return try {
            realm.where(T::class.java).equalTo(getPrimaryKeyName<T>(), id).findFirst()?.let { realm.copyFromRealm(it) }
        } catch (e: Exception) {
            null
        } finally {
            realm.close()
        }
    }

    /**
     * 汎用的なデータ取得メソッド
     *
     * @param clazz 取得するデータ型のクラス
     * @return 条件に一致するデータのリスト
     */
    fun <T : RealmObject> getDataList(clazz: Class<T>): List<T> {
        val realm = Realm.getDefaultInstance()
        return try {
            realm.where(clazz)
                .equalTo("isDeleted", false)
                .apply {
                    if (realm.schema[clazz.simpleName]?.hasField("order") == true) {
                        sort("order", Sort.ASCENDING)
                    }
                }
                .findAll()
                .let { realm.copyFromRealm(it) }
        } finally {
            realm.close()
        }
    }

    /**
     * 任意のRealmObjectでisDeletedがfalseのデータ数を取得
     *
     * @param clazz RealmObjectのクラス型
     * @return Int データ数
     */
    fun <T : RealmObject> getCount(clazz: Class<T>): Int {
        val realm = Realm.getDefaultInstance()
        return try {
            realm.where(clazz)
                .equalTo("isDeleted", false)
                .count()
                .toInt()
        } finally {
            realm.close()
        }
    }

    /**
     * groupIDに合致する完了した課題を取得
     *
     * @param groupID groupID
     * @return List<TaskData>
     */
    fun getCompletedTasksByGroupId(groupID: String): List<TaskData> {
        val realm = Realm.getDefaultInstance()
        return try {
            realm.where(TaskData::class.java)
                .equalTo("groupID", groupID)
                .equalTo("isComplete", true)
                .equalTo("isDeleted", false)
                .sort("order", Sort.ASCENDING)
                .findAll()
                .map { realm.copyFromRealm(it) }
        } finally {
            realm.close()
        }
    }

    /**
     * taskIDに合致する対策を取得
     *
     * @param taskID taskID
     * @return List<Measures>
     */
    fun getMeasuresByTaskID(taskID: String): List<Measures> {
        val realm = Realm.getDefaultInstance()
        return try {
            realm.where(Measures::class.java)
                .equalTo("taskID", taskID)
                .equalTo("isDeleted", false)
                .sort("order", Sort.ASCENDING)
                .findAll()
                .map { realm.copyFromRealm(it) }
        } finally {
            realm.close()
        }
    }

    /**
     * フリーノートを取得
     */
    fun getFreeNote(): Note? {
        val realm = Realm.getDefaultInstance()
        return try {
            realm.where(Note::class.java)
                .equalTo("noteType", NoteType.FREE.value)
                .equalTo("isDeleted", false)
                .findFirst()?.let { realm.copyFromRealm(it) }
        } finally {
            realm.close()
        }
    }

    /**
     * 指定された文字列を含むノートを検索
     *
     * @param query 検索する文字列
     * @return 検索結果のノートリスト
     */
    fun searchNotesByQuery(query: String): List<Note> {
        val realm = Realm.getDefaultInstance()
        return try {
            val freeNotes =
                realm.where(Note::class.java)
                    .equalTo("noteType", NoteType.FREE.value)
                    .equalTo("isDeleted", false)
                    .findAll()
                    .map { realm.copyFromRealm(it) }

            val queryNotes =
                realm.where(Note::class.java)
                    .equalTo("isDeleted", false)
                    .and()
                    .beginGroup()
                    .contains("condition", query, Case.INSENSITIVE)
                    .or()
                    .contains("reflection", query, Case.INSENSITIVE)
                    .or()
                    .contains("purpose", query, Case.INSENSITIVE)
                    .or()
                    .contains("detail", query, Case.INSENSITIVE)
                    .or()
                    .contains("target", query, Case.INSENSITIVE)
                    .or()
                    .contains("consciousness", query, Case.INSENSITIVE)
                    .or()
                    .contains("result", query, Case.INSENSITIVE)
                    .endGroup()
                    .findAll()
                    .map { realm.copyFromRealm(it) }
            (freeNotes + queryNotes).distinct()
        } finally {
            realm.close()
        }
    }

    /**
     * 指定した日付に合致するノートを取得
     *
     * @param selectedDate 日付
     * @return List<Note>
     */
    fun getNotesByDate(selectedDate: LocalDate): List<Note> {
        val realm = Realm.getDefaultInstance()
        return try {
            val startOfDay = Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
            val endOfDay = Date.from(selectedDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant())
            realm.where(Note::class.java)
                .equalTo("isDeleted", false)
                .notEqualTo("noteType", NoteType.FREE.value)
                .greaterThanOrEqualTo("date", startOfDay)
                .lessThan("date", endOfDay)
                .findAll()
                .map { realm.copyFromRealm(it) }
        } finally {
            realm.close()
        }
    }

    /**
     * measuresIDに合致するメモを取得
     *
     * @param measuresID 対策ID
     * @return List<Memo>
     */
    fun getMemosByMeasuresID(measuresID: String): List<Memo> {
        val realm = Realm.getDefaultInstance()
        return try {
            realm.where(Memo::class.java)
                .equalTo("measuresID", measuresID)
                .equalTo("isDeleted", false)
                .sort("created_at", Sort.ASCENDING)
                .findAll()
                .map { realm.copyFromRealm(it) }
        } finally {
            realm.close()
        }
    }

    /**
     * noteIDに合致するメモを取得
     *
     * @param noteID ノートID
     * @return List<Memo>
     */
    fun getMemosByNoteID(noteID: String): List<Memo> {
        val realm = Realm.getDefaultInstance()
        return try {
            realm.where(Memo::class.java)
                .equalTo("noteID", noteID)
                .equalTo("isDeleted", false)
                .sort("created_at", Sort.ASCENDING)
                .findAll()
                .map { realm.copyFromRealm(it) }
        } finally {
            realm.close()
        }
    }

    /**
     * ノートの背景色を取得
     *
     * @param noteID ノートID
     * @return 背景色
     */
    fun getNoteBackgroundColor(noteID: String): Color {
        val realm = Realm.getDefaultInstance()
        return try {
            val memo =
                realm.where(Memo::class.java)
                    .equalTo("noteID", noteID)
                    .equalTo("isDeleted", false)
                    .findFirst()
                    ?.let { realm.copyFromRealm(it) } ?: return Color.White

            val measures =
                realm.where(Measures::class.java)
                    .equalTo("measuresID", memo.measuresID)
                    .findFirst()
                    ?.let { realm.copyFromRealm(it) } ?: return Color.White

            val taskData =
                realm.where(TaskData::class.java)
                    .equalTo("taskID", measures.taskID)
                    .findFirst()
                    ?.let { realm.copyFromRealm(it) } ?: return Color.White

            val group =
                realm.where(Group::class.java)
                    .equalTo("groupID", taskData.groupID)
                    .findFirst()
                    ?.let { realm.copyFromRealm(it) } ?: return Color.White

            com.it6210.sportsnote.utils.Color.fromInt(group.color).toComposeColor()
        } finally {
            realm.close()
        }
    }

    /**
     * 指定した年と月に合致し、削除されていない目標を取得
     *
     * @param year 取得したい目標の年
     * @param month 取得したい目標の月
     * @return 条件に一致する目標のリスト
     */
    fun fetchTargetsByYearMonth(
        year: Int,
        month: Int,
    ): List<Target> {
        val realm = Realm.getDefaultInstance()
        return try {
            realm.where<Target>()
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
                .map { realm.copyFromRealm(it) }
        } finally {
            realm.close()
        }
    }

    /**
     * 汎用的な論理削除処理
     *
     * @param T RealmObjectを継承したデータ型
     * @param id 削除するデータのID
     */
    internal suspend inline fun <reified T : RealmObject> logicalDelete(id: String) {
        withContext(Dispatchers.IO) {
            val realm = Realm.getDefaultInstance()
            try {
                realm.executeTransaction { realmTransaction ->
                    val item =
                        realmTransaction.where(T::class.java)
                            .equalTo(getPrimaryKeyName<T>(), id)
                            .findFirst()

                    item?.let {
                        // 指定されたオブジェクトを削除
                        markAsDeleted(it, realmTransaction)

                        // 関連エンティティも削除
                        when (it) {
                            is Note -> deleteRelatedNoteMemos(it.noteID, realmTransaction)
                            is Group -> deleteRelatedTasks(it.groupID, realmTransaction)
                            is TaskData -> deleteRelatedMeasures(it.taskID, realmTransaction)
                            is Measures -> deleteRelatedMeasuresMemos(it.measuresID, realmTransaction)
                        }
                    }
                }
            } finally {
                realm.close()
            }
        }
    }

    /**
     * 任意のオブジェクトを論理削除
     *
     * @param item 削除するオブジェクト
     * @param realmTransaction Realmトランザクション
     */
    private fun markAsDeleted(
        item: RealmObject,
        realmTransaction: Realm,
    ) {
        when (item) {
            is Group -> {
                item.isDeleted = true
            }
            is Measures -> {
                item.isDeleted = true
            }
            is Memo -> {
                item.isDeleted = true
            }
            is Note -> {
                item.isDeleted = true
            }
            is Target -> {
                item.isDeleted = true
            }
            is TaskData -> {
                item.isDeleted = true
            }
        }
        realmTransaction.insertOrUpdate(item)
    }

    /**
     * Note に関連する Memo を削除
     *
     * @param noteID ノートID
     * @param realmTransaction Realmトランザクション
     */
    private fun deleteRelatedNoteMemos(
        noteID: String,
        realmTransaction: Realm,
    ) {
        realmTransaction.where(Memo::class.java)
            .equalTo("noteID", noteID)
            .findAll()
            .forEach { markAsDeleted(it, realmTransaction) }
    }

    /**
     * Group に関連する TaskData, Measures, Memo を削除
     *
     * @param groupID グループID
     * @param realmTransaction Realmトランザクション
     */
    private fun deleteRelatedTasks(
        groupID: String,
        realmTransaction: Realm,
    ) {
        realmTransaction.where(TaskData::class.java)
            .equalTo("groupID", groupID)
            .findAll()
            .forEach { task ->
                markAsDeleted(task, realmTransaction)
                deleteRelatedMeasures(task.taskID, realmTransaction)
            }
    }

    /**
     * TaskData に関連する Measures, Memo を削除
     *
     * @param taskID 課題ID
     * @param realmTransaction Realmトランザクション
     */
    private fun deleteRelatedMeasures(
        taskID: String,
        realmTransaction: Realm,
    ) {
        realmTransaction.where(Measures::class.java)
            .equalTo("taskID", taskID)
            .findAll()
            .forEach { measure ->
                markAsDeleted(measure, realmTransaction)
                deleteRelatedMeasuresMemos(measure.measuresID, realmTransaction)
            }
    }

    /**
     * Measures に関連する Memo を削除
     *
     * @param measuresID 対策ID
     * @param realmTransaction Realmトランザクション
     */
    private fun deleteRelatedMeasuresMemos(
        measuresID: String,
        realmTransaction: Realm,
    ) {
        realmTransaction.where(Memo::class.java)
            .equalTo("measuresID", measuresID)
            .findAll()
            .forEach { markAsDeleted(it, realmTransaction) }
    }
}
