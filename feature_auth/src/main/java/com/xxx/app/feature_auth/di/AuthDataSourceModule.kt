package com.xxx.app.feature_auth.di

import com.xxx.app.feature_auth.data.datasource.AuthDataSource
import com.xxx.app.feature_auth.data.datasource.AuthLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthDataSourceModule {

    @Binds
    abstract fun bindAuthDataSource(
        impl: AuthLocalDataSource
    ): AuthDataSource
}
