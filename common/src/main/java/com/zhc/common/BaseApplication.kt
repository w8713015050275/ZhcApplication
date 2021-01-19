package com.zhc.common

import android.app.Application
import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import androidx.multidex.MultiDex
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.AppUtils
import com.zhc.common.di.component.DaggerRepoComponent
import com.zhc.common.di.component.RepoComponent
import com.zhc.common.di.module.ApiModule
import com.zhc.common.di.module.NetModule
import com.zhc.common.di.module.RepoModule
import com.zhc.common.utils.SPUtils
import kotlin.properties.Delegates

open class BaseApplication:Application() {

    lateinit var repoComponent: RepoComponent

    companion object {
        var instance: BaseApplication by Delegates.notNull()
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun registerActivityLifecycleCallbacks(callback: ActivityLifecycleCallbacks?) {
        super.registerActivityLifecycleCallbacks(callback)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)

    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        SPUtils.init(this, AppUtils.getAppPackageName())
        ARouter.init(this)
        initRepoComponent()
    }

    private fun initRepoComponent() {
        repoComponent = DaggerRepoComponent.builder()
            .apiModule(ApiModule())
            .netModule(NetModule())
            .repoModule(RepoModule())
            .build()
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