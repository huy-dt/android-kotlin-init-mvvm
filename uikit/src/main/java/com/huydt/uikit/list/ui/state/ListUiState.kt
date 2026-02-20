package com.huydt.uikit.list.ui.state

data class ListUiState<T>(
    val items: List<T> = emptyList(),
    val pagingState: PagingState = PagingState.Init,
    val mutationState: MutationState = MutationState.Idle,
    val selectedItems: Set<T> = emptySet(),
    val canLoadMore: Boolean = true
) {
    val isRefreshing get() = pagingState is PagingState.Refreshing
    val isLoadingMore get() = pagingState is PagingState.LoadingMore
    val isDeleting get() = mutationState is MutationState.Deleting
    val isSelectionMode get() = selectedItems.isNotEmpty()
    val isAllSelected get() =
        items.isNotEmpty() && selectedItems.size == items.size
}