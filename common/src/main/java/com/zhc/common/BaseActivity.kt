package com.zhc.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.blankj.utilcode.util.ToastUtils
import com.zhc.common.utils.ReflectUtils
import com.zhc.common.vm.BaseViewModel

abstract class BaseActivity<VM: BaseViewModel>: AppCompatActivity() {
    lateinit var viewModel: VM

    /**
     * 布局ID
     */
    abstract fun getLayoutId(): Int

    /**
     *
     */
    abstract fun onViewCreated(savedInstanceState: Bundle?)

    /**
     * 是否显示数据加载动画
     */
    open fun enableLoading(): Boolean {
        return true
    }

    /**
     * 创建ViewModule
     */
    open fun generateViewModel(): VM {
        val clz: Class<VM> = ReflectUtils.getTypeClass(javaClass, BaseViewModel::class.java) as Class<VM>
        //绑定activity的生命周期
        return ViewModelProviders.of(this).get(clz)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (getLayoutId() != 0) {
            setContentView(getLayoutId())
        }

        viewModel = generateViewModel()

        //监听数据loading
        if (enableLoading()) {
            viewModel.loading.observe(this, Observer {
                when (it) {
                    true -> ToastUtils.showShort("begin loading")
                    false -> ToastUtils.showShort("end loading")
                }
            })
        }

        viewModel.successToast.observe(this, Observer {
            ToastUtils.showShort("successToast")
        })

        viewModel.errorToast.observe(this, Observer {
            ToastUtils.showShort("errorToast")
        })

        viewModel.networkError.observe(this, Observer {
//            it?.message?.let { message ->
//                ToastUtils.showError(message)
//            }
//            hideLoading()
        })

        viewModel.reLogin.observe(this, Observer {
//            launchActivity(Router.Pages.ACTIVITY_LOGIN_NEW)
        })

        onViewCreated(savedInstanceState)
    }
}