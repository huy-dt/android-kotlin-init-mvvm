package com.xxx.app.feature_user.di

import com.xxx.app.feature_user.data.datasource.UserListDataSource
// import com.xxx.app.feature_user.data.datasource.UserListFakeDataSource
import com.xxx.app.feature_user.data.datasource.UserListFakeDataSourceWithError
import com.xxx.app.feature_user.data.repository.UserListRepositoryImpl
import com.huydt.uikit.list.data.ListRepository
import com.xxx.app.feature_user.domain.model.UserDto
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserListModule {

    @Binds
    @Singleton
    abstract fun bindUserListRepository(
        impl: UserListRepositoryImpl
    ): ListRepository<UserDto>

    @Binds
    @Singleton
    abstract fun bindUserListDataSource(
        impl: UserListFakeDataSourceWithError
    ): UserListDataSource
}
