package com.zhc.common.okHttp

import android.util.Log
import com.zhc.common.utils.JsonUtil
import okhttp3.logging.HttpLoggingInterceptor

/**
 * 调试网络请求
 */
class HttpLogger : HttpLoggingInterceptor.Logger {
    companion object {
        //不监听的域名
        val ignoreHost = mutableListOf<String>().apply {
            add("https://www.baidu.com")
        }
    }

    private val sb = StringBuffer()

    @Synchronized
    override fun log(message: String) {
        var msg = message
        // 请求或者响应开始
        if (message.startsWith("--> POST") || message.startsWith("--> GET")) {
            sb.setLength(0)
        }
        // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
        if (message.startsWith("{") && message.endsWith("}")
                || message.startsWith("[") && message.endsWith("]")) {
            msg = JsonUtil.formatJson(JsonUtil.decodeUnicode(message))
        }
        if (notIgnore(sb)) {
            sb.append(msg + "\n")
            // 响应结束，打印整条日志
            if (message.startsWith("<-- END HTTP") || message.startsWith("<-- HTTP FAILED")) {
                // d华为的有些机型打印不错，需要e
                httpLog("=======================================================================")
                httpLog(sb.toString())
                httpLog("=======================================================================")
            }
        }
    }

    private fun notIgnore(sb: StringBuffer): Boolean {
        for (s in ignoreHost) {
            if (sb.contains(s)) {
                return false
            }
        }
        return true
    }

    fun httpLog(msg: String) {
        val msglist = msg.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (str in msglist) {
            val max_str_length = 2001
            var tempStr = str
            while (tempStr.length > max_str_length) {
                Log.d("httpLog","|| " + tempStr.substring(0, max_str_length))
                tempStr = str.substring(max_str_length)
            }
            Log.d("httpLog","|| $tempStr")
        }
    }
}