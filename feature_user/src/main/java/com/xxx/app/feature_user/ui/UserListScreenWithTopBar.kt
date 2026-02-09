package com.xxx.app.feature_user.ui

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xxx.app.feature_user.ui.topbar.UserListTopBar
import com.xxx.app.feature_user.ui.topbar.UserSelectionTopBar
import com.xxx.app.feature_user.ui.viewmodel.UserListViewModel

@Composable
fun ListUserScreenWithTopBar(
    onBack: (() -> Unit)?,
    vm: UserListViewModel
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
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
        }
    ) { padding ->
        _ListUserScreen(
            vm = vm,
            contentPadding = padding
        )
    }
}
