package com.example.sportsnote.utils

/**
 * ノート種別
 */
enum class NoteType(val value: Int) {
    FREE(0),        // フリーノート
    PRACTICE(1),    // 練習ノート
    TOURNAMENT(2);  // 大会ノート

    companion object {
        // Int 値から NoteType を取得するためのメソッド
        fun fromInt(value: Int): NoteType {
            return entries.first { it.value == value }
        }
    }
}