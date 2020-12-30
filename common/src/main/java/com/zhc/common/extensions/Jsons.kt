package com.zhc.common.extensions

import android.text.TextUtils
import com.google.gson.Gson
import org.json.JSONObject

/**
 *  Created by yetao on 2019-09-30
 *  description
 **/

val gson = Gson()

fun Any?.toJson(): String {
    this?.let {
        return gson.toJson(it)
    }
    return ""
}

fun <T> Class<T>.fromJson(json: String?): T? {
    if (TextUtils.isEmpty(json)) {
        return null
    }
    return gson.fromJson(json, this)
}

fun Any?.toJsonObject(): Any? {
    this?.let {
        return try {
            JSONObject(toJson())
        } catch (e: Exception) {
            this
        }
    }
    return null
}

