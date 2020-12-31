package com.zhc.common.repo

import com.zhc.common.api.Api
import com.zhc.common.api.Api1Response
import com.zhc.common.api.Api2Response
import com.zhc.common.api.Api3Response
import com.zhc.common.exception.ExceptionTransformer

/**
 * 可以访问网络api或本地数据的repo
 */
class Repo(val api: Api): IRepo {

    /**
     * 网络请求过程中发生的异常会全部被拦截
     */
    override suspend fun getData1(): Api1Response? {
        return try {
            //发生异常时，转换为内部错误码
            return api.getData(header1 = 1, query1 = 2)
        } catch (e: KotlinNullPointerException) {
            null
        } catch (e: Exception) {
            throw ExceptionTransformer.error(e)
        }
    }

    override suspend fun getData2(): Api2Response? {
        return Api2Response()
    }

    override suspend fun getData3(): Api3Response? {
        return Api3Response()
    }
}