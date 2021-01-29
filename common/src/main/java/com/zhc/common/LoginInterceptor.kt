package com.zhc.common

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import com.zhc.common.utils.Logger
import com.zhc.common.utils.UserInfoProvider

/**
 * 必须拦截器所在的目录下面有依赖arouter-compiler，否则没法识别@Interceptor
 *
 */
private const val TAG = "LoginInterceptor zhc==="
@Interceptor(name = Router.Interceptor.LOGIN, priority = 1)
class LoginInterceptor : IInterceptor {

    override fun process(postcard: Postcard, callback: InterceptorCallback) {
        val path = postcard.path
        Log.d(TAG, "path---> $path")
        if (UserInfoProvider.isLogin()) {
            //已经登录
            callback.onContinue(postcard)
        } else {
            //未登录，需要拦截，如果未登录也不用拦截，直接使用绿色通道方法greenchannel
            val bundle = postcard.extras

            val manager :ActivityManager = BaseApplication.instance.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager;
            val list = manager.getRunningTasks(1)
            if (list.size>0) {
                val name = list.get(0).topActivity!!.className
                Log.d(TAG, "path---> $name")
                //防止重复打开
                if (!name.contains("OneButtonLoginActivity")&& !name.contains("OneLoginActivity")&& !name.contains("LoginAuthActivity")
                        && !name.contains("login", true)) {
                    Log.d(TAG, "path---> goto login")
                    launchActivityGreenChannel(Router.Pages.LoginModule.ACTIVITY_LOGIN_NEW) {
                        with(bundle)
//                        withString(BaseConstants.KEY_FROM_ROUTE, path)
                        greenChannel()
                    }
                }
            }

            callback.onInterrupt(null)
        }
    }

    override fun init(context: Context?) {
        Logger.d("LoginInterceptor init")
    }
}


class LoginNavigationCallback(var postcardHolder: (Postcard.() -> Unit)? = null, val newTask: Boolean = false) : NavigationCallback {
    override fun onLost(postcard: Postcard) {
        Logger.d("login onLost$postcard")
    }

    override fun onFound(postcard: Postcard) {
        Logger.d("login onFound$postcard")
        if (newTask) {
            postcard.withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        postcardHolder?.let { postcard.also(it) }
    }

    override fun onInterrupt(postcard: Postcard) {
        Logger.d("login onInterrupt$postcard")
    }

    override fun onArrival(postcard: Postcard) {
        Logger.d("login onArrival$postcard")
    }
}