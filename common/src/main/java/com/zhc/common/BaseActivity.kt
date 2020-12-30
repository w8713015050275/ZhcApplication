package com.zhc.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zhc.common.vm.BaseViewModel

abstract class BaseActivity<VM: BaseViewModel>: AppCompatActivity() {
    private lateinit var viewModel: VM

    /**
     * 布局ID
     */
    abstract fun getLayoutId(): Int

    /**
     *
     */
    abstract fun onViewCreated(savedInstanceState: Bundle?)

    /**
     * 创建ViewModule
     */
    abstract fun generateViewModel(): VM


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (getLayoutId() != 0) {
            setContentView(getLayoutId())
        }

        viewModel = generateViewModel()

        onViewCreated(savedInstanceState)
    }
}