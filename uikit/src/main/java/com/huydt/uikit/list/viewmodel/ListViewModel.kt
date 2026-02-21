package com.huydt.uikit.list.viewmodel

import androidx.lifecycle.viewModelScope
import com.huydt.uikit.list.config.ListConfig
import com.huydt.uikit.list.data.ListRepository
import com.huydt.uikit.list.ui.swipe.SwipeActions
import com.huydt.uikit.list.data.SortOption
import com.huydt.uikit.list.ui.state.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Tầng PAGING: kế thừa Base, thêm load/loadMore/pagination/search.
 *
 * Hai chế độ:
 *  - config.enableLoadMore = true  → infinite scroll (load more)
 *  - config.enableLoadMore = false → page-based pagination (prev / next / goTo)
 */
open class ListViewModel<T>(
    private val repository: ListRepository<T>,
    config: ListConfig = ListConfig()
) : ListViewModelBase<T>(config) {

    /** Override trong subclass để cung cấp swipe actions cho từng item */
    open fun swipeActions(item: T): SwipeActions = SwipeActions()

    /**
     * Gọi từ UI khi user tap vào swipe action button.
     * Override trong subclass để xử lý từng actionId.
     */
    open fun onSwipeActionClick(item: T, actionId: String) = Unit

    private var page = 0
    private var runtimeTotalCount: Int? = null
    private var runtimeTotalPages: Int? = null
    private var currentQuery: String? = null
    private var searchJob: Job? = null
    private var loadJob: Job? = null

    /* ── Hooks từ Base ── */
    override fun onItemsChanged() = tryLoadIfEmpty()
    override fun onSortChanged(option: SortOption?) = refresh()
    override fun onFilterChanged(filters: Map<String, Any>) = refresh()

    private fun tryLoadIfEmpty() {
        val state = currentState
        if (state.items.isEmpty() &&
            (state.canLoadMore || !config.enableLoadMore) &&
            state.pagingState is PagingState.Idle &&
            state.mutationState is MutationState.Idle
        ) refresh()
    }

    /* =========================================================
     * Infinite scroll mode
     * ========================================================= */

    fun refresh() {
        if (!config.enableRefresh) return
        if (currentState.pagingState is PagingState.Refreshing) return

        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            updateState {
                copy(
                    pagingState = PagingState.Refreshing,
                    selectedItems = emptySet(),
                    errorMessage = null
                )
            }

            page = 0
            runtimeTotalCount = null
            runtimeTotalPages = null

            if (config.clearOnRefresh) clearItems()

            try {
                val result = fetchPage(page)
                if (result.items.isNotEmpty()) page++

                updateState {
                    val canLoad = resolveCanLoadMore(result.items, result.totalCount)
                    copy(
                        items = result.items,
                        pagingState = PagingState.Idle,
                        canLoadMore = canLoad,
                        currentPage = 1,
                        totalPages = result.totalPages ?: 1,
                        totalCount = result.totalCount ?: 0,
                        emptyMessage = if (result.items.isEmpty()) emptyMessage else null
                    )
                }
                sendScrollToTop()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                updateState {
                    copy(
                        pagingState = PagingState.Error(e.message ?: "Unknown Error"),
                        canLoadMore = false,
                        errorMessage = e.message
                    )
                }
            }
        }
    }

    fun loadMore() {
        if (!config.enableLoadMore) return
        val state = currentState
        if (!state.canLoadMore ||
            state.pagingState !is PagingState.Idle ||
            state.mutationState !is MutationState.Idle
        ) return

        loadJob = viewModelScope.launch {
            setPagingState(PagingState.LoadingMore)
            try {
                val result = fetchPage(page)
                if (result.items.isNotEmpty()) page++

                updateState {
                    val newItems = items + result.items
                    val canLoad = resolveCanLoadMore(result.items, result.totalCount, newItems.size)
                    copy(
                        items = newItems,
                        pagingState = PagingState.Idle,
                        canLoadMore = canLoad,
                        totalCount = result.totalCount ?: totalCount
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
                setPagingState(PagingState.Idle)
            }
        }
    }

    /* =========================================================
     * Pagination mode (enableLoadMore = false)
     * ========================================================= */

    fun goToPage(targetPage: Int) {
        if (config.enableLoadMore) return
        val total = runtimeTotalPages ?: currentState.totalPages
        if (targetPage < 1 || targetPage > total) return
        if (currentState.pagingState !is PagingState.Idle) return

        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            setPagingState(PagingState.Refreshing)
            try {
                val result = fetchPage(targetPage - 1) // 0-based

                updateState {
                    copy(
                        items = result.items,
                        pagingState = PagingState.Idle,
                        currentPage = targetPage,
                        totalPages = result.totalPages ?: totalPages,
                        totalCount = result.totalCount ?: totalCount,
                        canLoadMore = false,
                        selectedItems = emptySet()
                    )
                }
                sendScrollToTop()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                updateState {
                    copy(
                        pagingState = PagingState.Error(e.message ?: "Unknown Error"),
                        errorMessage = e.message
                    )
                }
            }
        }
    }

    fun nextPage() = goToPage(currentState.currentPage + 1)
    fun previousPage() = goToPage(currentState.currentPage - 1)
    fun firstPage() = goToPage(1)
    fun lastPage() = goToPage(runtimeTotalPages ?: currentState.totalPages)

    /* =========================================================
     * Search
     * ========================================================= */

    fun search(query: String) {
        val trimmed = query.trim()
        if (trimmed == currentQuery) return
        currentQuery = trimmed
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(config.searchDebounceMs)
            refresh()
        }
    }

    open fun clearSearch() {
        if (currentQuery == null) return
        currentQuery = null
        searchJob?.cancel()
        refresh()
    }

    /* =========================================================
     * Optimistic update
     * ========================================================= */

    suspend fun optimisticRemove(item: T, block: suspend () -> Unit) {
        updateState {
            copy(items = items - item, selectedItems = selectedItems - item)
        }
        try {
            block()
        } catch (e: Exception) {
            addItem(item) // rollback
            sendEvent(ListEvent.ShowMessage("Xóa thất bại: ${e.message}"))
        }
    }

    /* =========================================================
     * Convenience aliases
     * ========================================================= */

    fun load() = refresh()
    fun reload() = refresh()

    /* =========================================================
     * Private helpers
     * ========================================================= */

    private data class FetchResult<T>(
        val items: List<T>,
        val totalCount: Int?,
        val totalPages: Int?,
    )

    private suspend fun fetchPage(zeroBasedPage: Int): FetchResult<T> {
        val state = currentState
        val paged = repository.getPagedItems(
            page = zeroBasedPage,
            pageSize = config.pageSize,
            query = currentQuery,
            sortOption = state.sortOption,   // đã dùng com.huydt.uikit.list.data.SortOption
            filters = state.filterOptions,
        )

        return if (paged != null) {
            runtimeTotalCount = paged.totalCount
            val totalPg = if (paged.totalPages > 0) paged.totalPages
                          else calculateTotalPages(paged.totalCount, config.pageSize)
            runtimeTotalPages = totalPg
            FetchResult(paged.items, paged.totalCount, totalPg)
        } else {
            val items = repository.getItems(
                page = zeroBasedPage,
                pageSize = config.pageSize,
                query = currentQuery,
                sortOption = state.sortOption,
                filters = state.filterOptions,
            )
            FetchResult(items, null, null)
        }
    }

    private fun resolveCanLoadMore(
        latestBatch: List<T>,
        totalCount: Int?,
        currentSize: Int = latestBatch.size,
    ): Boolean {
        if (!config.enableLoadMore) return false
        return if (totalCount != null) currentSize < totalCount
               else latestBatch.size == config.pageSize
    }

    private fun calculateTotalPages(totalCount: Int, pageSize: Int): Int {
        if (pageSize <= 0) return 1
        return (totalCount + pageSize - 1) / pageSize
    }
}