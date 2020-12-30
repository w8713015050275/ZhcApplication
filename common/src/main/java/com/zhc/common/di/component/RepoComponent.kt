package com.zhc.common.di.component

import com.zhc.common.di.module.ApiModule
import com.zhc.common.di.module.NetModule
import com.zhc.common.di.module.RepoModule
import com.zhc.common.di.scope.AppScope
import com.zhc.common.repo.IRepo
import dagger.Component

@Component(modules = [RepoModule::class, ApiModule::class, NetModule::class])
@AppScope
interface RepoComponent {
    fun repo(): IRepo
}