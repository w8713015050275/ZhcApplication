package com.zhc.common

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter

object Router {
    object Pages{
        object BizOneModule {
            const val BIZ_ONE_BIZ_ONE_ACTIVITY = "/bizOne/activity/BizOneActivity"
            const val BIZ_ONE_WEB_VIEW_ACTIVITY = "/bizOne/activity/WebViewActivity"
        }

        //不同module使用不同的group，不然找不到
        object LoginModule {
            const val ACTIVITY_LOGIN_NEW = "/loginModule/activity/LoginActivity"
        }
    }

    object Interceptor {
        const val LOGIN = "login"
    }
}

@JvmOverloads
fun Any?.launchActivity(path: String, requestCode: Int = -1, newTask: Boolean = false, postcardHolder: (Postcard.() -> Unit)? = null) {
    val context = when (this) {
        is Fragment -> this.activity as Context
        is Activity -> this
        else -> BaseApplication.instance
    }
    ARouter.getInstance().build(path).apply {
        val nCallback = LoginNavigationCallback(postcardHolder, newTask)
        if (requestCode == 0) {
            navigation(context, nCallback)
        } else {
            if (context is Activity) {
                navigation(context, requestCode, nCallback)
            } else {
                navigation(context, nCallback)
            }
        }
    }
}

@JvmOverloads
fun Any?.launchActivityGreenChannel(path: String, requestCode: Int = -1, newTask: Boolean = false, postcardHolder: (Postcard.() -> Unit)? = null) {
    val context = when (this) {
        is Fragment -> this.activity as Context
        is Activity -> this
        else -> BaseApplication.instance
    }
    //不使用拦截器，拦截登录
    ARouter.getInstance().build(path).greenChannel().apply {
        val nCallback = LoginNavigationCallback(postcardHolder, newTask)
        if (requestCode == 0) {
            navigation(context, nCallback)
        } else {
            if (context is Activity) {
                navigation(context, requestCode, nCallback)
            } else {
                navigation(context, nCallback)
            }
        }
    }
}