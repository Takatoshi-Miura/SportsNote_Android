package com.example.sportsnote.model

import com.example.sportsnote.utils.NoteType
import com.example.sportsnote.utils.Weather
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

/**
 * ノート
 */
open class Note : RealmObject {

    @PrimaryKey
    var noteID: String

    var userID: String
    var noteType: Int
    var isDeleted: Boolean
    var created_at: Date
    var updated_at: Date

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
        this.userID = "" // TODO: SharedPreferenceからユーザーIDを取得
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
    constructor(title: String): this() {
        this.noteType = NoteType.FREE.value
        this.title = title
    }

    // 練習ノートのイニシャライザ
    constructor(purpose: String, detail: String): this() {
        this.noteType = NoteType.PRACTICE.value
        this.purpose = purpose
        this.detail = detail
    }

    // 大会ノートのイニシャライザ
    constructor(target: String, consciousness: String, result: String): this() {
        this.noteType = NoteType.TOURNAMENT.value
        this.target = target
        this.consciousness = consciousness
        this.result = result
    }

}