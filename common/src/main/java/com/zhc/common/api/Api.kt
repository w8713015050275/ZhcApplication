package com.zhc.common.api

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * 定义网络接口
 */
interface Api {

    @GET(Path.URL_1)
    suspend fun getData(@Query("page") query1: Int): TestResponse

//    @GET(Path.URL_1)
//    suspend fun getData(@Header("header1") header1: Int, @Query("query1") query1: Int): Api1Response

    @POST(Path.URL_2)
    suspend fun getData2(@Body request: Api1Request): Api2Response

//    val requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file)
//    val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
//    val updateAvatarDTO = api.upload(body)
    @Multipart
    @POST(Path.URL_3)
    suspend fun upload(@Part file: MultipartBody.Part): Api3Response

    @GET(Path.URL_4)
    suspend fun getData3(@QueryMap map: Map<String, Any>): Api4Response

}