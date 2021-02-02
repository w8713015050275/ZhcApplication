package com.zhc.common.ui

import com.zhc.common.vm.BaseViewModel

/**
 * 不需要请求网络的
 * 默认BaseViewModel
 */
abstract class NoNetAccessFragmentDialog: BaseDialogFragment<BaseViewModel, Any>() {
    override fun generateViewModel(): BaseViewModel = BaseViewModel()

    fun invokeConfirmed() {
        super.invokeConfirmed(Any())
    }
}