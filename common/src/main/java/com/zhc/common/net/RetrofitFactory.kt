package com.zhc.common.net


import com.google.gson.Gson

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 配置Retrofit
 */
object RetrofitFactory {

    //不要重复创建okHttpClient和Retrofit
    // 不同baseUrl不同retrofit实例
    fun create(gson: Gson, okHttpClient: OkHttpClient, baseUrl: String): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .client(okHttpClient) //可以用同一个OkHttpClient，同一个Gson
                .build()
    }
}
