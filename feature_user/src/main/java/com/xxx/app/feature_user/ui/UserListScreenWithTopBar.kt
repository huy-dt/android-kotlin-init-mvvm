package com.xxx.app.feature_user.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xxx.app.feature_user.ui.topbar.UserListTopBar
import com.xxx.app.feature_user.ui.topbar.UserSelectionTopBar
import com.xxx.app.feature_user.ui.viewmodel.UserListViewModel
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color

@Composable
fun ListUserScreenWithTopBar(
    onBack: (() -> Unit)?,
    vm: UserListViewModel
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        if (uiState.isSelectionMode) {
            UserSelectionTopBar(
                selectedCount = uiState.selectedItems.size,
                isAllSelected = uiState.selectedItems.size == uiState.items.size,
                onCancelSelection = vm::clearSelection,
                onToggleSelectAll = vm::toggleSelectAll
            )
        } else {
            UserListTopBar(
                onBack = onBack,
                onRefresh = vm::refresh
            )
        }

        _ListUserScreen(
            vm = vm,
            modifier = Modifier.weight(1f)
        )
    }
}
