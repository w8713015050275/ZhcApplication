package com.zhc.common.net

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class Repo private constructor() {

    companion object {
        @Volatile
        private var instance: Repo? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: Repo().apply {
                instance = this
            }
        }
    }

//    val api by lazy {
//        Retrofit.Builder()
//            .addConverterFactory(WrapperConverterFactory(Gson()))
//            .baseUrl(BaseConfig.AI_LESSON_HOST)
//            .client(getOkHttpClient())
//            .build()
//            .create(Api::class.java)
//    }

    fun getOkHttpClient(): OkHttpClient {

        var builder = OkHttpClient.Builder().apply {
//            dns(HttpDnsHelper.getInstance())
//            addInterceptor(HeaderInterceptor())
//            addInterceptor(RpcInfoInterceptor())
//            addNetworkInterceptor(HttpLoggingInterceptor(HttpLogger()).apply {
//                level = HttpLoggingInterceptor.Level.BODY
//            })
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(10, TimeUnit.SECONDS)
            writeTimeout(10, TimeUnit.SECONDS)
        }

        try {
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            val sslSocketFactory = sslContext.socketFactory
            builder.sslSocketFactory(sslSocketFactory)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
        builder.hostnameVerifier { hostname, session -> true }
        return builder.build()

    }

    private val trustAllCerts =
        arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(
                chain: Array<X509Certificate>,
                authType: String
            ) {
            }

            override fun checkServerTrusted(
                chain: Array<X509Certificate>,
                authType: String
            ) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }
        )
}