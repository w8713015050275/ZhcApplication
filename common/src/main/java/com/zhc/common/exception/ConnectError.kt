package com.zhc.common.exception

data class ServerException(
        val code: Int,
        override val message: String): RuntimeException(message)

data class AccessThrowable(
        val code: Int,
        override val message: String): Throwable(message)

/**
 * 约定异常
 */
object ERROR {
    /**
     * 未知错误
     */
    const val UNKNOWN = 1000
    /**
     * 解析错误
     */
    const val ERROR_PARSE = 1001
    /**
     * 网络错误
     */
    const val ERROR_NETWORK = 1002
    /**
     * 协议出错
     */
    const val ERROR_HTTP = 1003
    /**
     * 证书出错
     */
    const val ERROR_SSL = 1005
    /**
     * 数据库出错
     */
    const val ERROR_SQL = 1006

    /**
     * 接口返回数据为null
     */
    const val DATA_IS_NULL = 1007

    /**
     * token过期
     */
    const val ERROR_TOKEN_EXPIRED = 11

    /**
     * app需升级
     */
    const val ERROR_NEED_UPDATE = 12
}