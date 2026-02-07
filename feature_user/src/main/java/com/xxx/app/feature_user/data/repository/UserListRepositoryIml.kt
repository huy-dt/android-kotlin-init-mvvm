package com.xxx.app.feature_user.data.repository

import com.xxx.app.feature_user.data.datasource.UserListDataSource
import com.xxx.app.feature_user.domain.model.UserDto
import com.huydt.uikit.list.data.ListRepository
import javax.inject.Inject

class UserListRepositoryImpl @Inject constructor(
    private val dataSource: UserListDataSource
) : ListRepository<UserDto> {

    override suspend fun getItems(
        page: Int,
        pageSize: Int,
        query: String?
    ): List<UserDto> {
        return dataSource.getItems(
            page = page,
            pageSize = pageSize,
            query = query
        )
    }

    override suspend fun add(item: UserDto) {
        dataSource.add(item)
    }

    override suspend fun remove(item: UserDto) {
        dataSource.remove(item)
    }
}
