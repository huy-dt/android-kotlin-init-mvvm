package com.huydt.uikit.list.ui.state

data class ListUiState<T>(
    val items: List<T> = emptyList(),
    val status: ListStatus = ListStatus.Init,
    val selectedItems: Set<T> = emptySet(),
    val canLoadMore: Boolean = true
) {
    val isRefreshing get() = status is ListStatus.Refreshing
    val isLoadingMore get() = status is ListStatus.LoadingMore
    val isSelectionMode get() = selectedItems.isNotEmpty()
}
