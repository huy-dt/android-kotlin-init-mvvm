package com.xxx.app.feature_user.ui.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material.icons.filled.Upload
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.huydt.uikit.topbar.TopBar
import com.huydt.uikit.topbar.IconColorDefaults
import com.huydt.uikit.topbar.model.TopBarActionGroup
import com.huydt.uikit.topbar.model.TopBarItem
import com.huydt.uikit.icon.model.IconSize

/**
 * TopBar cho User List screen
 *
 * @param onBack Callback khi click back (null = disable)
 * @param onRefresh Callback khi click refresh
 * @param onSearch Callback khi click search (null = disable)
 * @param onAdd Callback khi click add (null = disable)
 * @param onImport Callback khi click import (null = disable)
 * @param onExport Callback khi click export (null = disable)
 * @param onDeleteAll Callback khi click delete all (null = disable)
 */
@Composable
fun UserListTopBar(
    onBack: (() -> Unit)? = null,
    onRefresh: () -> Unit,
    onSearch: (() -> Unit)? = null,
    onAdd: (() -> Unit)? = null,
    onImport: (() -> Unit)? = null,
    onExport: (() -> Unit)? = null,
    onDeleteAll: (() -> Unit)? = null
) {

    TopBar(
        itemSize = IconSize.SMALL,
        showLabel = true,
        colors = IconColorDefaults.colors(
            color = Color(0xFFE6E6E6),
            // background = Color.White
        ),

        /* ---------------- LEFT ---------------- */
        leftActions = buildList {
            if (onBack != null) {
                add(
                    TopBarItem(
                        id = "back",
                        icon = Icons.Default.ArrowBack,
                        label = "Back",
                        onClick = onBack
                    )
                )
            }
        },

        /* ---------------- MID (tab-like filters) ---------------- */
        midActions = listOf(
            TopBarItem(
                id = "filter",
                icon = Icons.Default.FilterList,
                label = "Filter",
                selected = true,
                onClick = { /* Handle filter */ }
            ),
            TopBarItem(
                id = "sort",
                icon = Icons.Default.Sort,
                label = "Sort",
                onClick = { /* Handle sort */ }
            ),
            TopBarItem(
                id = "role",
                icon = Icons.Default.Badge,
                label = "Role",
                onClick = { /* Handle role filter */ }
            ),
            TopBarItem(
                id = "status",
                icon = Icons.Default.ToggleOn,
                label = "Status",
                onClick = { /* Handle status filter */ }
            )
        ),

        /* ---------------- RIGHT ---------------- */
        rightActions = buildList {
            if (onSearch != null) {
                add(
                    TopBarItem(
                        id = "search",
                        icon = Icons.Default.Search,
                        label = "Search",
                        onClick = onSearch
                    )
                )
            }
            add(
                TopBarItem(
                    id = "refresh",
                    icon = Icons.Default.Refresh,
                    label = "Refresh",
                    onClick = onRefresh
                )
            )
        },

        /* ---------------- OVERFLOW ---------------- */
        moreActions = listOf(
            // Import/Export group
            TopBarActionGroup(
                id = "import_export",
                title = "Data Management", // Optional title
                items = buildList {
                    add(
                        TopBarItem(
                            id = "import",
                            icon = Icons.Default.Upload,
                            label = "Import",
                            onClick = onImport
                        )
                    )
                    add(
                        TopBarItem(
                            id = "export",
                            icon = Icons.Default.Download,
                            label = "Export",
                            onClick = onExport
                        )
                    )
                
                }
            ),

            // Danger zone group
            TopBarActionGroup(
                id = "danger",
                title = "Danger Zone", // Optional title
                items = buildList {
                    add(
                        TopBarItem(
                            id = "delete_all",
                            icon = Icons.Default.DeleteForever,
                            label = "Delete All",
                            onClick = onDeleteAll
                        )
                    )
                }
            )
        ).filter { it.items.isNotEmpty() } // Chỉ giữ groups có items
    )
}