package com.zhc.bizone.vm

import com.zhc.bizone.di.component.DaggerBizOneModuleComponent
import com.zhc.common.BaseApplication
import com.zhc.common.repo.IRepo
import com.zhc.common.vm.BaseViewModel
import javax.inject.Inject

/**
 * BizOneActivity关联的Vm
 */
class BizOneActivityVm : BaseViewModel() {

    @Inject
    lateinit var repo: IRepo

    init {
        DaggerBizOneModuleComponent.builder()
            .repoComponent(BaseApplication.instance.repoComponent)
            .build().inject(this)
    }


}