package com.it6210.sportsnote.model

import androidx.compose.ui.graphics.Color
import com.it6210.sportsnote.utils.NoteType
import com.it6210.sportsnote.utils.Weather
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.Date
import java.util.UUID

/**
 * ノート
 */
open class Note : RealmObject, Syncable {
    @PrimaryKey
    var noteID: String

    var userID: String
    var noteType: Int
    var isDeleted: Boolean
    var created_at: Date
    override var updated_at: Date = Date()

    // フリーノート
    var title: String

    // 練習・大会共通
    var date: Date
    var weather: Int
    var temperature: Int
    var condition: String
    var reflection: String

    // 練習ノート
    var purpose: String
    var detail: String

    // 大会ノート
    var target: String
    var consciousness: String
    var result: String

    // デフォルトコンストラクタ
    constructor() {
        this.noteID = UUID.randomUUID().toString()
        this.userID = PreferencesManager.get<String>(PreferencesManager.Keys.USER_ID, UUID.randomUUID().toString())
        this.noteType = NoteType.FREE.value
        this.isDeleted = false
        this.created_at = Date()
        this.updated_at = Date()
        this.title = ""
        this.date = Date()
        this.weather = Weather.SUNNY.id
        this.temperature = 0
        this.condition = ""
        this.reflection = ""
        this.purpose = ""
        this.detail = ""
        this.target = ""
        this.consciousness = ""
        this.result = ""
    }

    // フリーノートのイニシャライザ
    constructor(title: String) : this() {
        this.noteType = NoteType.FREE.value
        this.title = title
    }

    // 練習ノートのイニシャライザ
    constructor(purpose: String, detail: String) : this() {
        this.noteType = NoteType.PRACTICE.value
        this.purpose = purpose
        this.detail = detail
    }

    // 大会ノートのイニシャライザ
    constructor(target: String, consciousness: String, result: String) : this() {
        this.noteType = NoteType.TOURNAMENT.value
        this.target = target
        this.consciousness = consciousness
        this.result = result
    }

    // Syncable インターフェースの getId() メソッドを実装
    override fun getId(): String {
        return noteID
    }
}

/** ノート一覧表示用 */
data class NoteListItem(
    val noteID: String,
    val noteType: Int,
    val date: Date,
    val backGroundColor: Color,
    val title: String,
    val subTitle: String,
)

/** 練習ノート詳細画面表示用 */
data class PracticeNote(
    val noteID: String,
    val date: Date,
    val weather: Int,
    val temperature: Int,
    val condition: String,
    val purpose: String,
    val detail: String,
    val reflection: String,
    val taskReflections: Map<TaskListData, String>,
    val created_at: Date,
)
