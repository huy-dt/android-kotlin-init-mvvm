package com.huydt.uikit.list.ui

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
import com.huydt.uikit.list.config.*
import com.huydt.uikit.list.ui.state.*
import com.huydt.uikit.list.viewmodel.*
import com.huydt.uikit.list.ui.components.*
import com.huydt.uikit.list.ui.swipe.SwipeCoordinator
import com.huydt.uikit.list.data.ListRepository

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun <T> UiKitListView(
    modifier: Modifier = Modifier,
    repository: ListRepository<T>,
    viewModel: ListViewModel<T>? = null,
    itemKey: ((T) -> Any)? = null,
    emptyMessage: String = "Không tìm thấy dữ liệu",
    showLoadingCenter: Boolean = true,
    autoLoad: Boolean = true,
    onSelectionChanged: ((Set<T>) -> Unit)? = null,
    itemContent: @Composable (item: T, isSelected: Boolean, viewModel: ListViewModel<T>) -> Unit
) {

    /* ------------ ViewModel ------------ */

    val vm: ListViewModel<T> = viewModel ?: viewModel(
        key = repository.hashCode().toString(),
        factory = ListViewModelFactory(repository)
    )

    val uiState by vm.uiState.collectAsStateWithLifecycle()

    val items = uiState.items
    val status = uiState.status
    val selectedItems = uiState.selectedItems

    val isRefreshing = uiState.isRefreshing
    val isLoadingMore = uiState.isLoadingMore
    val isSelectionMode = uiState.isSelectionMode

    val listState = rememberLazyListState()
    val pullToRefreshState = rememberPullToRefreshState()

    val swipeCoordinator = remember { SwipeCoordinator<Any>() }
    LaunchedEffect(isSelectionMode) {
        if (isSelectionMode) {
            swipeCoordinator.closeAll()
        }
    }

    /* ------------ Selection callback ------------ */

    LaunchedEffect(selectedItems) {
        onSelectionChanged?.invoke(selectedItems)
    }

    /* ------------ Pull to refresh ------------ */

    if (pullToRefreshState.isRefreshing && vm.config.enableRefresh) {
        LaunchedEffect(Unit) { vm.refresh() }
    }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing && items.isNotEmpty())
            pullToRefreshState.startRefresh()
        else
            pullToRefreshState.endRefresh()
    }

    /* ------------ Load more ------------ */

    val shouldLoadMore by remember {
        derivedStateOf {
            val layout = listState.layoutInfo
            val total = layout.totalItemsCount
            val lastIndex = (layout.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1
            total > 0 && lastIndex >= total - 2
        }
    }

    LaunchedEffect(shouldLoadMore, status) {
        if (
            shouldLoadMore &&
            vm.config.enableLoadMore &&
            uiState.canLoadMore &&
            status is ListStatus.Idle
        ) {
            vm.loadMore()
        }
    }

    val scrollModifier =
        if (vm.config.enableRefresh)
            Modifier.nestedScroll(pullToRefreshState.nestedScrollConnection)
        else Modifier

    /* ------------ UI ------------ */

    Box(
        modifier = modifier
            .fillMaxSize()
            .then(scrollModifier)
    ) {
        when {

            items.isEmpty() && isRefreshing && showLoadingCenter -> {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }

            items.isEmpty() && status is ListStatus.Idle -> {
                ListEmptyState(
                    message = emptyMessage,
                    onReload = { vm.refresh() }
                )
            }

            status is ListStatus.Error -> {
                ListEmptyState(
                    message = status.message,
                    onReload = { vm.refresh() }
                )
            }

            else -> {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(items, key = itemKey) { item ->

                        val isSelected = selectedItems.contains(item)
                        val swipeActions = vm.swipeActions(item)

                        val content: @Composable () -> Unit = {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .pointerInput(
                                        vm.config.selectionMode,
                                        isSelectionMode
                                    ) {
                                        if (vm.config.selectionMode == SelectionMode.NONE)
                                            return@pointerInput

                                        detectTapGestures(
                                            onLongPress = { vm.toggleSelection(item) },
                                            onTap = {
                                                if (isSelectionMode)
                                                    vm.toggleSelection(item)
                                            }
                                        )
                                    }
                            ) {
                                itemContent(item, isSelected, vm)
                            }
                        }

                        if (swipeActions.enabled && !isSelectionMode) {

                            val swipeKey: Any = itemKey?.invoke(item) ?: item.hashCode()
                            SwipeListItem(
                                item = item,
                                key = swipeKey,
                                coordinator = swipeCoordinator,
                                swipeEnabled = !isSelectionMode, // 👈 NEW
                                startActions = swipeActions.start,
                                endActions = swipeActions.end
                            ) {
                                content()
                            }
                        } else {
                            content()
                        }

                    }

                    if (isLoadingMore && uiState.canLoadMore) {
                        item { LoadMoreIndicator() }
                    }
                }
            }
        }

        if (
            vm.config.enableRefresh &&
            (items.isNotEmpty() || pullToRefreshState.isRefreshing)
        ) {
            PullToRefreshContainer(
                modifier = Modifier.align(Alignment.TopCenter),
                state = pullToRefreshState
            )
        }
    }

    LaunchedEffect(repository) {
        if (autoLoad && items.isEmpty() && status is ListStatus.Idle) {
            vm.load()
        }
    }
}
