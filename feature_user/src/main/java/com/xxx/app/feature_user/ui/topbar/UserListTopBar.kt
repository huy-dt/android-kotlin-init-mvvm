package com.xxx.app.feature_user.ui.topbar

import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.huydt.uikit.topbar.TopBar
import com.huydt.uikit.topbar.model.TopBarActionGroup
import com.huydt.uikit.topbar.model.TopBarItem
import androidx.compose.ui.graphics.Color
import com.huydt.uikit.topbar.model.ItemColorDefaults
import com.huydt.uikit.topbar.model.TopBarItemSize

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
        itemSize = TopBarItemSize.MEDIUM,
        showLabel = true,
        colors = ItemColorDefaults.colors(
            content = Color(0xFFE6E6E6) // ví dụ, hoặc màu bạn tự tính
        ),

        /* ---------------- LEFT ---------------- */
        leftActions = listOf(
            TopBarItem(
                id = "back",
                icon = Icons.Default.ArrowBack,
                label = "Back",
                enabled = onBack != null,
                onClick = { onBack?.invoke() }
            )
        ),

        /* ---------------- MID (tab-like filters) ---------------- */
        midActions = listOf(
            TopBarItem(
                id = "filter",
                icon = Icons.Default.FilterList,
                label = "Filter",
                selected = true,
                onClick = {}
            ),
            TopBarItem(
                id = "sort",
                icon = Icons.Default.Sort,
                label = "Sort",
                onClick = {}
            ),
            TopBarItem(
                id = "role",
                icon = Icons.Default.Badge,
                label = "Role",
                onClick = {}
            ),
            TopBarItem(
                id = "status",
                icon = Icons.Default.ToggleOn,
                label = "Status",
                onClick = {}
            )
        ),

        /* ---------------- RIGHT ---------------- */
        rightActions = listOf(
            TopBarItem(
                id = "search",
                icon = Icons.Default.Search,
                label = "Search",
                enabled = onSearch != null,
                onClick = { onSearch?.invoke() }
            ),
            TopBarItem(
                id = "refresh",
                icon = Icons.Default.Refresh,
                label = "Refresh",
                onClick = onRefresh
            )
        ),

        /* ---------------- OVERFLOW ---------------- */
        moreActions = listOf(

            TopBarActionGroup(
                id = "import_export",
                // title = "Data",
                actions = listOf(
                    TopBarItem(
                        id = "import",
                        icon = Icons.Default.Upload,
                        label = "Import",
                        enabled = onImport != null,
                        onClick = { onImport?.invoke() }
                    ),
                    TopBarItem(
                        id = "export",
                        icon = Icons.Default.Download,
                        label = "Export",
                        enabled = onExport != null,
                        onClick = { onExport?.invoke() }
                    )
                )
            ),

            TopBarActionGroup(
                id = "danger",
                // title = "Danger zone",
                actions = listOf(
                    TopBarItem(
                        id = "delete_all",
                        icon = Icons.Default.DeleteForever,
                        label = "Delete ALL",
                        enabled = onDeleteAll != null,
                        onClick = { onDeleteAll?.invoke() }
                    )
                )
            )
        )
    )
}
