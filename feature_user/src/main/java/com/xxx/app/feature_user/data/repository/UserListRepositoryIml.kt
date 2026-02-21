package com.xxx.app.feature_user.data.repository

import com.xxx.app.feature_user.data.datasource.UserListDataSource
import com.xxx.app.feature_user.domain.model.UserDto
import com.huydt.uikit.list.data.ListRepository
import com.huydt.uikit.list.data.SortOption
import com.huydt.uikit.list.data.result.PagedResult
import javax.inject.Inject

class UserListRepositoryImpl @Inject constructor(
    private val dataSource: UserListDataSource
) : ListRepository<UserDto> {

    override suspend fun getItems(
        page: Int,
        pageSize: Int,
        query: String?,
        sortOption: SortOption?,
        filters: Map<String, Any>,
    ): List<UserDto> {
        return dataSource.getItems(
            page = page,
            pageSize = pageSize,
            query = query,
            sortOption = sortOption,
            filters = filters,
        )
    }

    override suspend fun getPagedItems(
        page: Int,
        pageSize: Int,
        query: String?,
        sortOption: SortOption?,
        filters: Map<String, Any>,
    ): PagedResult<UserDto>? {
        val result = dataSource.getPagedItems(
            page = page,
            pageSize = pageSize,
            query = query,
            sortOption = sortOption,
            filters = filters,
        )
        android.util.Log.e("k", result.toString())
        return result
    }

    override suspend fun add(item: UserDto) {
        dataSource.add(item)
    }

    override suspend fun remove(item: UserDto) {
        dataSource.remove(item)
    }
}