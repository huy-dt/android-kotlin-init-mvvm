package com.xxx.app.feature_user.ui.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import com.huydt.uikit.topbar.TopBar
import com.huydt.uikit.topbar.model.TopBarActionGroup
import com.huydt.uikit.topbar.model.TopBarItem
import com.huydt.uikit.topbar.model.TopBarItemSize
import com.huydt.uikit.topbar.TopBarDefaults
import androidx.compose.ui.graphics.Color

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
        leftActions = listOf(
            TopBarItem(
                id = "cancel",
                icon = Icons.Default.Close,
                label = "Cancel",
                enabled = onCancelSelection != null,
                onClick = { onCancelSelection?.invoke() }
            )
        ),

        /* ---------------- MID ---------------- */
        midActions = listOf(
            TopBarItem(
                id = "selected_count",
                icon = Icons.Default.Check,
                label = "$selectedCount selected",
                enabled = false,          // display-only
                onClick = {}              // no-op
            )
        ),

        /* ---------------- RIGHT ---------------- */
        rightActions = listOf(
            TopBarItem(
                id = "toggle_all",
                icon = Icons.Default.SelectAll,
                label = "Select all",
                enabled = onToggleSelectAll != null,
                contentColor = if (isAllSelected) Color(0xFF2E7D32) else null,
                onClick = { onToggleSelectAll?.invoke() }
            ),

            TopBarItem(
                id = "delete_selected",
                icon = Icons.Default.Delete,
                label = "Delete",
                enabled = true,
                contentColor = TopBarDefaults.colors().content.copy(red = 0.9f),
                onClick = { onDeleteSelected?.invoke() }
            )
        ),

        /* ---------------- OVERFLOW ---------------- */
        moreActions = listOf(

            // ===== GROUP 1: MAIN ACTIONS =====
            TopBarActionGroup(
                id = "main_actions",
                title = "Actions",
                actions = listOf(
                    TopBarItem(
                        id = "edit",
                        icon = Icons.Default.Edit,
                        label = "Edit selected",
                        onClick = { onEditSelected?.invoke() }
                    ),
                    TopBarItem(
                        id = "export",
                        icon = Icons.Default.Download,
                        label = "Export selected",
                        onClick = { onExportSelected?.invoke() }
                    )
                )
            ),

            // ===== GROUP 2: SECONDARY =====
            TopBarActionGroup(
                id = "secondary_actions",
                title = "More",
                actions = listOf(
                    TopBarItem(
                        id = "duplicate",
                        icon = Icons.Default.ContentCopy,
                        label = "Duplicate",
                        enabled = false,          // demo disabled
                        onClick = {}
                    )
                )
            ),

            // ===== GROUP 3: DANGER ZONE =====
            TopBarActionGroup(
                id = "danger_zone",
                // title = "Danger zone",
                actions = listOf(
                    TopBarItem(
                        id = "delete",
                        icon = Icons.Default.Delete,
                        label = "Delete selected",
                        contentColor = TopBarDefaults.colors().content.copy(red = 0.9f),
                        onClick = { onDeleteSelected?.invoke() }
                    )
                )
            )
        )

    )
}
