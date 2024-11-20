package com.example.sportsnote.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

/**
 * メモ
 */
open class Memo : RealmObject {

    @PrimaryKey
    var memoID: String

    var userID: String
    var measuresID: String
    var noteID: String
    var detail: String
    var isDeleted: Boolean
    var created_at: Date
    var updated_at: Date
    var noteDate: Date

    // デフォルトコンストラクタ
    constructor() {
        this.memoID = UUID.randomUUID().toString()
        this.userID = "" // TODO: SharedPreferenceからユーザーIDを取得
        this.measuresID = ""
        this.noteID = ""
        this.detail = ""
        this.isDeleted = false
        this.created_at = Date()
        this.updated_at = Date()
        this.noteDate = Date()
    }

}