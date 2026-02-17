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

@Composable
internal fun TopBarItemView(
    item: TopBarItem,
    colors: IconColors,
    itemSize: IconSize,
    showLabel: Boolean,
    selected: Boolean = false,
    onItemClick: ((TopBarItem) -> Unit)? = null
) {
    AppIcon(
        icon = item.icon,
        label = if (showLabel) item.label else null,
        size = itemSize,
        colors = item.colors ?: colors,
        enabled = item.enabled,
        selected = selected,
        showIndicator = false,
        badgeCount = item.badgeCount,
        onClick = { onItemClick?.invoke(item) },
        contentPadding = 4.dp,
        spacing = 2.dp
    )
}
