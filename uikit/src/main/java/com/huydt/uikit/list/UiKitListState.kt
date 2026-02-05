package com.huydt.uikit.list

import androidx.compose.runtime.Stable

@Stable
data class UiKitListState(
    val isLoading: Boolean = false,
    val endReached: Boolean = false,
    val error: Throwable? = null
)
