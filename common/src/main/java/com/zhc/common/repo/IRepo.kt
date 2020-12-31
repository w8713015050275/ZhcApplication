package com.zhc.common.repo

import com.zhc.common.api.Api1Response
import com.zhc.common.api.Api2Response
import com.zhc.common.api.Api3Response

/**
 * 可以访问网络api或本地数据的repo
 */
interface IRepo {

    suspend fun getData1(): Api1Response?

    suspend fun getData2(): Api2Response?

    suspend fun getData3(): Api3Response?
}