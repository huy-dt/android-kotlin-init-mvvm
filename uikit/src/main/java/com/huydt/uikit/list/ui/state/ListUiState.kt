package com.huydt.uikit.list.ui.state

import com.huydt.uikit.list.data.SortOption

data class ListUiState<T>(
    val items: List<T> = emptyList(),
    val selectedItems: Set<T> = emptySet(),
    val pagingState: PagingState = PagingState.Idle,
    val mutationState: MutationState = MutationState.Idle,
    val canLoadMore: Boolean = false,
    // Pagination (khi enableLoadMore = false)
    val currentPage: Int = 1,
    val totalPages: Int = 1,
    val totalCount: Int = 0,
    // Sort / Filter
    val sortOption: SortOption? = null,
    val filterOptions: Map<String, Any> = emptyMap(),
    // Undo
    val undoItem: T? = null,
    val undoAction: UndoAction = UndoAction.NONE,
    // Empty / Error message
    val emptyMessage: String? = null,
    val errorMessage: String? = null,
) {
    val isAllSelected: Boolean
        get() = items.isNotEmpty() && selectedItems.containsAll(items)

    val isAnySelected: Boolean
        get() = selectedItems.isNotEmpty()

    val selectedCount: Int
        get() = selectedItems.size

    val isLoading: Boolean
        get() = pagingState is PagingState.Refreshing

    val isLoadingMore: Boolean
        get() = pagingState is PagingState.LoadingMore

    val isMutating: Boolean
        get() = mutationState is MutationState.Loading

    val isIdle: Boolean
        get() = pagingState is PagingState.Idle && mutationState is MutationState.Idle

    val isEmpty: Boolean
        get() = items.isEmpty() && pagingState is PagingState.Idle

    val hasError: Boolean
        get() = pagingState is PagingState.Error || mutationState is MutationState.Error

    // Pagination helpers
    val hasPreviousPage: Boolean get() = currentPage > 1
    val hasNextPage: Boolean get() = currentPage < totalPages
}

enum class UndoAction { NONE, REMOVE, REMOVE_BATCH }