package com.example.sportsnote.utils
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object Network {
    /**
     * デバイスがオンラインか判定
     * @param context アプリケーションのコンテキスト
     * @return 接続状態（true→オンライン、false→オフライン）
     */
    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}
