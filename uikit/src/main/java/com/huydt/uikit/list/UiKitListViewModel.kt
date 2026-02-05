package com.huydt.uikit.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UiKitListViewModel<T>(
    private val repository: UiKitListRepository<T>
) : ViewModel() {

    private val _items = MutableStateFlow<List<T>>(emptyList())
    val items: StateFlow<List<T>> = _items

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore

    var canRefresh: Boolean = true
    var canLoadMore: Boolean = true

    private var page = 0
    private val pageSize = 20
    private var currentQuery: String? = null

    fun load() {
        refresh()
    }

    fun refresh() {
        if (!canRefresh || _isRefreshing.value) return

        viewModelScope.launch {
            _isRefreshing.value = true
            page = 0

            val data = repository.getItems(
                page = page,
                pageSize = pageSize,
                query = currentQuery
            )

            _items.value = data
            page++

            canLoadMore = data.size == pageSize
            _isRefreshing.value = false
        }
    }

    fun loadMore() {
        if (!canLoadMore || _isLoadingMore.value) return

        viewModelScope.launch {
            _isLoadingMore.value = true

            val more = repository.getItems(
                page = page,
                pageSize = pageSize,
                query = currentQuery
            )

            _items.value = _items.value + more
            page++

            canLoadMore = more.size == pageSize
            _isLoadingMore.value = false
        }
    }

    fun search(query: String) {
        currentQuery = query
        refresh()
    }

    fun add(item: T) {
        viewModelScope.launch {
            repository.add(item)
            _items.value = listOf(item) + _items.value
        }
    }

    fun remove(item: T) {
        viewModelScope.launch {
            repository.remove(item)
            _items.value = _items.value - item
        }
    }
}
