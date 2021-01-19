package com.zhc.bizone.net

import com.zhc.common.api.Path
import com.zhc.common.api.TestResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * module bizOne獨有api
 */
interface BizOneApi {

    @GET(Path.URL_1)
    suspend fun getData(@Query("page") query1: Int): TestResponse
}