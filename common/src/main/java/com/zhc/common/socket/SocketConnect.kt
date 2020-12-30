package com.zhc.common.socket


import com.zhc.common.utils.SSLUtil
import com.zhc.common.utils.SSLUtil.getSslContext
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket
import okhttp3.OkHttpClient
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import javax.net.ssl.SSLContext

class SocketConnect {

    companion object {
        @JvmStatic
        fun connect(okHttpClient: OkHttpClient, connectBean: ConnectBean, errorAction: (String?) -> Unit): Socket? {
            val sOkHttpClient = okHttpClient
            val opts = IO.Options()
            opts.callFactory = sOkHttpClient
            return connectBean.run {
                val str = StringBuilder()

//                str.append("ability=" + SocketAbility.getAllAbility() + "&")

                opts.reconnectionDelay = (30 * 1000).toLong()
                opts.timeout = (30 * 1000).toLong()
                opts.query = str.toString()
                opts.transports = arrayOf(WebSocket.NAME)

                // todo 加入ssl证书
                val sslContext: SSLContext = SSLUtil.getSslContext(null, null, null)
                val builder = OkHttpClient().newBuilder()

                val sslSocketFactory = sslContext.socketFactory
                builder.sslSocketFactory(sslSocketFactory)
                builder.hostnameVerifier { hostname, session -> true }
                opts.webSocketFactory = builder.build()

                try {
//                    return@run IO.socket(connUrl, opts)
                    return@run  IO.socket("https://socket-io-chat.now.sh/")
//                    return@run IO.socket("http://172.25.113.4:1338", opts)
                } catch (e: Exception) {
                    e.printStackTrace()
                    errorAction.invoke(e.message)
                }
                null
            }
        }
    }
}

class SocketAbility {
    companion object {
        const val COURSE_WARE_UP_STAIR = "shareScreen"
        const val VIDEO_UP_STAIR = "upStairs"
        const val BATCH_GIVE_GOOD = "batchGivegood"
        @JvmStatic
        fun getAllAbility(): String {
            return StringBuilder()
                .append(COURSE_WARE_UP_STAIR).append(",")
                .append(VIDEO_UP_STAIR).append(",")
                .append(BATCH_GIVE_GOOD)
                .toString()
        }
    }
}