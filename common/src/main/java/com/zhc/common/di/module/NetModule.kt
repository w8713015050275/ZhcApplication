package com.zhc.common.di.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.zhc.common.di.scope.AppScope
import com.zhc.common.okHttp.OkUtils
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient

@Module
class NetModule {
    @Provides
    @AppScope
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @AppScope
    fun provideOkHttpClient(): OkHttpClient {
        //使用统一的OkHttpClient
        return OkUtils.okHttpClient
    }
}