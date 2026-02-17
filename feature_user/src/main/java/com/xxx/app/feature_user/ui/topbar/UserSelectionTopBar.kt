package com.xxx.app.feature_user.ui.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.huydt.uikit.topbar.TopBar
import com.huydt.uikit.topbar.IconColorDefaults
import com.huydt.uikit.topbar.model.TopBarActionGroup
import com.huydt.uikit.topbar.model.TopBarItem
import com.huydt.uikit.icon.model.IconSize

@Composable
fun UserSelectionTopBar(
    selectedCount: Int,
    isAllSelected: Boolean,
    onCancelSelection: (() -> Unit)? = null,
    onToggleSelectAll: (() -> Unit)? = null,
    onEditSelected: (() -> Unit)? = null,
    onDeleteSelected: (() -> Unit)? = null,
    onExportSelected: (() -> Unit)? = null
) {
    // Khai báo màu chung cho toàn bộ TopBar
    val defaultColors = IconColorDefaults.colors(
        // color = Color(0xFFE6E6E6),
        // background = Color.White
    )

    TopBar(
        showLabel = true,
        itemSize = IconSize.SMALL,
        colors = defaultColors,

        /* ---------------- LEFT: Nút đóng/hủy ---------------- */
        leftActions = buildList {
            onCancelSelection?.let {
                add(
                    TopBarItem(
                        id = "cancel",
                        icon = Icons.Default.Close,
                        label = "Cancel",
                        onClick = it
                    )
                )
            }
        },

        /* ---------------- MID: Hiển thị số lượng ---------------- */
        midActions = listOf(
            TopBarItem(
                id = "selected_count",
                label = "$selectedCount selected",
                enabled = false,
                onClick = {} 
            )
        ),

        /* ---------------- RIGHT: Các thao tác nhanh ---------------- */
        rightActions = buildList {
            onToggleSelectAll?.let {
                add(
                    TopBarItem(
                        id = "toggle_all",
                        icon = Icons.Default.SelectAll,
                        label = "Select all",
                        selected = isAllSelected,
                        // Fix: Nếu không chọn thì dùng màu default của bar
                        colors = defaultColors.copy(
                            color = if (isAllSelected) Color(0xFF2E7D32) else defaultColors.color
                        ),
                        onClick = it
                    )
                )
            }

            onDeleteSelected?.let {
                add(
                    TopBarItem(
                        id = "delete_selected",
                        icon = Icons.Default.Delete,
                        label = "Delete",
                        colors = defaultColors.copy(color = Color(0xFFE6E6E6)),
                        onClick = it
                    )
                )
            }
        },

        /* ---------------- OVERFLOW: Các thao tác bổ sung ---------------- */
        moreActions = listOf(
            // GROUP 1: Chỉnh sửa & Xuất bản
            TopBarActionGroup(
                id = "main_actions",
                title = "Actions",
                items = buildList {
                    onEditSelected?.let {
                        add(TopBarItem(id = "edit", icon = Icons.Default.Edit, label = "Edit", onClick = it))
                    }
                    onExportSelected?.let {
                        add(TopBarItem(id = "export", icon = Icons.Default.Download, label = "Export", onClick = it))
                    }
                }
            ),

            // GROUP 2: Các thao tác phụ
            TopBarActionGroup(
                id = "secondary_actions",
                title = "More",
                items = listOf(
                    TopBarItem(
                        id = "duplicate",
                        icon = Icons.Default.ContentCopy,
                        label = "Duplicate",
                        enabled = false, // Hiện tại chưa support
                        onClick = {}
                    )
                )
            ),

            // GROUP 3: Vùng nguy hiểm
            TopBarActionGroup(
                id = "danger_zone",
                title = "Danger Zone",
                items = buildList {
                    onDeleteSelected?.let {
                        add(
                            TopBarItem(
                                id = "delete",
                                icon = Icons.Default.Delete,
                                label = "Delete all",
                                // FIX: Sửa lỗi copy.copy thành defaultColors.copy
                                colors = defaultColors.copy(color = Color(0xFFD32F2F)), // Đổi sang màu đỏ cho đúng tính chất Danger
                                onClick = it
                            )
                        )
                    }
                }
            )
        ).filter { it.items.isNotEmpty() }
    )
}