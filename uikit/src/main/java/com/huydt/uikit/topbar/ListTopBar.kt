package com.huydt.uikit.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Upload
import androidx.compose.runtime.Composable
import com.huydt.uikit.topbar.TopBar
import com.huydt.uikit.topbar.IconColorDefaults
import com.huydt.uikit.topbar.model.TopBarActionGroup
import com.huydt.uikit.topbar.model.TopBarItem
import com.huydt.uikit.icon.model.IconSize

/**
 * TopBar chế độ bình thường cho mọi màn hình dùng ListViewModel.
 *
 * @param filterBadge       Số filter đang active — hiện badge trên nút Filter
 * @param onBack            Null = ẩn nút back
 * @param onSearch          Null = ẩn nút search
 * @param onRefresh         Luôn hiện
 * @param onFilter          Null = ẩn nút filter
 * @param onSort            Null = ẩn nút sort
 * @param onImport          Null = ẩn khỏi overflow
 * @param onExport          Null = ẩn khỏi overflow
 * @param onDeleteAll       Null = ẩn khỏi overflow
 * @param extraMoreActions  Inject thêm action group tuỳ theo từng feature
 */
@Composable
fun ListTopBar(
    filterBadge: Int = 0,
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
                    TopBarItem(id = "import", icon = Icons.Default.Upload, label = "Import", onClick = onImport)
                )
                if (onExport != null) add(
                    TopBarItem(id = "export", icon = Icons.Default.Download, label = "Export", onClick = onExport)
                )
            }
            if (dataItems.isNotEmpty()) {
                add(TopBarActionGroup(id = "data", title = "Data", items = dataItems))
            }

            val dangerItems = buildList {
                if (onDeleteAll != null) add(
                    TopBarItem(id = "delete_all", icon = Icons.Default.DeleteForever, label = "Delete All", onClick = onDeleteAll)
                )
            }
            if (dangerItems.isNotEmpty()) {
                add(TopBarActionGroup(id = "danger", title = "Danger Zone", items = dangerItems))
            }

            addAll(extraMoreActions)
        }
    )
}