package com.zhc.common.repo

import com.zhc.common.api.Api
import com.zhc.common.api.Api1Response
import com.zhc.common.api.Api2Response
import com.zhc.common.api.Api3Response

/**
 * 可以访问网络api或本地数据的repo
 */
class Repo(val api: Api): IRepo {

    override suspend fun getData1(): Api1Response {
        return api.getData(header1 = 1, query1 = 2)
    }

    override suspend fun getData2(): Api2Response {
        return Api2Response()
    }

    override suspend fun getData3(): Api3Response {
        return Api3Response()
    }
}