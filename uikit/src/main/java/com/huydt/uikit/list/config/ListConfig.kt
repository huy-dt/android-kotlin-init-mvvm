package com.huydt.uikit.list.config

import com.huydt.uikit.list.data.SortOption

data class ListConfig(
    // Paging
    val pageSize: Int = 20,
    val enableRefresh: Bool = true,
    val enableLoadMore: Boolean = true,  // false = dùng pagination (prev/next)
    val clearOnRefresh: Boolean = true,

    // Selection
    val selectionMode: SelectionMode = SelectionMode.NONE,

    // Search
    val searchDebounceMs: Long = 500L,

    // Undo
    val enableUndo: Boolean = false,
    val undoTimeoutMs: Long = 4000L,

    // Sort / Filter
    val defaultSortOption: SortOption? = null,
)

typealias Bool = Boolean

enum class SelectionMode { NONE, SINGLE, MULTI }