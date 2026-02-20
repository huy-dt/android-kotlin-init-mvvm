package com.huydt.uikit.list.data.result

data class PagedResult<T>(
    val items: List<T>,
    val totalCount: Int? = null
)