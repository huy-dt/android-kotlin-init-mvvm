package com.huydt.uikit.list.domain

interface ListRepository<T> {

    suspend fun getItems(
        page: Int,
        pageSize: Int,
        query: String? = null
    ): List<T>

    suspend fun add(item: T)

    suspend fun remove(item: T)
}
