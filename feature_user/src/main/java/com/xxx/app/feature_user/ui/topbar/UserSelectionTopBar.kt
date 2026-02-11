package com.xxx.app.feature_user.ui.topbar

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
import com.huydt.uikit.topbar.ItemColorDefaults
import com.huydt.uikit.topbar.model.TopBarActionGroup
import com.huydt.uikit.topbar.model.TopBarItem
import com.huydt.uikit.topbar.model.TopBarItemSize

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

    TopBar(
        showLabel = true,
        itemSize = TopBarItemSize.MEDIUM,

        /* ---------------- LEFT ---------------- */
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

        /* ---------------- MID ---------------- */
        midActions = listOf(
            TopBarItem(
                id = "selected_count",
                label = "$selectedCount selected",
                enabled = false,
                onClick = {} // display-only
            )
        ),

        /* ---------------- RIGHT ---------------- */
        rightActions = buildList {

            if (onToggleSelectAll != null) {
                add(
                    TopBarItem(
                        id = "toggle_all",
                        icon = Icons.Default.SelectAll,
                        label = "Select all",
                        selected = isAllSelected,
                        colors = ItemColorDefaults.colors(
                            content = if (isAllSelected)
                                Color(0xFF2E7D32)
                            else
                                Color.Unspecified
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
                        colors = ItemColorDefaults.colors(
                            content = Color(0xFFE6E6E6)
                        ),
                        onClick = onDeleteSelected
                    )
                )
            }
        },

        /* ---------------- OVERFLOW ---------------- */
        moreActions = listOf(

            // ===== GROUP 1: MAIN ACTIONS =====
            TopBarActionGroup(
                id = "main_actions",
                title = "Actions",
                items = buildList {
                    if (onEditSelected != null) {
                        add(
                            TopBarItem(
                                id = "edit",
                                icon = Icons.Default.Edit,
                                label = "Edit selected",
                                onClick = onEditSelected
                            )
                        )
                    }

                    if (onExportSelected != null) {
                        add(
                            TopBarItem(
                                id = "export",
                                icon = Icons.Default.Download,
                                label = "Export selected",
                                onClick = onExportSelected
                            )
                        )
                    }
                }
            ),

            // ===== GROUP 2: SECONDARY =====
            TopBarActionGroup(
                id = "secondary_actions",
                title = "More",
                items = listOf(
                    TopBarItem(
                        id = "duplicate",
                        icon = Icons.Default.ContentCopy,
                        label = "Duplicate",
                        enabled = false,
                        onClick = {}
                    )
                )
            ),

            // ===== GROUP 3: DANGER ZONE =====
            TopBarActionGroup(
                id = "danger_zone",
                title = "Danger Zone",
                items = buildList {
                    if (onDeleteSelected != null) {
                        add(
                            TopBarItem(
                                id = "delete",
                                icon = Icons.Default.Delete,
                                label = "Delete selected",
                                colors = ItemColorDefaults.colors(
                                    content = Color(0xFFE6E6E6)
                                ),
                                onClick = onDeleteSelected
                            )
                        )
                    }
                }
            )
        ).filter { it.items.isNotEmpty() } // Giữ lại group có items
    )
}
