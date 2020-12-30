package com.zhc.bizone.di.component

import com.zhc.bizone.vm.BizOneActivityVm
import com.zhc.common.di.component.RepoComponent
import com.zhc.common.di.scope.ActivityScope
import dagger.Component

/**
 * 注入repo对象
 * 一般注入到vm中
 */
@Component(dependencies = [RepoComponent::class])
@ActivityScope
interface BizOneModuleComponent {
    fun inject(bizOneVm: BizOneActivityVm)
}