package com.huydt.uikit.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.huydt.uikit.topbar.TopBar
import com.huydt.uikit.topbar.IconColorDefaults
import com.huydt.uikit.topbar.model.TopBarActionGroup
import com.huydt.uikit.topbar.model.TopBarItem
import com.huydt.uikit.icon.model.IconSize

/**
 * TopBar chế độ selection cho mọi màn hình dùng ListViewModel.
 *
 * @param selectedCount         Số item đang được chọn
 * @param isAllSelected         Tất cả item đã được chọn chưa
 * @param onCancelSelection     Huỷ selection
 * @param onToggleSelectAll     Toggle chọn tất / bỏ tất
 * @param onDeleteSelected      Null = ẩn nút delete (cả quick action lẫn overflow)
 * @param onEditSelected        Null = ẩn khỏi overflow
 * @param onExportSelected      Null = ẩn khỏi overflow
 * @param onDuplicateSelected   Null = ẩn khỏi overflow
 * @param extraMoreActions      Inject thêm action group tuỳ theo từng feature
 */
@Composable
fun ListSelectionTopBar(
    selectedCount: Int,
    isAllSelected: Boolean,
    onCancelSelection: (() -> Unit)? = null,
    onToggleSelectAll: (() -> Unit)? = null,
    onDeleteSelected: (() -> Unit)? = null,
    onEditSelected: (() -> Unit)? = null,
    onExportSelected: (() -> Unit)? = null,
    onDuplicateSelected: (() -> Unit)? = null,
    extraMoreActions: List<TopBarActionGroup> = emptyList()
) {
    val defaultColors = IconColorDefaults.colors()

    TopBar(
        itemSize = IconSize.SMALL,
        showLabel = true,
        colors = defaultColors,

        /* ──────────────── LEFT: Huỷ selection ──────────────── */
        leftActions = buildList {
            if (onCancelSelection != null) {
                add(
                    TopBarItem(
                        id = "cancel",
                        icon = Icons.Default.Close,
                        label = "Cancel",
                        onClick = onCancelSelection
                    )
                )
            }
        },

        /* ──────────────── MID: Số lượng đang chọn ──────────────── */
        midActions = listOf(
            TopBarItem(
                id = "selected_count",
                label = "$selectedCount selected",
                enabled = false,
                onClick = {}
            )
        ),

        /* ──────────────── RIGHT: Thao tác nhanh ──────────────── */
        rightActions = buildList {
            if (onToggleSelectAll != null) {
                add(
                    TopBarItem(
                        id = "toggle_all",
                        icon = Icons.Default.SelectAll,
                        label = if (isAllSelected) "Deselect" else "Select all",
                        selected = isAllSelected,
                        colors = defaultColors.copy(
                            color = if (isAllSelected) Color(0xFF2E7D32) else defaultColors.color
                        ),
                        onClick = onToggleSelectAll
                    )
                )
            }
            if (onDeleteSelected != null) {
                add(
                    TopBarItem(
                        id = "delete_selected",
                        icon = Icons.Default.Delete,
                        label = "Delete",
                        colors = defaultColors.copy(color = Color(0xFFD32F2F)),
                        onClick = onDeleteSelected
                    )
                )
            }
        },

        /* ──────────────── OVERFLOW ──────────────── */
        moreActions = buildList {
            val actionItems = buildList {
                if (onEditSelected != null) add(
                    TopBarItem(id = "edit", icon = Icons.Default.Edit, label = "Edit", onClick = onEditSelected)
                )
                if (onExportSelected != null) add(
                    TopBarItem(id = "export", icon = Icons.Default.Download, label = "Export", onClick = onExportSelected)
                )
                if (onDuplicateSelected != null) add(
                    TopBarItem(id = "duplicate", icon = Icons.Default.ContentCopy, label = "Duplicate", onClick = onDuplicateSelected)
                )
            }
            if (actionItems.isNotEmpty()) {
                add(TopBarActionGroup(id = "actions", title = "Actions", items = actionItems))
            }

            val dangerItems = buildList {
                if (onDeleteSelected != null) add(
                    TopBarItem(
                        id = "delete_all_selected",
                        icon = Icons.Default.Delete,
                        label = "Delete selected",
                        colors = defaultColors.copy(color = Color(0xFFD32F2F)),
                        onClick = onDeleteSelected
                    )
                )
            }
            if (dangerItems.isNotEmpty()) {
                add(TopBarActionGroup(id = "danger", title = "Danger Zone", items = dangerItems))
            }

            addAll(extraMoreActions)
        }
    )
}