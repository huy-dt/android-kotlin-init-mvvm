package com.huydt.uikit.list.config

enum class SelectionMode {
    NONE,
    SINGLE,
    MULTI
}

data class ListConfig(
    val pageSize: Int = 20,
    val enableRefresh: Boolean = true,
    val enableLoadMore: Boolean = true,
    val clearOnRefresh: Boolean = true,
    val selectionMode: SelectionMode = SelectionMode.NONE
)
