package com.zhc.common.di.module

import com.zhc.common.api.Api
import com.zhc.common.di.scope.AppScope
import com.zhc.common.repo.IRepo
import com.zhc.common.repo.Repo
import dagger.Module
import dagger.Provides

@Module
class RepoModule {

    @Provides
    @AppScope
    fun provideRepo(api: Api): IRepo {
        return Repo(api)
    }
}