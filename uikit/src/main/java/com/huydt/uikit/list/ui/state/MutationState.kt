package com.huydt.uikit.list.ui.state

sealed class MutationState {
    object Idle : MutationState()
    object Deleting : MutationState()
    object Creating : MutationState()
    object Updating : MutationState()
}