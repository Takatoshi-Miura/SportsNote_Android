package com.example.sportsnote.utils

/**
 * 天気
 */
enum class Weather(val value: Int) {
    SUNNY(0),   // 晴れ
    CLOUDY(1),  // くもり
    RAINY(2);   // 雨

    // タイトル
    val title: String
        get() = when (this) {
            SUNNY -> "Sunny"  // TODO: 多言語対応
            CLOUDY -> "Cloudy"
            RAINY -> "Rainy"
        }

    // TODO: 画像ファイル追加
//    val image: Int
//        get() = when (this) {
//            SUNNY -> R.drawable.sunny
//            CLOUDY -> R.drawable.cloudy
//            RAINY -> R.drawable.rainy
//        }

    companion object {
        // Int 値から Weather を取得するためのメソッド
        fun fromInt(value: Int): Weather {
            return entries.first { it.value == value }
        }
    }
}