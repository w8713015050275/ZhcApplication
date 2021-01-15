package com.zhc.common.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhc.common.exception.AccessThrowable
import com.zhc.common.exception.ERROR
import com.zhc.common.exception.ExceptionTransformer
import com.zhc.common.executors.Executor.dispatchers
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

open class BaseViewModel: ViewModel() {

    //数据加载中
    protected val _loading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val loading: LiveData<Boolean>
        get() = _loading

    //各种错误监听
    protected val _errorToast: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val errorToast: LiveData<String>
        get() = _errorToast

    //成功
    protected val _successToast: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val successToast: LiveData<String>
        get() = _successToast

    //token过期
    private val _reLogin: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val reLogin: LiveData<Boolean>
        get() = _reLogin

    //网络
    private val _networkError: MutableLiveData<AccessThrowable> by lazy { MutableLiveData<AccessThrowable>() }
    val networkError: LiveData<AccessThrowable>
        get() = _networkError


    /**
     * load产生的异常如果是AccessThrowable都会被result方法拦截掉
     */
    fun <T> load(loader: suspend () -> T): Deferred<T> {
        return viewModelScope.async(dispatchers.io) {
            loader()
        }
    }

    /**
     * 等待数据返回，并分发
     */
    fun <T> Deferred<T>.result(
        success: (T) -> Unit,
        error: (AccessThrowable) -> Unit = { _errorToast.value = it.message },
        complete: () -> Unit = {},
        showLoading: Boolean = true,
        escapeHideSpinner: Boolean = false) {
        if (isActive) viewModelScope.launch {
            try {
                if (showLoading) {
                    _loading.value = true
                }
                //接口200返回成功，不一定有数据
                success(this@result.await())
            } catch (err: AccessThrowable) {
                when (err.code) {
                    ERROR.ERROR_NETWORK -> {
                        _networkError.postValue(err)
                        // 2020年1月20日 19:48:59 无网络连接需要分发出去
                        error(err)
                    }
                    ERROR.ERROR_TOKEN_EXPIRED, ExceptionTransformer.UNAUTHORIZED -> {
                        //重新登录处理
//                        if (reLogin.hasActiveObservers()) {
//                            _reLogin.postValue(true)
//                        } else {
//                            Router.startActivity(Router.Pages.ACTIVITY_LOGIN_NEW)
//                        }
//                        val userInfo = UserInfoProvider.userInfo
//                        if (userInfo?.mobile != null) {
//                            PushManager.getInstance(BaseApplication.instance).unBindPushMobile(userInfo.mobile)
//                        }
                    }
                    else -> error(err)
                }
            } catch (err: Exception) {
                error(AccessThrowable(ERROR.UNKNOWN, err.message ?: ""))
            } finally {
                if (showLoading && !escapeHideSpinner) {
                    _loading.value = false
                }
                complete.invoke()
            }
        }
    }


}