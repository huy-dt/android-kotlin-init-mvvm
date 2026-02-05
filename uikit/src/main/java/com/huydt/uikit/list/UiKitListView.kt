package com.huydt.uikit.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun <T> UiKitListView(
    repository: UiKitListRepository<T>,
    itemKey: ((T) -> Any)? = null,
    itemContent: @Composable (item: T, viewModel: UiKitListViewModel<T>) -> Unit
) {
    val viewModel: UiKitListViewModel<T> =
        viewModel(factory = UiKitListViewModelFactory(repository))

    val items by viewModel.items.collectAsState()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()

    val listState = rememberLazyListState()

    // initial load
    LaunchedEffect(Unit) {
        viewModel.load()
    }

    // load more when scroll to bottom
    LaunchedEffect(listState) {
        snapshotFlow {
            val lastVisibleIndex =
                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            val totalItems = listState.layoutInfo.totalItemsCount
            lastVisibleIndex to totalItems
        }.collect { (last, total) ->
            if (
                last != null &&
                last >= total - 1 &&
                viewModel.canLoadMore &&
                !isLoadingMore
            ) {
                viewModel.loadMore()
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        LazyColumn(state = listState) {

            items(
                items = items,
                key = itemKey
            ) { item ->
                itemContent(item, viewModel)
            }

            if (isLoadingMore) {
                item {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}
