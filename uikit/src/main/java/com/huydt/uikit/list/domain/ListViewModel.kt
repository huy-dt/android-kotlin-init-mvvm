package com.huydt.uikit.list.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class ListViewModel<T>(
    private val repository: ListRepository<T>
) : ViewModel() {

    private val _items = MutableStateFlow<List<T>>(emptyList())
    val items: StateFlow<List<T>> = _items

    private val _status = MutableStateFlow<ListStatus>(ListStatus.Idle)
    val status: StateFlow<ListStatus> = _status

    private val _selectedItems = MutableStateFlow<Set<T>>(emptySet())
    val selectedItems: StateFlow<Set<T>> = _selectedItems

    var canLoadMore: Boolean = true
    private var page = 0
    private val pageSize = 20
    private var currentQuery: String? = null
    private var searchJob: Job? = null

    // --- Selection Logic ---
    fun toggleSelection(item: T) {
        val current = _selectedItems.value
        _selectedItems.value = if (current.contains(item)) current - item else current + item
    }

    fun selectAll() {
        _selectedItems.value = _items.value.toSet()
    }

    fun clearSelection() {
        _selectedItems.value = emptySet()
    }

    // --- Data Logic ---
    fun refresh() {
        if (_status.value is ListStatus.Refreshing || _status.value is ListStatus.LoadingMore) return
        viewModelScope.launch {
            _status.value = ListStatus.Refreshing
            clearSelection()
            page = 0
            try {
                val data = repository.getItems(page, pageSize, currentQuery)
                _items.value = data
                page++
                canLoadMore = data.size == pageSize
                _status.value = ListStatus.Idle
            } catch (e: Exception) {
                _status.value = ListStatus.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun loadMore() {
        if (!canLoadMore || _status.value !is ListStatus.Idle) return
        viewModelScope.launch {
            _status.value = ListStatus.LoadingMore
            try {
                val more = repository.getItems(page, pageSize, currentQuery)
                _items.value = _items.value + more
                page++
                canLoadMore = more.size == pageSize
                _status.value = ListStatus.Idle
            } catch (e: Exception) {
                _status.value = ListStatus.Idle
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

    fun remove(item: T) {
        viewModelScope.launch {
            repository.remove(item)
            _items.value = _items.value - item
            _selectedItems.value = _selectedItems.value - item
        }
    }

    fun load() = refresh()
}