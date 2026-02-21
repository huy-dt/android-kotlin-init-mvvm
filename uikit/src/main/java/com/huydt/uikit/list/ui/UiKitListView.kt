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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.huydt.uikit.list.ui.components.*
import com.huydt.uikit.list.ui.state.*
import com.huydt.uikit.list.ui.swipe.SwipeAction
import com.huydt.uikit.list.viewmodel.ListEvent
import com.huydt.uikit.list.viewmodel.ListViewModel
import kotlinx.coroutines.flow.filterIsInstance

// ─────────────────────────────────────────────────────────────
// Helper: render list SwipeAction thành Row các button
// Tách ra function @Composable riêng → tránh lỗi "invocations
// can only happen from the context of a @Composable function"
// ─────────────────────────────────────────────────────────────
@Composable
private fun <T> SwipeActionRow(
    actions: List<SwipeAction>,
    item: T,
    vm: ListViewModel<T>,
    onClose: () -> Unit,
) {
    actions.forEach { swipeAction ->
        SwipeActionButton(
            action = swipeAction,
            onClose = {
                vm.onSwipeActionClick(item, swipeAction.id)
                onClose()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun <T> UiKitListView(
    vm: ListViewModel<T>,
    modifier: Modifier = Modifier,
    itemContent: @Composable (T, Boolean) -> Unit,
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val pullState = rememberPullToRefreshState()
    val haptic = LocalHapticFeedback.current
    val snackbarHostState = remember { SnackbarHostState() }

    val isPaginationMode = !vm.config.enableLoadMore
    val isSelectionMode = uiState.isAnySelected

    var openedItemKey by remember { mutableStateOf<Any?>(null) }
    var startActionsTotalPx by remember { mutableFloatStateOf(0f) }
    var endActionsTotalPx by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(isSelectionMode) {
        if (isSelectionMode) openedItemKey = null
    }

    LaunchedEffect(Unit) {
        if (uiState.items.isEmpty() && uiState.pagingState is PagingState.Idle) {
            vm.load()
        }
    }

    LaunchedEffect(Unit) {
        vm.scrollToTop.collect {
            listState.scrollToItem(0)
            openedItemKey = null
        }
    }

    LaunchedEffect(Unit) {
        vm.events.filterIsInstance<ListEvent.ShowUndo>().collect { event ->
            val result = snackbarHostState.showSnackbar(
                message = "Đã xóa",
                actionLabel = "Hoàn tác",
                duration = SnackbarDuration.Short,
            )
            if (result == SnackbarResult.ActionPerformed) vm.undoRemove()
        }
    }

    LaunchedEffect(Unit) {
        vm.events.filterIsInstance<ListEvent.ShowMessage>().collect { event ->
            snackbarHostState.showSnackbar(message = event.message)
        }
    }

    if (pullState.isRefreshing) {
        LaunchedEffect(Unit) { vm.refresh() }
    }

    LaunchedEffect(uiState.pagingState) {
        if (uiState.isLoading && uiState.items.isNotEmpty()) pullState.startRefresh()
        if (!uiState.isLoading) pullState.endRefresh()
    }

    if (!isPaginationMode) {
        LaunchedEffect(listState, uiState.canLoadMore, uiState.pagingState) {
            snapshotFlow {
                val info = listState.layoutInfo
                val total = info.totalItemsCount
                val lastVisible = info.visibleItemsInfo.lastOrNull()?.index ?: 0
                lastVisible >= total - 2 && total > 0
            }.collect { isAtBottom ->
                if (isAtBottom &&
                    uiState.pagingState is PagingState.Idle &&
                    uiState.canLoadMore
                ) vm.loadMore()
            }
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .clip(RectangleShape)
                .nestedScroll(pullState.nestedScrollConnection)
        ) {
            when {
                uiState.items.isEmpty() && uiState.isLoading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(strokeWidth = 3.dp)
                    }
                }

                uiState.items.isEmpty() && uiState.pagingState is PagingState.Error -> {
                    ErrorStateView(
                        message = uiState.errorMessage
                            ?: (uiState.pagingState as PagingState.Error).message,
                        onRetry = { vm.refresh() }
                    )
                }

                uiState.items.isEmpty() -> {
                    EmptyStateView(
                        message = uiState.emptyMessage ?: "Không có dữ liệu",
                        onRefresh = { vm.refresh() }
                    )
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

                            // Build Composable wrapper list dùng SwipeActionRow
                            // SwipeActionItem vẫn nhận List<(onClose: () -> Unit) -> Unit>
                            val lActions: List<@Composable (onClose: () -> Unit) -> Unit> =
                                if (isSelectionMode || actions.startActions.isEmpty()) {
                                    emptyList()
                                } else {
                                    listOf(
                                        { onClose ->
                                            SwipeActionRow(
                                                actions = actions.startActions,
                                                item = item,
                                                vm = vm,
                                                onClose = onClose,
                                            )
                                        }
                                    )
                                }

                            val rActions: List<@Composable (onClose: () -> Unit) -> Unit> =
                                if (isSelectionMode || actions.endActions.isEmpty()) {
                                    emptyList()
                                } else {
                                    listOf(
                                        { onClose ->
                                            SwipeActionRow(
                                                actions = actions.endActions,
                                                item = item,
                                                vm = vm,
                                                onClose = onClose,
                                            )
                                        }
                                    )
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
                                    rightTotalPx = endActionsTotalPx,
                                ) {
                                    val isSelected = uiState.selectedItems.contains(item)
                                    ListItemContent(
                                        item = item,
                                        isSelected = isSelected,
                                        isSelectionMode = isSelectionMode,
                                        haptic = haptic,
                                        onItemClick = { clicked ->
                                            if (isSelectionMode) vm.toggleSelection(clicked)
                                        },
                                        onToggleSelection = { toggled ->
                                            openedItemKey = null
                                            vm.toggleSelection(toggled)
                                        },
                                        itemContent = itemContent,
                                    )
                                }
                            }
                        }

                        item(key = "footer") {
                            ListFooter(
                                isLoadingMore = uiState.isLoadingMore,
                                canLoadMore = uiState.canLoadMore,
                                isPaginationMode = isPaginationMode,
                                currentPage = uiState.currentPage,
                                totalPages = uiState.totalPages,
                                hasPreviousPage = uiState.hasPreviousPage,
                                hasNextPage = uiState.hasNextPage,
                                onPrevious = { vm.previousPage() },
                                onNext = { vm.nextPage() },
                                onGoToPage = { vm.goToPage(it) },
                            )
                        }
                    }
                }
            }

            PullToRefreshContainer(
                state = pullState,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }
    }
}