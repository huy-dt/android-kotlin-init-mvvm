package com.huydt.uikit.list.config

data class ListConfig(
    val pageSize: Int = 20,
    val enableRefresh: Boolean = true,
    val enableLoadMore: Boolean = true,
    val selectionMode: SelectionMode = SelectionMode.NONE
)
