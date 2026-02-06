package com.huydt.uikit.list.ui.swipe

data class SwipeActions<T>(
    val start: List<SwipeAction<T>> = emptyList(),
    val end: List<SwipeAction<T>> = emptyList()
) {
    val enabled: Boolean
        get() = start.isNotEmpty() || end.isNotEmpty()
}
