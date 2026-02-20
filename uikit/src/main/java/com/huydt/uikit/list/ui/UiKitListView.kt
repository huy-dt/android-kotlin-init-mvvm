package com.huydt.uikit.list.ui

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.huydt.uikit.list.ui.components.*
import com.huydt.uikit.list.ui.state.*
import com.huydt.uikit.list.viewmodel.ListViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun <T> UiKitListView(
    vm: ListViewModel<T>,
    modifier: Modifier = Modifier,
    itemContent: @Composable (T, Boolean) -> Unit
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val pullState = rememberPullToRefreshState()
    val haptic = LocalHapticFeedback.current

    var openedItemKey by remember { mutableStateOf<Any?>(null) }
    val isSelectionMode = uiState.isSelectionMode

    var startActionsTotalPx by remember { mutableFloatStateOf(0f) }
    var endActionsTotalPx by remember { mutableFloatStateOf(0f) }

    if (uiState.items.isNotEmpty()) {
        val sampleItem = uiState.items.first()
        val actions = vm.swipeActions(sampleItem)
        Box(Modifier.fillMaxWidth().height(0.dp).alpha(0f)) {
            Row(Modifier.onSizeChanged { startActionsTotalPx = it.width.toFloat() }) {
                actions.start.forEach { action -> action(sampleItem) {} }
            }
            Row(Modifier.onSizeChanged { endActionsTotalPx = it.width.toFloat() }) {
                actions.end.forEach { action -> action(sampleItem) {} }
            }
        }
    }

    LaunchedEffect(isSelectionMode) {
        if (isSelectionMode) openedItemKey = null
    }

    // Auto load lần đầu
    LaunchedEffect(uiState.items) {
        if (uiState.items.isEmpty() &&
            uiState.pagingState is PagingState.Init
        ) {
            vm.load()
        }
    }

    // Scroll to top
    LaunchedEffect(Unit) {
        vm.scrollToTop.collect {
            listState.scrollToItem(0)
            openedItemKey = null
        }
    }

    // Pull refresh trigger
    if (pullState.isRefreshing) {
        LaunchedEffect(Unit) { vm.refresh() }
    }

    // Sync pull state với pagingState
    LaunchedEffect(uiState.pagingState) {
        if (uiState.isRefreshing && uiState.items.isNotEmpty())
            pullState.startRefresh()
        if (!uiState.isRefreshing)
            pullState.endRefresh()
    }

    // Load more khi gần cuối list
    LaunchedEffect(listState, uiState.canLoadMore, uiState.pagingState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisible >= totalItems - 2 && totalItems > 0
        }.collect { isAtBottom ->
            if (isAtBottom &&
                uiState.pagingState is PagingState.Idle &&
                uiState.canLoadMore
            ) {
                vm.loadMore()
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RectangleShape)
            .nestedScroll(pullState.nestedScrollConnection)
    ) {
        when {
            uiState.items.isEmpty() && uiState.isRefreshing -> {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    CircularProgressIndicator(strokeWidth = 3.dp)
                }
            }

            uiState.items.isEmpty() -> {
                if(uiState.pagingState !is PagingState.Init){
                    EmptyStateView(
                        message = (uiState.pagingState as? PagingState.Error)?.message
                            ?: "Không có dữ liệu",
                        onRefresh = { vm.refresh() }
                    )
                }
            }

            else -> {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = uiState.items,
                        key = { it.hashCode() }
                    ) { item ->

                        val currentItemKey = item.hashCode()
                        val isOpened = openedItemKey == currentItemKey
                        val actions = vm.swipeActions(item)

                        val lActions = if (isSelectionMode) emptyList() else actions.start.map { action ->
                            val wrapper: @Composable (onClose: () -> Unit) -> Unit = { onClose ->
                                action(item, onClose)
                            }
                            wrapper
                        }

                        val rActions = if (isSelectionMode) emptyList() else actions.end.map { action ->
                            val wrapper: @Composable (onClose: () -> Unit) -> Unit = { onClose ->
                                action(item, onClose)
                            }
                            wrapper
                        }

                        key(currentItemKey to isSelectionMode) {
                            SwipeActionItem(
                                isOpened = isOpened,
                                onOpened = { openedItemKey = currentItemKey },
                                onClosed = {
                                    if (openedItemKey == currentItemKey)
                                        openedItemKey = null
                                },
                                leftActions = lActions,
                                rightActions = rActions,
                                leftTotalPx = startActionsTotalPx,
                                rightTotalPx = endActionsTotalPx
                            ) {
                                val isSelected =
                                    uiState.selectedItems.contains(item)

                                ListItemContent(
                                    item = item,
                                    isSelected = isSelected,
                                    isSelectionMode = isSelectionMode,
                                    haptic = haptic,
                                    onItemClick = { clickedItem ->
                                        if (isSelectionMode)
                                            vm.toggleSelection(clickedItem)
                                    },
                                    onToggleSelection = { toggledItem ->
                                        openedItemKey = null
                                        vm.toggleSelection(toggledItem)
                                    },
                                    itemContent = itemContent
                                )
                            }
                        }
                    }

                    item {
                        FooterContent(
                            isLoading = uiState.isLoadingMore,
                            hasItems = uiState.items.isNotEmpty(),
                            canLoadMore = uiState.canLoadMore,
                            isPaginationEnabled = vm.config.enableLoadMore
                        )
                    }
                }
            }
        }

        PullToRefreshContainer(
            state = pullState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}