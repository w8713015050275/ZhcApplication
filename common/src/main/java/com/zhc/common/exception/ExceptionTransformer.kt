package com.zhc.common.exception

import android.database.sqlite.SQLiteException
import com.google.gson.JsonIOException
import com.google.gson.JsonParseException
//import com.zhangmen.braintrain.base.utils.Logger
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
import javax.net.ssl.SSLHandshakeException


object ExceptionTransformer {
    const val UNAUTHORIZED = 401
    private const val FORBIDDEN = 403
    private const val NOT_FOUND = 404
    private const val REQUEST_TIMEOUT = 408
    private const val INTERNAL_SERVER_ERROR = 500
    private const val BAD_GATEWAY = 502
    private const val SERVICE_UNAVAILABLE = 503
    private const val GATEWAY_TIMEOUT = 504


    fun error(e: Throwable): AccessThrowable{
//        Logger.d("request error $e")
        e.printStackTrace()
        return when (e) {
            is HttpException -> AccessThrowable(e.code(), when (e.code()) {
                UNAUTHORIZED -> "登录过期,请重新登录哦"
                FORBIDDEN,
                NOT_FOUND, REQUEST_TIMEOUT,
                GATEWAY_TIMEOUT, INTERNAL_SERVER_ERROR,
                BAD_GATEWAY, SERVICE_UNAVAILABLE -> "网络异常,请检查网络设置"
                else -> "网络错误"
            })
            is KotlinNullPointerException-> AccessThrowable(ERROR.DATA_IS_NULL,"数据异常")
            is ServerException -> AccessThrowable(e.code, e.message)
            is JsonParseException,
            is JSONException,
            is ParseException,
            is JsonIOException -> AccessThrowable(ERROR.ERROR_PARSE, "解析错误")
            is ConnectException,
            is SocketTimeoutException,
            is UnknownHostException -> AccessThrowable(ERROR.ERROR_NETWORK, "网络不给力")
            is SSLHandshakeException -> AccessThrowable(ERROR.ERROR_SSL, "证书验证失败")
            is SQLiteException -> AccessThrowable(ERROR.ERROR_SQL, "数据库出错")
            else -> {
//                Logger.e("未知错误：${e.message}")
                e.printStackTrace()
                AccessThrowable(ERROR.UNKNOWN, "未知错误")
            }
        }
    }
}


