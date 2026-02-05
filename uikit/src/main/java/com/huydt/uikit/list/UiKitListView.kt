package com.huydt.uikit.list

import com.huydt.uikit.list.domain.*
import com.huydt.uikit.list.components.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun <T> UiKitListView(
    modifier: Modifier = Modifier,
    repository: ListRepository<T>,
    viewModel: ListViewModel<T>? = null,
    itemKey: ((T) -> Any)? = null,
    emptyMessage: String = "Không tìm thấy dữ liệu",
    showLoadingCenter: Boolean = true,
    enableSelection: Boolean = true,
    autoLoad: Boolean = true,
    onSelectionChanged: ((List<T>) -> Unit)? = null,
    itemContent: @Composable (item: T, isSelected: Boolean, viewModel: ListViewModel<T>) -> Unit
) {
    val vm: ListViewModel<T> = viewModel ?: viewModel(
        key = repository.hashCode().toString(),
        factory = ListViewModelFactory(repository)
    )

    val items by vm.items.collectAsStateWithLifecycle()
    val status by vm.status.collectAsStateWithLifecycle()
    val selectedItems by vm.selectedItems.collectAsStateWithLifecycle()

    val isRefreshing = status is ListStatus.Refreshing
    val isLoadingMore = status is ListStatus.LoadingMore
    val isSelectionMode by remember { derivedStateOf { selectedItems.isNotEmpty() } }

    val listState = rememberLazyListState()
    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(selectedItems) { onSelectionChanged?.invoke(selectedItems.toList()) }

    // Sync Refresh UI (Fix icon trùng)
    if (pullToRefreshState.isRefreshing) { LaunchedEffect(Unit) { vm.refresh() } }
    LaunchedEffect(isRefreshing) {
        if (isRefreshing && items.isNotEmpty()) pullToRefreshState.startRefresh() 
        else pullToRefreshState.endRefresh()
    }

    // Load More Logic
    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val total = layoutInfo.totalItemsCount
            val lastIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1
            total > 0 && lastIndex >= (total - 2)
        }
    }
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && vm.canLoadMore && status is ListStatus.Idle) vm.loadMore()
    }

    Box(modifier = modifier.fillMaxSize().nestedScroll(pullToRefreshState.nestedScrollConnection)) {
        when {
            items.isEmpty() && isRefreshing && showLoadingCenter -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            items.isEmpty() && status is ListStatus.Idle -> {
                ListEmptyState(emptyMessage) { vm.refresh() }
            }
            status is ListStatus.Error -> {
                ListEmptyState((status as ListStatus.Error).message) { vm.refresh() }
            }
            else -> {
                LazyColumn(state = listState, modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 80.dp)) {
                    items(items, key = itemKey) { item ->
                        val isSelected = selectedItems.contains(item)
                        Box(modifier = Modifier.fillMaxWidth().pointerInput(enableSelection) {
                            if (!enableSelection) return@pointerInput
                            detectTapGestures(
                                onLongPress = { vm.toggleSelection(item) },
                                onTap = { if (isSelectionMode) vm.toggleSelection(item) }
                            )
                        }) {
                            itemContent(item, isSelected, vm)
                        }
                    }
                    if (isLoadingMore) item { LoadMoreIndicator() }
                }
            }
        }

        if (items.isNotEmpty() || pullToRefreshState.isRefreshing) {
            PullToRefreshContainer(modifier = Modifier.align(Alignment.TopCenter), state = pullToRefreshState)
        }
    }

    LaunchedEffect(repository) {
        if (autoLoad && items.isEmpty() && status is ListStatus.Idle) vm.load()
    }
}