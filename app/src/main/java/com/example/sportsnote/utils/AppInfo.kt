package com.example.sportsnote.utils
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

object AppInfo {
    /**
     * アプリバージョンを取得
     *
     * @param context アプリケーションのコンテキスト
     * @return バージョン番号（例：1.0.0）
     */
    fun getAppVersion(context: Context): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName ?: "Unknown"
        } catch (e: PackageManager.NameNotFoundException) {
            "Unknown"
        }
    }

    /**
     * ビルド番号を取得
     *
     * @param context アプリケーションのコンテキスト
     * @return ビルド番号
     */
    fun getBuildNo(context: Context): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.longVersionCode.toString()
        } catch (e: PackageManager.NameNotFoundException) {
            "Unknown"
        }
    }

    /**
     * Android OSバージョンを取得
     *
     * @return Android OSバージョン
     */
    fun getAndroidVersionInfo(): String {
        val osVersion = Build.VERSION.RELEASE // Androidのバージョン（例: 13）
        val apiLevel = Build.VERSION.SDK_INT // APIレベル（例: 33）
        return "Android $osVersion (API Level $apiLevel)"
    }

    /**
     * デバイス名を取得
     *
     * @return デバイス名
     */
    fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER // 製造元
        val model = Build.MODEL // モデル名

        // モデル名が製造元名を含まない場合、製造元名を付け加える
        return if (model.startsWith(manufacturer, ignoreCase = true)) {
            model
        } else {
            "$manufacturer $model"
        }
    }
}
