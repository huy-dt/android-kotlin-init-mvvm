package com.huydt.uikit.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import com.huydt.uikit.icon.model.IconSize
import com.huydt.uikit.topbar.model.TopBarActionGroup
import com.huydt.uikit.topbar.model.TopBarItem

/**
 * TopBar chế độ bình thường cho mọi màn hình dùng ListViewModel.
 */
@Composable
fun ListTopBar(
    filterBadge: Int = 0,

    // 🔥 Hoisted selected state
    selectedId: String? = null,
    onSelectedChange: (String?) -> Unit = {},

    onBack: (() -> Unit)? = null,
    onSearch: (() -> Unit)? = null,
    onRefresh: () -> Unit,
    onFilter: (() -> Unit)? = null,
    onSort: (() -> Unit)? = null,
    onImport: (() -> Unit)? = null,
    onExport: (() -> Unit)? = null,
    onDeleteAll: (() -> Unit)? = null,
    extraMoreActions: List<TopBarActionGroup> = emptyList()
) {
    TopBar(
        itemSize = IconSize.SMALL,
        showLabel = true,
        colors = IconColorDefaults.colors(),

        // 👇 Forward state xuống TopBar
        selectedId = selectedId,
        onSelectedChange = onSelectedChange,

        /* ──────────────── LEFT ──────────────── */
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

        /* ──────────────── MID ──────────────── */
        midActions = buildList {
            if (onFilter != null) {
                add(
                    TopBarItem(
                        id = "filter",
                        icon = Icons.Default.FilterList,
                        label = "Filter",
                        badgeCount = filterBadge.takeIf { it > 0 },
                        onClick = onFilter
                    )
                )
            }
            if (onSort != null) {
                add(
                    TopBarItem(
                        id = "sort",
                        icon = Icons.Default.Sort,
                        label = "Sort",
                        onClick = onSort
                    )
                )
            }
        },

        /* ──────────────── RIGHT ──────────────── */
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

        /* ──────────────── OVERFLOW ──────────────── */
        moreActions = buildList {
            val dataItems = buildList {
                if (onImport != null) add(
                    TopBarItem("import", Icons.Default.Upload, "Import", onClick = onImport)
                )
                if (onExport != null) add(
                    TopBarItem("export", Icons.Default.Download, "Export", onClick = onExport)
                )
            }
            if (dataItems.isNotEmpty()) {
                add(TopBarActionGroup("data", "Data", dataItems))
            }

            val dangerItems = buildList {
                if (onDeleteAll != null) add(
                    TopBarItem("delete_all", Icons.Default.DeleteForever, "Delete All", onClick = onDeleteAll)
                )
            }
            if (dangerItems.isNotEmpty()) {
                add(TopBarActionGroup("danger", "Danger Zone", dangerItems))
            }

            addAll(extraMoreActions)
        }
    )
}