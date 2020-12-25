package com.zhc.common

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter

object Router {
    object Pages{
        const val BIZ_ONE_BIZ_ONE_ACTIVITY = "/bizOne/activity/BizOneActivity"
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
        val nCallback = /*LoginNavigationCallback(postcardHolder, newTask)*/null
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