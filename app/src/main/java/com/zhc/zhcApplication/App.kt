package com.zhc.zhcApplication

import android.util.Log
import com.zhc.common.BaseApplication

private const val TAG = "zhc App"
class App(): BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: create shell app")
    }
}