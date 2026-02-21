package com.huydt.uikit.list.ui.state

sealed class PagingState {
    object Idle : PagingState()
    object Refreshing : PagingState()
    object LoadingMore : PagingState()
    data class Error(val message: String) : PagingState()
}