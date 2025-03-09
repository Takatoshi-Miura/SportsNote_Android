package com.it6210.sportsnote.model

import androidx.compose.ui.graphics.Color
import com.it6210.sportsnote.model.manager.PreferencesManager
import com.it6210.sportsnote.model.manager.Syncable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.Date
import java.util.UUID

/**
 * 課題
 */
open class TaskData : RealmObject, Syncable {
    @PrimaryKey
    var taskID: String

    var userID: String
    var groupID: String
    var title: String
    var cause: String
    var order: Int
    var isComplete: Boolean
    var isDeleted: Boolean
    var created_at: Date
    override var updated_at: Date = Date()

    // デフォルトコンストラクタ
    constructor() {
        this.taskID = UUID.randomUUID().toString()
        this.userID = PreferencesManager.get<String>(PreferencesManager.Keys.USER_ID, UUID.randomUUID().toString())
        this.groupID = ""
        this.title = ""
        this.cause = ""
        this.order = 0
        this.isComplete = false
        this.isDeleted = false
        this.created_at = Date()
        this.updated_at = Date()
    }

    // コンストラクタ
    constructor(
        taskId: String = UUID.randomUUID().toString(),
        title: String,
        cause: String,
        groupId: String,
        isComplete: Boolean,
        created_at: Date = Date(),
    ) {
        this.taskID = taskId
        this.userID = PreferencesManager.get<String>(PreferencesManager.Keys.USER_ID, UUID.randomUUID().toString())
        this.groupID = groupId
        this.title = title
        this.cause = cause
        this.order = 0
        this.isComplete = isComplete
        this.isDeleted = false
        this.created_at = created_at
        this.updated_at = Date()
    }

    // Syncable インターフェースの getId() メソッドを実装
    override fun getId(): String {
        return taskID
    }
}

/**
 * 課題の追加画面用データ
 *
 * @param title 課題のタイトル
 * @param cause 原因
 * @param measuresTitle 対策のタイトル
 * @param groupList Groupのリスト
 */
data class AddTaskData(
    val title: String = "",
    val cause: String = "",
    val measuresTitle: String = "",
    val groupList: List<Group>,
)

/**
 * 課題一覧用データ
 *
 * @param taskID 課題ID
 * @param groupID グループID
 * @param groupColor グループカラー
 * @param title タイトル
 * @param measuresID 対策ID
 * @param measures 最優先の対策
 * @param memoID メモID（存在する場合のみ）
 * @param order 並び順
 */
data class TaskListData(
    val taskID: String,
    val groupID: String,
    val groupColor: Color,
    val title: String,
    val measuresID: String,
    val measures: String,
    var memoID: String?,
    val order: Int,
)

/**
 * 課題詳細用データ
 *
 * @param task TaskData
 * @param measuresList 対策リスト
 */
data class TaskDetailData(
    val task: TaskData,
    var measuresList: List<Measures>,
)
