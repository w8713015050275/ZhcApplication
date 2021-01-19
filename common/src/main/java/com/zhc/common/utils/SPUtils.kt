package com.zhc.common.utils

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.text.TextUtils
import android.util.Pair
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

object SPUtils {
    private var sp: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    /**
     * 初始化SPUtils
     *
     * @param spName spName
     */
    fun init(context: Context, spName: String?) {
        sp = context.applicationContext
            .getSharedPreferences(spName, Context.MODE_PRIVATE)
        editor = sp?.edit()
        editor?.apply()
    }

    /**
     * SP中写入String类型value
     *
     * @param key   键
     * @param value 值
     */
    fun put(key: String?, value: String?) {
        editor!!.putString(key, value).apply()
    }

    /**
     * SP中读取String
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值`null`
     */
    fun getString(key: String?): String? {
        return getString(key, null)
    }

    /**
     * SP中读取String
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值`defaultValue`
     */
    fun getString(key: String?, defaultValue: String?): String? {
        return sp!!.getString(key, defaultValue)
    }

    /**
     * SP中写入int类型value
     *
     * @param key   键
     * @param value 值
     */
    fun put(key: String?, value: Int) {
        editor!!.putInt(key, value).apply()
    }

    /**
     * SP中读取int
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    fun getInt(key: String?): Int {
        return getInt(key, -1)
    }

    /**
     * SP中读取int
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值`defaultValue`
     */
    fun getInt(key: String?, defaultValue: Int): Int {
        return sp!!.getInt(key, defaultValue)
    }

    /**
     * SP中写入long类型value
     *
     * @param key   键
     * @param value 值
     */
    fun put(key: String?, value: Long) {
        editor!!.putLong(key, value).apply()
    }

    /**
     * SP中读取long
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    fun getLong(key: String?): Long {
        return getLong(key, -1L)
    }

    /**
     * SP中读取long
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值`defaultValue`
     */
    fun getLong(key: String?, defaultValue: Long): Long {
        return sp!!.getLong(key, defaultValue)
    }

    /**
     * SP中写入float类型value
     *
     * @param key   键
     * @param value 值
     */
    fun put(key: String?, value: Float) {
        editor!!.putFloat(key, value).apply()
    }

    /**
     * 提交一个对象到sp中
     *
     * @param key    键
     * @param object 值
     */
    fun put(key: String?, `object`: Any?) {
        val value = Gson().toJson(`object`)
        put(key, value)
    }

    operator fun get(key: String?, typeToken: Type?): Any? {
        val value = getString(key)
        return if (TextUtils.isEmpty(value)) {
            null
        } else Gson().fromJson<Any>(value, typeToken)
    }

    /**
     * SP中读取float
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    fun getFloat(key: String?): Float {
        return getFloat(key, -1f)
    }

    /**
     * SP中读取float
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值`defaultValue`
     */
    fun getFloat(key: String?, defaultValue: Float): Float {
        return sp!!.getFloat(key, defaultValue)
    }

    /**
     * SP中写入boolean类型value
     *
     * @param key   键
     * @param value 值
     */
    fun put(key: String?, value: Boolean) {
        editor!!.putBoolean(key, value).apply()
    }

    /**
     * SP中读取boolean
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值`false`
     */
    fun getBoolean(key: String?): Boolean {
        return getBoolean(key, false)
    }

    /**
     * SP中读取boolean
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值`defaultValue`
     */
    fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
        return sp!!.getBoolean(key, defaultValue)
    }

    /**
     * SP中获取所有键值对
     *
     * @return Map对象
     */
    fun getAll(): Map<String?, *>? {
        return sp!!.all
    }

    /**
     * SP中移除该key
     *
     * @param key 键
     */
    fun remove(key: String?) {
        editor!!.remove(key).apply()
    }

    /**
     * SP中是否存在该key
     *
     * @param key 键
     * @return `true`: 存在<br></br>`false`: 不存在
     */
    operator fun contains(key: String?): Boolean {
        return sp!!.contains(key)
    }

    /**
     * SP中清除所有数据
     */
    fun clear() {
        editor!!.clear().apply()
    }


    fun putObjectData(key: String?, obj: Any?) {
        if (obj == null) {
            editor!!.putString(key, "").commit()
        } else {
            val gson = GsonBuilder()
                .registerTypeAdapter(Uri::class.java, UriSerializer())
                .create()
            val str = gson.toJson(obj, obj.javaClass)
            sp!!.edit().putString(key, str).commit()
        }
    }


    fun <T> getObjectData(key: String?, cls: Class<*>?): T? {
        return try {
            val gson = GsonBuilder()
                .registerTypeAdapter(Uri::class.java, UriDeserializer())
                .create()
            val data = getString(key, null)
            if (!TextUtils.isEmpty(data)) {
                gson.fromJson(data, cls) as T
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    class UriSerializer : JsonSerializer<Uri> {
        override fun serialize(
            src: Uri, typeOfSrc: Type,
            context: JsonSerializationContext
        ): JsonElement {
            return JsonPrimitive(src.toString())
        }
    }

    class UriDeserializer : JsonDeserializer<Uri> {
        @Throws(JsonParseException::class)
        override fun deserialize(
            src: JsonElement, srcType: Type,
            context: JsonDeserializationContext
        ): Uri {
            return Uri.parse(src.asString)
        }
    }

    fun <K, T> putMapData(
        key: String?,
        map: Map<K, T>?
    ) {
        val gson = Gson()
        val strJson = gson.toJson(map)
        editor!!.putString(key, strJson)
        editor!!.commit()
    }

    fun <K, T> putMap(key: String?, pair: Pair<K, T>) {
        val map: HashMap<K, T> = getMap(key)
        map[pair.first] = pair.second
        val gson = Gson()
        val strJson = gson.toJson(map)
        editor!!.putString(key, strJson)
        editor!!.commit()
    }

    fun <K, T> setMap(key: String?, map: Map<K, T>?) {
        if (map == null || map.isEmpty() || map.size < 1) {
            return
        }
        val gson = Gson()
        val strJson = gson.toJson(map)
        editor!!.putString(key, strJson)
        editor!!.commit()
    }

    fun <K, T> getMap(key: String?): HashMap<K, T> {
        var map = HashMap<K, T>()
        val strJson = sp!!.getString(key, null) ?: return map
        val gson = Gson()
        map = gson.fromJson(
            strJson,
            object : TypeToken<Map<K, T>?>() {}.type
        )
        return map
    }

    fun getMapData(key: String?): Map<String, String>? {
        return try {
            val data = getString(key)
            if (TextUtils.isEmpty(data)) return null
            val gson = Gson()
            val type = object :
                TypeToken<Map<String?, String?>?>() {}.type
            gson.fromJson<Map<String, String>>(data, type)
        } catch (e: Exception) {
            null
        }
    }
}