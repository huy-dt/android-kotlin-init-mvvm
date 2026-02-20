package com.huydt.uikit.list.data

import com.huydt.uikit.list.data.result.PagedResult

interface ListRepository<T> {

    suspend fun getItems(
        page: Int,
        pageSize: Int,
        query: String? = null
    ): List<T>

    suspend fun getPagedItems(
        page: Int,
        pageSize: Int,
        query: String? = null
    ): PagedResult<T>? = null

    suspend fun add(item: T)

    suspend fun remove(item: T)
}