package com.zhc.common.vm

import androidx.lifecycle.LiveData

open class StatedLiveData<T> : LiveData<StatedData<T>>() {

    /**
     * Use this to put the Data on a LOADING Status
     */
    fun postLoading() {
        postValue(StatedData<T>().loading())
    }

    /**
     * Use this to put the Data on a ERROR DataStatus
     * @param throwable the error to be handled
     */
    fun postError(throwable: Throwable) {
        postValue(StatedData<T>().error(throwable))
    }

    /**
     * Use this to put the Data on a SUCCESS DataStatus
     * @param data
     */
    fun postSuccess(data: T) {
        postValue(StatedData<T>().success(data))
    }

    /**
     * Use this to put the Data on a COMPLETE DataStatus
     */
    fun postComplete() {
        postValue(StatedData<T>().complete())
    }


}