package com.example.sportsnote.utils

import android.content.Context
import com.example.sportsnote.R
import androidx.compose.ui.graphics.Color as ComposeColor

/**
 * カラー
 *
 * @param id ID
 * @param titleRes タイトル文字列のリソースID
 * @param colorValue カラー
 */
enum class Color(
    val id: Int,
    val titleRes: Int,
    val colorValue: ComposeColor,
) {
    RED(0, R.string.red, ComposeColor(0xFFFF0000)),
    PINK(1, R.string.pink, ComposeColor(0xFFFFC0CB)),
    ORANGE(2, R.string.orange, ComposeColor(0xFFFFA500)),
    YELLOW(3, R.string.yellow, ComposeColor(0xFFFFFF00)),
    GREEN(4, R.string.green, ComposeColor(0xFF008000)),
    BLUE(5, R.string.blue, ComposeColor(0xFF0000FF)),
    PURPLE(6, R.string.purple, ComposeColor(0xFF800080)),
    GRAY(7, R.string.gray, ComposeColor(0xFF808080)),
    ;

    /**
     * タイトル文字列を取得
     *
     * @param context Context
     * @return カラー名
     */
    fun getTitle(context: Context): String {
        return context.getString(titleRes)
    }

    /**
     * 実際の色を取得 (ComposeColor)
     *
     * @return ComposeColor型の色
     */
    fun toComposeColor(): ComposeColor = colorValue

    companion object {
        /**
         * IDからColorを取得
         *
         * @param id ID
         * @return Color
         */
        fun fromInt(id: Int): Color {
            return Color.entries.first { it.id == id }
        }
    }
}
