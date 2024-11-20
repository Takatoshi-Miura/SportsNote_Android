package com.example.sportsnote.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

/**
 * 目標
 */
open class Target : RealmObject {

    @PrimaryKey
    var targetID: String

    var userID: String
    var title: String
    var year: Int
    var month: Int
    var isYearlyTarget: Boolean
    var isDeleted: Boolean
    var created_at: Date
    var updated_at: Date

    // デフォルトコンストラクタ
    constructor() {
        this.targetID = UUID.randomUUID().toString()
        this.userID = "" // TODO: SharedPreferenceからユーザーIDを取得
        this.title = ""
        this.year = 2020
        this.month = 1
        this.isYearlyTarget = false
        this.isDeleted = false
        this.created_at = Date()
        this.updated_at = Date()
    }

}