package com.zhc.bizone.vm

import com.zhc.bizone.di.component.DaggerBizOneModuleComponent
import com.zhc.common.BaseApplication
import com.zhc.common.api.Api1Response
import com.zhc.common.repo.IRepo
import com.zhc.common.vm.BaseViewModel
import com.zhc.common.vm.StatedLiveData
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

    
    val loadData1 by lazy { StatedLiveData<Api1Response?>() }

    fun loadData1() {
        load {
            repo.getData1()
        }.result(
            success = {
                //liveData通知数据请求成功，可能为null
                loadData1.postSuccess(it)
            },
            error = {
                //liveData通知错误类型
                loadData1.postError(it)
            }

        )
    }
}