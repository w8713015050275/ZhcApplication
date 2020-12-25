package com.zhc.common

import android.app.Application
import android.content.ComponentCallbacks
import android.content.SharedPreferences
import android.content.res.Configuration
import com.alibaba.android.arouter.launcher.ARouter
import kotlin.properties.Delegates

open class BaseApplication:Application() {

    companion object {
        var instance: BaseApplication by Delegates.notNull()
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun registerActivityLifecycleCallbacks(callback: ActivityLifecycleCallbacks?) {
        super.registerActivityLifecycleCallbacks(callback)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        ARouter.init(this)
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun unregisterOnProvideAssistDataListener(callback: OnProvideAssistDataListener?) {
        super.unregisterOnProvideAssistDataListener(callback)
    }

    override fun unregisterComponentCallbacks(callback: ComponentCallbacks?) {
        super.unregisterComponentCallbacks(callback)
    }

    override fun unregisterActivityLifecycleCallbacks(callback: ActivityLifecycleCallbacks?) {
        super.unregisterActivityLifecycleCallbacks(callback)
    }

    override fun registerComponentCallbacks(callback: ComponentCallbacks?) {
        super.registerComponentCallbacks(callback)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
    }

    override fun registerOnProvideAssistDataListener(callback: OnProvideAssistDataListener?) {
        super.registerOnProvideAssistDataListener(callback)
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}