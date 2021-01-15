package com.zhc.common.api

data class Api1Response(
    val data1: Any?= null,
    val data2: Any? = null
)
data class TestResponse(
    val `data`: MutableList<Data>,
    val page: Int,
    val per_page: Int,
    val support: Support,
    val total: Int,
    val total_pages: Int
)

data class Data(
    val avatar: String,
    val email: String,
    val first_name: String,
    val id: Int,
    val last_name: String
)

data class Support(
    val text: String,
    val url: String
)
data class Api2Response(
    val data1: Any?= null,
    val data2: Any? = null
)

data class Api3Response(
    val data1: Any?= null,
    val data2: Any? = null
)

data class Api4Response(
    val data1: Any?= null,
    val data2: Any? = null
)
