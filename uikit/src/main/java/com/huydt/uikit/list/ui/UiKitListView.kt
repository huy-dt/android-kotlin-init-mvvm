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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.huydt.uikit.list.ui.components.*
import com.huydt.uikit.list.ui.state.ListStatus
// import com.huydt.uikit.list.ui.swipe.SwipeActionItem
import com.huydt.uikit.list.viewmodel.ListViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun <T> UiKitListView(
    vm: ListViewModel<T>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    itemContent: @Composable (T, Boolean) -> Unit
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val pullState = rememberPullToRefreshState()
    val haptic = LocalHapticFeedback.current

    // State quản lý item đang mở swipe
    var openedItemKey by remember { mutableStateOf<Any?>(null) }
    val isSelectionMode = uiState.selectedItems.isNotEmpty()

    // --- LOGIC ĐO KÍCH THƯỚC SWIPE ---
    var startActionsTotalPx by remember { mutableFloatStateOf(0f) }
    var endActionsTotalPx by remember { mutableFloatStateOf(0f) }

    if (uiState.items.isNotEmpty()) {
        val sampleItem = uiState.items.first()
        val actions = vm.swipeActions(sampleItem)
        Box(modifier = Modifier.fillMaxWidth().height(0.dp).alpha(0f)) {
            Row(modifier = Modifier.onSizeChanged { startActionsTotalPx = it.width.toFloat() }) {
                actions.start.forEach { action -> action(sampleItem) {} }
            }
            Row(modifier = Modifier.onSizeChanged { endActionsTotalPx = it.width.toFloat() }) {
                actions.end.forEach { action -> action(sampleItem) {} }
            }
        }
    }

    // Tự đóng swipe khi vào chế độ chọn nhiều
    LaunchedEffect(isSelectionMode) {
        if (isSelectionMode) openedItemKey = null
    }

    LaunchedEffect(uiState.items, uiState.status) {
        if (uiState.items.isEmpty() && uiState.status is ListStatus.Init) {
            vm.load()
        }
    }

    // ĐÚNG: Collect flow phải nằm trong LaunchedEffect
    LaunchedEffect(Unit) {
        vm.scrollToTop.collect {
            listState.scroll { }
            listState.scrollToItem(0)
            openedItemKey = null
        }
    }

    // Refresh & Load More logic
    if (pullState.isRefreshing) {
        LaunchedEffect(Unit) { vm.refresh() }
    }
    LaunchedEffect(uiState.status) {
        if (uiState.status is ListStatus.Refreshing && uiState.items.isNotEmpty())
            pullState.startRefresh()
        if (uiState.status !is ListStatus.Refreshing || uiState.items.isEmpty())
            pullState.endRefresh()
    }

    LaunchedEffect(listState, uiState.canLoadMore, uiState.status) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisible >= totalItems - 2 && totalItems > 0
        }.collect { isAtBottom ->
            if (isAtBottom && uiState.status is ListStatus.Idle && uiState.canLoadMore) {
                vm.loadMore()
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()
        .padding(contentPadding)
        .nestedScroll(pullState.nestedScrollConnection)
    ) {
        when {
            uiState.items.isEmpty() && uiState.status is ListStatus.Refreshing -> {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    CircularProgressIndicator(strokeWidth = 3.dp)
                }
            }
            uiState.items.isEmpty() -> {
                if (uiState.status != ListStatus.Init && uiState.status !is ListStatus.Refreshing) {
                    EmptyStateView(
                        message = (uiState.status as? ListStatus.Error)?.message ?: "Không có dữ liệu",
                        onRefresh = { vm.refresh() }
                    )
                }
            }
            else -> {
                LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                    items(
                        items = uiState.items,
                        // Sử dụng hashCode làm key mặc định vì signature không còn itemKey
                        key = { it.hashCode() }
                    ) { item ->
                        val currentItemKey = item.hashCode()
                        val isOpened by remember(currentItemKey, openedItemKey) {
                            derivedStateOf { openedItemKey == currentItemKey }
                        }

                        val actions = vm.swipeActions(item)

                        // Fix lỗi Composable context bằng cách bọc lambda đúng kiểu
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

                        // key() fix lỗi rất quan trong Lưu Ý
                        key(currentItemKey to isSelectionMode) {
                            SwipeActionItem(
                                isOpened = isOpened,
                                onOpened = { openedItemKey = currentItemKey },
                                onClosed = { if (openedItemKey == currentItemKey) openedItemKey = null },
                                leftActions = lActions,
                                rightActions = rActions,
                                leftTotalPx = startActionsTotalPx,
                                rightTotalPx = endActionsTotalPx
                            ) {
                                val isSelected = uiState.selectedItems.contains(item)
                                ListItemContent(
                                    item = item,
                                    isSelected = isSelected,
                                    isSelectionMode = isSelectionMode,
                                    haptic = haptic,
                                    onItemClick = { clickedItem ->
                                        if (openedItemKey != null) {
                                            // openedItemKey = null
                                        } else {
                                            if (isSelectionMode) vm.toggleSelection(clickedItem)
                                            // Nếu cần xử lý click riêng, bạn có thể gọi qua config hoặc một hàm open trong VM
                                        }
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

                    // if (uiState.canLoadMore) {
                        item {
                            FooterContent(
                                isLoading = uiState.status is ListStatus.LoadingMore,
                                hasItems = uiState.items.isNotEmpty(),
                                canLoadMore = uiState.canLoadMore,
                                isPaginationEnabled = vm.config.enableLoadMore
                            )
                        }
                    // }
                }
            }
        }

        PullToRefreshContainer(
            state = pullState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}