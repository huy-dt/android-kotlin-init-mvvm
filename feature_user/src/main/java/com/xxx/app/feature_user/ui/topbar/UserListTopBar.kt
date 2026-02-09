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
fun UserListTopBar(
    onBack: (() -> Unit)?,
    onRefresh: () -> Unit,
    onSearch: (() -> Unit)? = null,
    onAdd: (() -> Unit)? = null,
    onImport: (() -> Unit)? = null,
    onExport: (() -> Unit)? = null,
    onDeleteAll: (() -> Unit)? = null
) {
    TopBar(
        showLabel = false,

        /* ---------------- LEFT ---------------- */
        leftActions = listOf(
            TopBarItem(
                id = "back",
                icon = Icons.Default.ArrowBack,
                label = "Back",
                onClick = onBack
            )
        ),

        /* ---------------- MID (scrollable filters) ---------------- */
        midActions = listOf(
            TopBarItem(
                id = "filter",
                icon = Icons.Default.FilterList,
                label = "Filter"
            ),
            TopBarItem(
                id = "sort",
                icon = Icons.Default.Sort,
                label = "Sort"
            ),
            TopBarItem(
                id = "role",
                icon = Icons.Default.Badge,
                label = "Role"
            ),
            TopBarItem(
                id = "status",
                icon = Icons.Default.ToggleOn,
                label = "Status"
            )
        ),

        /* ---------------- RIGHT ---------------- */
        rightActions = listOf(
            TopBarItem(
                id = "search",
                icon = Icons.Default.Search,
                label = "Search",
                onClick = onSearch
            ),
            TopBarItem(
                id = "refresh",
                icon = Icons.Default.Refresh,
                label = "Refresh",
                onClick = onRefresh
            ),
            TopBarItem(
                id = "add",
                icon = Icons.Default.Add,
                label = "Add",
                onClick = onAdd
            )
        ),

        /* ---------------- OVERFLOW MENU (GROUPED) ---------------- */
        moreActions = listOf(

            // Import / Export
            TopBarActionGroup(
                items = listOf(
                    TopBarItem(
                        id = "import",
                        icon = Icons.Default.Upload,
                        label = "Import",
                        onClick = onImport
                    ),
                    TopBarItem(
                        id = "export",
                        icon = Icons.Default.Download,
                        label = "Export",
                        onClick = onExport
                    )
                )
            ),

            // Danger zone
            TopBarActionGroup(
                items = listOf(
                    TopBarItem(
                        id = "delete_all",
                        icon = Icons.Default.DeleteForever,
                        label = "Delete ALL",
                        tint = Color.Red,
                        onClick = onDeleteAll
                    )
                )
            )
        ),

        itemSize = TopBarItemSize.MEDIUM
    )
}
