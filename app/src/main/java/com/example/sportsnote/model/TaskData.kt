package com.example.sportsnote.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

/**
 * 課題
 */
open class TaskData : RealmObject {

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
    var updated_at: Date

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

}