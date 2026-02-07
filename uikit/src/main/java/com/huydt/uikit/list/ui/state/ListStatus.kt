package com.huydt.uikit.list.ui.state

sealed class ListStatus {
    object Init : ListStatus()
    object Idle : ListStatus()
    object Refreshing : ListStatus()
    object LoadingMore : ListStatus()
    data class Error(val message: String) : ListStatus()
}