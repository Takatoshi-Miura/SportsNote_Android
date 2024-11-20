package com.example.sportsnote.utils

/**
 * 色
 */
enum class Color(val rawValue: Int) {
    RED(0),
    PINK(1),
    ORANGE(2),
    YELLOW(3),
    GREEN(4),
    BLUE(5),
    PURPLE(6),
    GRAY(7);

    /** 色のタイトル */
    val title: String
        get() = when (this) {
            RED -> "Red" // TODO: 多言語対応
            PINK -> "Pink"
            ORANGE -> "Orange"
            YELLOW -> "Yellow"
            GREEN -> "Green"
            BLUE -> "Blue"
            PURPLE -> "Purple"
            GRAY -> "Gray"
        }

    /** 色 */
    val color: Int
        get() = rawValue
}