package com.example.sportsnote.utils

import com.example.sportsnote.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * 天気
 *
 * @param id ID
 * @param titleRes タイトル文字列のリソースID
 */
enum class Weather(val id: Int, val titleRes: Int) {
    SUNNY(0, R.string.sunny),
    CLOUDY(1, R.string.cloudy),
    RAINY(2, R.string.rainy);

    /**
     * タイトル文字列を取得
     *
     * @return 天気名
     */
    @Composable
    fun getTitle(): String {
        val context = LocalContext.current
        return context.getString(titleRes)
    }

    companion object {
        /**
         * IDからWeatherを取得
         *
         * @param id ID
         * @return Weather
         */
        fun fromInt(id: Int): Weather {
            return entries.first { it.id == id }
        }

        /**
         * 全ての天気のタイトルリストを取得
         *
         * @return 全ての天気のタイトルリスト
         */
        @Composable
        fun getAllTitles(): List<String> {
            return entries.map { it.getTitle() }
        }
    }
}