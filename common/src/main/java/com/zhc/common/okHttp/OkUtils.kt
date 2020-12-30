package com.zhc.common.okHttp

import okhttp3.OkHttpClient

object OkUtils {

    @JvmStatic
    var okHttpClient: OkHttpClient = okHttpClient()

    private fun okHttpClient(): OkHttpClient {
        return SSLUtil.getInstance().sslContext
    }
}