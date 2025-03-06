
@file:Suppress("ktlint:standard:property-naming")

package com.it6210.sportsnote.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.Date
import java.util.UUID

/**
 * 目標
 */
open class Target : RealmObject, Syncable {
    @PrimaryKey
    var targetID: String

    var userID: String
    var title: String
    var year: Int
    var month: Int
    var isYearlyTarget: Boolean
    var isDeleted: Boolean
    var created_at: Date
    override var updated_at: Date = Date()

    // デフォルトコンストラクタ
    constructor() {
        this.targetID = UUID.randomUUID().toString()
        this.userID = PreferencesManager.get<String>(PreferencesManager.Keys.USER_ID, UUID.randomUUID().toString())
        this.title = ""
        this.year = 2020
        this.month = 1
        this.isYearlyTarget = false
        this.isDeleted = false
        this.created_at = Date()
        this.updated_at = Date()
    }

    // Syncable インターフェースの getId() メソッドを実装
    override fun getId(): String {
        return targetID
    }
}
