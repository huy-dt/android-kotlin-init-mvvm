package com.xxx.app.feature_user.ui.topbar

import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import com.huydt.uikit.topbar.model.TopBarItem
import com.huydt.uikit.topbar.model.TopBarItemSize
import com.huydt.uikit.topbar.model.TopBarActionGroup
import com.huydt.uikit.topbar.ui.TopBar

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

        /* ---------------- LEFT ---------------- */
        leftActions = listOf(
            TopBarItem(
                id = "close",
                icon = Icons.Default.Close,
                label = "Cancel",
                onClick = onCancelSelection
            )
        ),

        /* ---------------- MID ---------------- */
        midActions = listOf(
            TopBarItem(
                id = "selected_count",
                icon = null,
                label = "$selectedCount selected",
                onClick = null
            )
        ),

        /* ---------------- RIGHT ---------------- */
        rightActions = listOf(
            TopBarItem(
                id = "select_all",
                icon = if (isAllSelected)
                    Icons.Default.ClearAll
                else
                    Icons.Default.SelectAll,
                label = if (isAllSelected)
                    "Clear selection"
                else
                    "Select all",
                onClick = onToggleSelectAll
            ),
            TopBarItem(
                id = "delete",
                icon = Icons.Default.Delete,
                label = "Delete",
                tint = Color.Red,
                onClick = onDeleteSelected
            )
        ),

        /* ---------------- OVERFLOW ---------------- */
        moreActions = listOf(

            // CRUD
            TopBarActionGroup(
                items = listOf(
                    TopBarItem(
                        id = "edit",
                        icon = Icons.Default.Edit,
                        label = "Edit selected",
                        onClick = onEditSelected
                    ),
                    TopBarItem(
                        id = "delete",
                        icon = Icons.Default.Delete,
                        label = "Delete selected",
                        onClick = onDeleteSelected
                    )
                )
            ),

            TopBarActionGroup(
                items = listOf(
                    TopBarItem(
                        id = "export",
                        icon = Icons.Default.Download,
                        label = "Export selected",
                        onClick = onExportSelected
                    )
                )
            )
        ),

        itemSize = TopBarItemSize.MEDIUM
    )
}
