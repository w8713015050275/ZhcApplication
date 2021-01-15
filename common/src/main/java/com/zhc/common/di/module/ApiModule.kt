package com.zhc.common.di.module

import com.google.gson.Gson
import com.zhc.common.api.Api
import com.zhc.common.di.scope.AppScope
import com.zhc.common.net.RetrofitFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient

@Module
class ApiModule {
    @Provides
    @AppScope
    fun provideApi(gson: Gson, okHttpClient: OkHttpClient): Api {
        return RetrofitFactory.create(gson, okHttpClient, "https://reqres.in")
            .create(Api::class.java)
    }
}