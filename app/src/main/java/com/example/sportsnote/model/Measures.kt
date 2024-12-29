package com.example.sportsnote.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

/**
 * 対策
 */
open class Measures : RealmObject {

    @PrimaryKey
    var measuresID: String

    var userID: String
    var taskID: String
    var title: String
    var order: Int
    var isDeleted: Boolean
    var created_at: Date
    var updated_at: Date

    // デフォルトコンストラクタ
    constructor() {
        this.measuresID = UUID.randomUUID().toString()
        this.userID = PreferencesManager.get<String>(PreferencesManager.Keys.USER_ID, UUID.randomUUID().toString())
        this.taskID = ""
        this.title = ""
        this.order = 0
        this.isDeleted = false
        this.created_at = Date()
        this.updated_at = Date()
    }

    // コンストラクタ
    constructor(
        measuresId: String = UUID.randomUUID().toString(),
        taskId: String,
        title: String,
    ) {
        this.measuresID = measuresId
        this.userID = PreferencesManager.get<String>(PreferencesManager.Keys.USER_ID, UUID.randomUUID().toString())
        this.taskID = taskId
        this.title = title
        this.order = 0
        this.isDeleted = false
        this.created_at = Date()
        this.updated_at = Date()
    }
}