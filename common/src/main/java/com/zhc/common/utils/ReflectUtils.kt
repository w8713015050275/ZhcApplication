package com.zhc.common.utils

import java.lang.reflect.ParameterizedType

/**
 *
 * @ClassName: ReflectUtils
 * @Description:
 * @Author: Laughing
 * @CreateDate: 2018/12/24 13:30
 * @Version: 2.7.0
 */
object ReflectUtils {
    /**
     * 获取泛型的类
     */
    fun getTypeClass(myselfClass: Class<*>?, targetClass: Class<*>): Class<*>? {
        var temp = myselfClass
        var z: Class<*>? = null
        while (z == null && null != temp) {
            z = getInstancedGenericKClass(temp, targetClass)
            temp = temp.superclass
        }
        return z
    }

    private fun getInstancedGenericKClass(z: Class<*>?, targetClass: Class<*>): Class<*>? {
        z?.let {
            val type = it.genericSuperclass
            if (type is ParameterizedType) {
                val types = type.actualTypeArguments
                for (temp in types) {
                    if (temp is Class<*>) {
                        if (targetClass.isAssignableFrom(temp)) {
                            return temp
                        }
                    } else if (temp is ParameterizedType) {
                        val rawType = temp.rawType
                        if (rawType is Class<*> && targetClass.isAssignableFrom(rawType)) {
                            return rawType
                        }
                    }
                }
            }
        }
        return null
    }
}