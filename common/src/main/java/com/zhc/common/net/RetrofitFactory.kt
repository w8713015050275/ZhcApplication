package com.zhc.common.net


import com.google.gson.Gson

import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * 配置Retrofit
 */
object RetrofitFactory {

    fun create(gson: Gson, okHttpClient: OkHttpClient, baseUrl: String): Retrofit {
        return Retrofit.Builder()
//                .addConverterFactory(WrapperConverterFactory(gson))
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build()
    }
}
