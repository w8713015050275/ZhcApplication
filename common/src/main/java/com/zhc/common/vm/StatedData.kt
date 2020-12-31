package com.zhc.common.vm

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.zhc.common.exception.AccessThrowable
import com.zhc.common.exception.ERROR

class StatedData<T> {

    @NonNull
    @get:NonNull
    var state: DataState? = null
        private set

    @Nullable
    @get:Nullable
    var data: T? = null
        private set

    @Nullable
    @get:Nullable
    var error: Throwable? = null
        private set

    init {
        this.state = DataState.CREATED
        this.data = null
        this.error = null
    }

    fun loading(): StatedData<T> {
        this.state = DataState.LOADING
        this.data = null
        this.error = null
        return this
    }

    fun success(@NonNull data: T): StatedData<T> {
        this.state = DataState.SUCCESS
        this.data = data
        this.error = null
        return this
    }

    fun error(@NonNull error: Throwable): StatedData<T> {
        if (error is AccessThrowable && error.code == ERROR.ERROR_NETWORK) {
            this.state = DataState.NETWORK
        } else {
            this.state = DataState.ERROR
        }
        this.data = null
        this.error = error
        return this
    }

    fun complete(): StatedData<T> {
        this.state = DataState.COMPLETE
        return this
    }
}