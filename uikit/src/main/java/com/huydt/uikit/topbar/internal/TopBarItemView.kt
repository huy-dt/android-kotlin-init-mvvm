package com.huydt.uikit.topbar.internal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.huydt.uikit.topbar.TopBarColors
import com.huydt.uikit.topbar.model.TopBarItem
import com.huydt.uikit.topbar.model.TopBarItemSize

@Composable
internal fun TopBarItemView(
    item: TopBarItem,
    itemSize: TopBarItemSize,
    showLabel: Boolean,
    colors: TopBarColors
) {
    val indicatorColor =
        if (item.selected) MaterialTheme.colorScheme.primary else androidx.compose.ui.graphics.Color.Transparent

    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .clickable(enabled = item.enabled) { item.onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val tint = when {
            !item.enabled -> colors.content.copy(alpha = 0.38f)
            item.contentColor != null -> item.contentColor
            item.selected -> MaterialTheme.colorScheme.primary
            else -> colors.content
        }

        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            tint = tint,
            modifier = Modifier.size(itemSize.iconSize)
        )

        if (showLabel && item.label != null) {
            Text(
                text = item.label,
                color = tint
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .height(2.dp)
                .width(16.dp)
                .background(indicatorColor)
        )
    }
}
