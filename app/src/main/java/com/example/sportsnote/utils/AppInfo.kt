package com.example.sportsnote.utils
import android.content.Context
import android.content.pm.PackageManager

object AppInfo {

    /**
     * アプリバージョンを取得
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
}