package com.xxx.app.feature_user.ui

import androidx.compose.animation.*
import com.huydt.uikit.topbar.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.huydt.uikit.list.config.SelectionMode
import com.xxx.app.feature_user.ui.viewmodel.TopBarMode
import com.xxx.app.feature_user.ui.viewmodel.UserListViewModel

@Composable
fun ListUserScreenWithTopBar(
    onBack: (() -> Unit)?,
    vm: UserListViewModel
) {
    val uiState        by vm.uiState.collectAsStateWithLifecycle()
    val topBarMode     by vm.topBarMode.collectAsStateWithLifecycle()
    val searchQuery    by vm.searchQuery.collectAsStateWithLifecycle()
    val filterGroups   by vm.activeFilterGroups.collectAsStateWithLifecycle()
    val filterBadge    by vm.activeFilterCount.collectAsStateWithLifecycle()
    val sortState      by vm.sortState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // ── TopBar layer ──
        when {
            // 1. Selection mode — ưu tiên cao nhất
            uiState.isSelectionMode -> {
                ListSelectionTopBar(
                    selectedCount = uiState.selectedItems.size,
                    isAllSelected = uiState.isAllSelected,
                    onCancelSelection = vm::clearSelection,
                    onToggleSelectAll = vm::toggleSelectAll.takeIf {
                        vm.selectionMode == SelectionMode.MULTI
                    },
                    onDeleteSelected = vm::deleteSelected,
                    onExportSelected = vm::exportSelected
                )
            }

            // 2. Search bar inline
            topBarMode == TopBarMode.SEARCH -> {
                InlineSearchBar(
                    query = searchQuery,
                    onQueryChange = vm::onSearchQueryChange,
                    onClose = {
                        vm.clearSearch()
                        vm.closeBar()
                    }
                )
            }

            // 3. Filter bar inline
            topBarMode == TopBarMode.FILTER -> {
                InlineFilterBar(
                    groups = filterGroups,
                    onSelect = vm::onFilterSelect,
                    onClearAll = vm::clearAllFilters,
                    onClose = vm::closeBar
                )
            }

            // 4. Sort bar inline
            topBarMode == TopBarMode.SORT -> {
                InlineSortBar(
                    options = vm.sortOptions,
                    sortState = sortState,
                    onSelect = vm::onSortSelect,
                    onToggleDir = vm::onToggleSortDirection,
                    onClear = vm::clearSort,
                    onClose = vm::closeBar
                )
            }

            // 5. Normal topbar
            else -> {
                ListTopBar(
                    filterBadge = filterBadge,
                    onBack = onBack,
                    onRefresh = vm::refresh,
                    onSearch = vm::openSearch,
                    onFilter = vm::openFilter,
                    onSort = vm::openSort,
                    onImport = vm::importData,
                    onExport = vm::exportAll,
                    onDeleteAll = vm::deleteAll
                )
            }
        }

        // ── Content ──
        _ListUserScreen(
            vm = vm,
            modifier = Modifier.weight(1f)
        )
    }
}