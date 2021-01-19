package com.zhc.common.okHttp

import okhttp3.Interceptor
import okhttp3.Response

/**
 * 添加公共请求头
 */
class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request().newBuilder().apply {
            addHeader("header1", "headerValue1")

        }.build())
    }
}