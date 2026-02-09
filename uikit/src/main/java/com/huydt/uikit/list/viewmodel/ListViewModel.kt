package com.huydt.uikit.list.viewmodel

import com.huydt.uikit.list.config.*
import com.huydt.uikit.list.ui.state.*
import com.huydt.uikit.list.ui.swipe.*
import com.huydt.uikit.list.data.ListRepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

open class ListViewModel<T>(
    private val repository: ListRepository<T>,
    val config: ListConfig = ListConfig()
) : ViewModel() {

    /* ------------ State ------------ */
    private val _scrollToTop = Channel<Unit>(Channel.BUFFERED)
    val scrollToTop = _scrollToTop.receiveAsFlow()

    private val _uiState = MutableStateFlow(ListUiState<T>())
    val uiState: StateFlow<ListUiState<T>> = _uiState

    open fun swipeActions(item: T): SwipeActions<T> =
        SwipeActions()

    private var page = 0
    private var currentQuery: String? = null
    private var searchJob: Job? = null

    private var loadJob: Job? = null

    /* =========================================================
     * Selection API
     * ========================================================= */
    fun toggleSelectAll() {
        val state = _uiState.value

        if (state.isAllSelected) {
            clearSelection()
        } else {
            selectAll()
        }
    }

    fun toggleSelection(item: T) {
        when (config.selectionMode) {
            SelectionMode.NONE -> return
            SelectionMode.SINGLE -> setSelection(
                if (_uiState.value.selectedItems.contains(item)) emptySet()
                else setOf(item)
            )
            SelectionMode.MULTI -> withSelection {
                if (contains(item)) this - item else this + item
            }
        }
    }

    fun select(item: T) {
        when (config.selectionMode) {
            SelectionMode.NONE -> return
            SelectionMode.SINGLE -> setSelection(setOf(item))
            SelectionMode.MULTI -> withSelection { this + item }
        }
    }

    fun unselect(item: T) {
        withSelection { this - item }
    }

    fun clearSelection() {
        setSelection(emptySet())
    }

    fun selectAll() {
        if (config.selectionMode != SelectionMode.MULTI) return
        setSelection(_uiState.value.items.toSet())
    }

    protected fun setSelection(items: Set<T>) {
        _uiState.update { it.copy(selectedItems = items) }
    }

    protected fun withSelection(transform: Set<T>.() -> Set<T>) {
        _uiState.update {
            it.copy(selectedItems = it.selectedItems.transform())
        }
    }

    /* =========================================================
     * Data mutation API (CHO FEATURE VM DÙNG)
     * ========================================================= */

    protected fun setItems(items: List<T>) {
        _uiState.update { it.copy(items = items) }
    }

    protected fun addItem(item: T) {
        _uiState.update { it.copy(items = it.items + item) }
    }

    protected fun addItems(items: List<T>) {
        _uiState.update { it.copy(items = it.items + items) }
    }

    protected fun updateItem(predicate: (T) -> Boolean, newItem: T) {
        _uiState.update {
            it.copy(
                items = it.items.map { item ->
                    if (predicate(item)) newItem else item
                }
            )
        }
    }

    protected fun updateItems(transform: (List<T>) -> List<T>) {
        _uiState.update { it.copy(items = transform(it.items)) }
    }

    protected fun removeItem(item: T) {
        _uiState.update {
            it.copy(
                items = it.items - item,
                selectedItems = it.selectedItems - item
            )
        }
    }

    protected fun removeItems(predicate: (T) -> Boolean) {
        _uiState.update {
            val removed = it.items.filter(predicate).toSet()
            it.copy(
                items = it.items.filterNot(predicate),
                selectedItems = it.selectedItems - removed
            )
        }
    }

    protected fun clearItems() {
        _uiState.update {
            it.copy(
                items = emptyList(),
                selectedItems = emptySet()
            )
        }
    }

    /* =========================================================
     * Paging / Query
     * ========================================================= */

    fun refresh() {
        if (!config.enableRefresh) return
        
        // Nếu đang refresh rồi thì bỏ qua, nhưng nếu đang LoadMore thì sẽ Cancel LoadMore để Refresh
        if (_uiState.value.status is ListStatus.Refreshing) return

        // QUAN TRỌNG: Hủy bất kỳ tiến trình load nào đang chạy (bao gồm LoadMore)
        loadJob?.cancel()

        loadJob = viewModelScope.launch {
            _uiState.update {
                it.copy(status = ListStatus.Refreshing, selectedItems = emptySet())
            }
            resetPaging()

            if(config.clearOnRefresh)
                clearItems()

            try {
                val data = repository.getItems(page, config.pageSize, currentQuery)
                page++
                
                _uiState.update {
                    it.copy(
                        items = data,
                        status = ListStatus.Idle,
                        canLoadMore = config.enableLoadMore && data.size == config.pageSize
                    )
                }
                
                _scrollToTop.send(Unit) 

            } catch (e: Exception) {
                // Kiểm tra nếu lỗi không phải do bị Cancel Job chủ động
                _uiState.update {
                    it.copy(
                        status = ListStatus.Error(e.message ?: "Unknown Error"),
                        canLoadMore = false
                    )
                }
            }
        }
    }

    fun loadMore() {
        val state = _uiState.value
        // Chặn LoadMore nếu không rảnh (Status không phải Idle)
        if (!config.enableLoadMore || !state.canLoadMore || state.status !is ListStatus.Idle) return

        // Gán vào loadJob để refresh() có thể cancel nó
        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(status = ListStatus.LoadingMore) }
            try {
                val more = repository.getItems(page, config.pageSize, currentQuery)
                page++
                
                _uiState.update {
                    it.copy(
                        items = it.items + more,
                        status = ListStatus.Idle,
                        canLoadMore = more.size == config.pageSize
                    )
                }
            } catch (_: Exception) {
                _uiState.update { it.copy(status = ListStatus.Idle) }
            }
        }
    }

    fun search(query: String) {
        currentQuery = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            refresh()
        }
    }

    protected fun resetPaging() {
        page = 0
    }

    fun load() = refresh()
}
