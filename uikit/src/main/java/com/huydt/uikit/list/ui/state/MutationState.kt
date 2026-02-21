package com.huydt.uikit.list.ui.state

sealed class MutationState {
    object Idle : MutationState()
    object Loading : MutationState()
    data class Success(val message: String? = null) : MutationState()
    data class Error(val message: String) : MutationState()
}