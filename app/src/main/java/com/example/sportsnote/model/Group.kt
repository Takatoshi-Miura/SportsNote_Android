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
        this.userID = PreferencesManager.get<String>(PreferencesManager.Keys.USER_ID, UUID.randomUUID().toString())
        this.title = ""
        this.color = Color.RED.id
        this.order = 0
        this.isDeleted = false
        this.created_at = Date()
        this.updated_at = Date()
    }

    // コンストラクタ
    constructor(
        groupId: String = UUID.randomUUID().toString(),
        title: String,
        colorId: Int,
        order: Int,
        created_at: Date
    ) : this() {
        this.title = title
        this.color = colorId
        this.order = order
        this.groupID = groupId
        this.userID = PreferencesManager.get<String>(PreferencesManager.Keys.USER_ID, UUID.randomUUID().toString())
        this.isDeleted = false
        this.created_at = created_at
        this.updated_at = Date()
    }

}