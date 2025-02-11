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
        this.userID = PreferencesManager.get<String>(PreferencesManager.Keys.USER_ID, UUID.randomUUID().toString())
        this.measuresID = ""
        this.noteID = ""
        this.detail = ""
        this.isDeleted = false
        this.created_at = Date()
        this.updated_at = Date()
        this.noteDate = Date()
    }

    // コンストラクタ
    constructor(
        memoID: String = UUID.randomUUID().toString(),
        measuresID: String,
        noteID: String,
        detail: String,
        created_at: Date = Date()
    ) {
        this.memoID = memoID
        this.userID = PreferencesManager.get<String>(PreferencesManager.Keys.USER_ID, UUID.randomUUID().toString())
        this.measuresID = measuresID
        this.noteID = noteID
        this.detail = detail
        this.isDeleted = false
        this.created_at = created_at
        this.updated_at = Date()
        this.noteDate = Date()
    }
}

// 対策画面用データクラス
data class MeasuresMemo(
    val memoID: String,
    val measuresID: String,
    val noteID: String,
    val detail: String,
    val date: Date
)