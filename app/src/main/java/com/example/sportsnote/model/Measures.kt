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
        this.userID = "" // TODO: SharedPreferenceからユーザーIDを取得
        this.taskID = ""
        this.title = ""
        this.order = 0
        this.isDeleted = false
        this.created_at = Date()
        this.updated_at = Date()
    }

}