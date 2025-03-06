package com.it6210.sportsnote.utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket

object Network {
    /**
     * デバイスがオンラインか判定
     * @return 接続状態（true→オンライン、false→オフライン）
     */
    suspend fun isOnline(): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val socket = Socket()
                socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
                socket.close()
                true
            } catch (e: Exception) {
                false
            }
        }
}
