package com.huydt.uikit.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huydt.uikit.list.config.ListConfig
import com.huydt.uikit.list.config.SelectionMode
import com.huydt.uikit.list.data.SortOption
import com.huydt.uikit.list.ui.state.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Tầng BASE: quản lý state, items, selection, toggle, undo, sort/filter.
 * KHÔNG phụ thuộc vào repository hay paging.
 */
open class ListViewModelBase<T>(
    val config: ListConfig = ListConfig()
) : ViewModel() {

    /* ── Scroll-to-top event ── */
    private val _scrollToTop = Channel<Unit>(Channel.BUFFERED)
    val scrollToTop = _scrollToTop.receiveAsFlow()

    /* ── One-time events ── */
    private val _events = Channel<ListEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    /* ── UI State ── */
    private val _uiState = MutableStateFlow(ListUiState<T>())
    val uiState: StateFlow<ListUiState<T>> = _uiState.asStateFlow()

    protected val currentState get() = _uiState.value

    private var undoJob: Job? = null

    /* =========================================================
     * Internal state helpers
     * ========================================================= */

    protected fun updateState(transform: ListUiState<T>.() -> ListUiState<T>) {
        _uiState.update { it.transform() }
    }

    protected fun sendScrollToTop() {
        viewModelScope.launch { _scrollToTop.send(Unit) }
    }

    protected fun sendEvent(event: ListEvent) {
        viewModelScope.launch { _events.send(event) }
    }

    /* =========================================================
     * Paging / Mutation state
     * ========================================================= */

    protected fun setPagingState(state: PagingState) =
        updateState { copy(pagingState = state) }

    protected fun setMutationState(state: MutationState) =
        updateState { copy(mutationState = state) }

    fun clearMutationError() =
        updateState { copy(mutationState = MutationState.Idle, errorMessage = null) }

    /* =========================================================
     * Items mutation API
     * ========================================================= */

    fun setItems(items: List<T>) =
        updateState { copy(items = items, emptyMessage = if (items.isEmpty()) emptyMessage else null) }

    fun addItem(item: T) =
        updateState { copy(items = items + item) }

    fun addItemAt(index: Int, item: T) =
        updateState {
            val mutable = items.toMutableList()
            mutable.add(index.coerceIn(0, items.size), item)
            copy(items = mutable)
        }

    fun addItems(newItems: List<T>) =
        updateState { copy(items = items + newItems) }

    fun updateItem(predicate: (T) -> Boolean, newItem: T) =
        updateState {
            copy(items = items.map { if (predicate(it)) newItem else it })
        }

    fun updateItems(transform: (List<T>) -> List<T>) =
        updateState { copy(items = transform(items)) }

    fun removeItem(item: T, withUndo: Boolean = config.enableUndo) {
        if (withUndo) {
            startUndo(item, UndoAction.REMOVE) { doRemoveItem(item) }
        } else {
            doRemoveItem(item)
        }
    }

    private fun doRemoveItem(item: T) {
        updateState {
            copy(
                items = items - item,
                selectedItems = selectedItems - item
            )
        }
        onItemsChanged()
    }

    fun removeItems(predicate: (T) -> Boolean, withUndo: Boolean = false) {
        val toRemove = currentState.items.filter(predicate)
        if (withUndo && toRemove.isNotEmpty()) {
            startUndo(toRemove.first(), UndoAction.REMOVE_BATCH) {
                doRemoveItems(predicate)
            }
        } else {
            doRemoveItems(predicate)
        }
    }

    private fun doRemoveItems(predicate: (T) -> Boolean) {
        updateState {
            val removed = items.filter(predicate).toSet()
            copy(
                items = items.filterNot(predicate),
                selectedItems = selectedItems - removed
            )
        }
        onItemsChanged()
    }

    fun clearItems() {
        updateState { copy(items = emptyList(), selectedItems = emptySet()) }
        onItemsChanged()
    }

    fun moveItem(fromIndex: Int, toIndex: Int) {
        val list = currentState.items.toMutableList()
        if (fromIndex !in list.indices || toIndex !in list.indices) return
        val item = list.removeAt(fromIndex)
        list.add(toIndex, item)
        updateState { copy(items = list) }
    }

    protected open fun onItemsChanged() = Unit

    /* =========================================================
     * Toggle helpers
     * ========================================================= */

    // FIX: không gọi updateItem(predicate, newItem) vì newItem cần giá trị cụ thể
    // Thay bằng updateItems trực tiếp để tránh lỗi infer
    fun toggleItem(predicate: (T) -> Boolean, transform: (T) -> T) =
        updateItems { list -> list.map { if (predicate(it)) transform(it) else it } }

    fun toggleAllItems(transform: (T) -> T) =
        updateItems { it.map(transform) }

    fun toggleItemWhere(predicate: (T) -> Boolean, transform: (T) -> T) =
        updateItems { list -> list.map { if (predicate(it)) transform(it) else it } }

    /* =========================================================
     * Selection API
     * ========================================================= */

    fun toggleSelectAll() {
        if (currentState.isAllSelected) clearSelection() else selectAll()
    }

    fun toggleSelection(item: T) {
        when (config.selectionMode) {
            SelectionMode.NONE -> return
            SelectionMode.SINGLE -> setSelection(
                if (currentState.selectedItems.contains(item)) emptySet()
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

    fun unselect(item: T) = withSelection { this - item }

    fun clearSelection() = setSelection(emptySet())

    fun selectAll() {
        if (config.selectionMode != SelectionMode.MULTI) return
        setSelection(currentState.items.toSet())
    }

    fun setSelection(items: Set<T>) =
        updateState { copy(selectedItems = items) }

    fun withSelection(transform: Set<T>.() -> Set<T>) =
        updateState { copy(selectedItems = selectedItems.transform()) }

    // FIX: dùng doRemoveItems trực tiếp với snapshot selected để tránh lỗi infer lambda
    open fun deleteSelected() {
        val selected = currentState.selectedItems.toSet()
        if (selected.isEmpty()) return
        updateState {
            copy(
                items = items.filterNot { it in selected },
                selectedItems = emptySet()
            )
        }
        onItemsChanged()
    }

    /* =========================================================
     * Undo API
     * ========================================================= */

    private fun startUndo(item: T, action: UndoAction, doAfterTimeout: () -> Unit) {
        undoJob?.cancel()
        updateState { copy(undoItem = item, undoAction = action) }
        sendEvent(ListEvent.ShowUndo(config.undoTimeoutMs))
        undoJob = viewModelScope.launch {
            delay(config.undoTimeoutMs)
            clearUndo()
            doAfterTimeout()
        }
    }

    fun undoRemove() {
        undoJob?.cancel()
        undoJob = null
        updateState { copy(undoItem = null, undoAction = UndoAction.NONE) }
        sendEvent(ListEvent.UndoDone)
    }

    private fun clearUndo() {
        updateState { copy(undoItem = null, undoAction = UndoAction.NONE) }
    }

    /* =========================================================
     * Sort API
     * ========================================================= */

    fun setSort(option: SortOption?) {
        updateState { copy(sortOption = option) }
        onSortChanged(option)
    }

    fun toggleSort(field: String) {
        val current = currentState.sortOption
        val new = if (current?.field == field) {
            current.copy(ascending = !current.ascending)
        } else {
            SortOption(field = field, ascending = true)
        }
        setSort(new)
    }

    open fun clearSort() = setSort(null)

    protected open fun onSortChanged(option: SortOption?) = Unit

    /* =========================================================
     * Filter API
     * ========================================================= */

    fun setFilter(key: String, value: Any?) {
        updateState {
            val newFilters = if (value == null) filterOptions - key
                            else filterOptions + (key to value)
            copy(filterOptions = newFilters)
        }
        onFilterChanged(currentState.filterOptions)
    }

    fun setFilters(filters: Map<String, Any>) {
        updateState { copy(filterOptions = filters) }
        onFilterChanged(filters)
    }

    fun clearFilters() {
        updateState { copy(filterOptions = emptyMap()) }
        onFilterChanged(emptyMap())
    }

    protected open fun onFilterChanged(filters: Map<String, Any>) = Unit

    /* =========================================================
     * Empty / Error message
     * ========================================================= */

    fun setEmptyMessage(msg: String?) = updateState { copy(emptyMessage = msg) }

    fun setErrorMessage(msg: String?) = updateState { copy(errorMessage = msg) }
}

/* ── One-time events ── */
sealed class ListEvent {
    data class ShowUndo(val durationMs: Long) : ListEvent()
    object UndoDone : ListEvent()
    data class ShowMessage(val message: String) : ListEvent()
}