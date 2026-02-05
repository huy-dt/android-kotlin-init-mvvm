package com.huydt.uikit.list.domain

sealed class ListStatus {
    object Idle : ListStatus()
    object Refreshing : ListStatus()
    object LoadingMore : ListStatus()
    data class Error(val message: String) : ListStatus()
}