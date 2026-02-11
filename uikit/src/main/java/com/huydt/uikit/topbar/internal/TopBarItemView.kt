package com.huydt.uikit.topbar.internal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.huydt.uikit.icon.AppIcon
import com.huydt.uikit.icon.model.*
import com.huydt.uikit.topbar.model.TopBarItem
import com.huydt.uikit.topbar.model.TopBarItemSize

/**
 * Internal component để render một TopBarItem sử dụng AppIcon
 *
 * @param item TopBarItem data
 * @param itemSize Kích thước của item
 * @param showLabel Hiển thị label hay không
 */
@Composable
internal fun TopBarItemView(
    item: TopBarItem,
    itemSize: TopBarItemSize,
    showLabel: Boolean
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    
    // Tính toán màu sắc dựa trên trạng thái
    val iconColor = when {
        !item.enabled -> item.colors.content.copy(alpha = 0.38f)
        item.selected -> primaryColor
        else -> item.colors.content
    }

    // Background color cho selected state
    val backgroundColor = when {
        item.selected -> primaryColor.copy(alpha = 0.12f)
        else -> item.colors.background
    }

    Column(
        modifier = Modifier
            .height(itemSize.containerSize)
            .background(backgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // AppIcon với tất cả states
        AppIcon(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 6.dp),
            icon = item.icon,
            label = if (showLabel) item.label else null,
            size = IconSize(
                icon = itemSize.iconSize,
                label = itemSize.textSize
            ),
            colors = IconColors(
                color = iconColor,
                background = Color.Transparent
            ),
            enabled = item.enabled,
            selected = item.selected,
            badgeCount = item.badgeCount,
            onClick = item.onClick,
            contentPadding = 4.dp,
            spacing = 2.dp
        )

        // Selection indicator
        if (item.selected) {
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .height(2.dp)
                    .width(16.dp)
                    .background(primaryColor)
            )
        }
    }
}