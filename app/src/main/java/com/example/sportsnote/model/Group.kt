package com.example.sportsnote.model

import com.example.sportsnote.utils.Color
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

/**
 * グループ
 */
open class Group : RealmObject {

    @PrimaryKey
    var groupID: String

    var userID: String
    var title: String
    var color: Int
    var order: Int
    var isDeleted: Boolean
    var created_at: Date
    var updated_at: Date

    // デフォルトのコンストラクタ
    constructor() {
        this.groupID = UUID.randomUUID().toString()
        this.userID = ""  // TODO: SharedPreferenceからユーザーIDを取得
        this.title = ""
        this.color = 0
        this.order = 0
        this.isDeleted = false
        this.created_at = Date()
        this.updated_at = Date()
    }

    // コンストラクタ
    constructor(title: String, color: Color, order: Int) : this() {
        this.title = title
        this.color = color.rawValue
        this.order = order
        this.groupID = UUID.randomUUID().toString()
        this.userID = ""  // TODO: SharedPreferenceからユーザーIDを取得
        this.isDeleted = false
        this.created_at = Date()
        this.updated_at = Date()
    }

}