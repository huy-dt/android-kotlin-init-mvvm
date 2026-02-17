package com.huydt.uikit.topbar.internal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.huydt.uikit.icon.AppIcon
import com.huydt.uikit.icon.model.*
import com.huydt.uikit.topbar.model.TopBarActionGroup
import com.huydt.uikit.topbar.model.TopBarItem

/**
 * Overflow menu component hiển thị "More" button và dropdown menu
 *
 * @param actionGroups Danh sách các action groups
 * @param itemSize Kích thước của items
 * @param showLabel Hiển thị label hay không
 * @param colors Màu sắc cho menu
 */
@Composable
internal fun OverflowMenu(
    actionGroups: List<TopBarActionGroup>,
    itemSize: IconSize,
    showLabel: Boolean,
    colors: IconColors
) {
    var expanded by remember { mutableStateOf(false) }

    // More button
    TopBarItemView(
        item = TopBarItem(
            id = "more",
            icon = Icons.Default.MoreVert,
            label = "More"
        ),
        colors = colors,
        itemSize = itemSize,
        showLabel = showLabel,
        onItemClick = { expanded = true },
        selected = false
    )

    // Dropdown menu
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier
            // .width(200.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        actionGroups.forEachIndexed { groupIndex, group ->
            // Group title (nếu có)
            group.title?.let { title ->
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            // Group items
            group.items.forEach { item ->
                AppIcon(
                    isVertical = false,
                    icon = item.icon,
                    label = item.label,
                    size = IconSize.MEDIUM,
                    colors = IconColors(
                        color = if (item.enabled) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                        },
                        background = Color.Transparent
                    ),
                    enabled = item.enabled,
                    badgeCount = item.badgeCount,
                    onClick = {
                        item.onClick?.invoke() // Thêm ?.invoke() nếu có thể null
                        expanded = false
                    },
                    contentPadding = 8.dp
                )
            }

            // Divider giữa các groups (không phải group cuối)
            if (groupIndex < actionGroups.lastIndex) {
                Divider(
                    modifier = Modifier.padding(vertical = 4.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
    }
}