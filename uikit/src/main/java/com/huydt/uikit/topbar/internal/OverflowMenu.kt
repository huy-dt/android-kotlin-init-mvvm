package com.huydt.uikit.topbar.internal

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import com.huydt.uikit.topbar.TopBarColors
import com.huydt.uikit.topbar.model.TopBarActionGroup
import com.huydt.uikit.topbar.model.TopBarItem
import com.huydt.uikit.topbar.model.TopBarItemSize

@Composable
internal fun OverflowMenu(
    actionGroups: List<TopBarActionGroup>,
    itemSize: TopBarItemSize,
    showLabel: Boolean,
    colors: TopBarColors
) {
    var expanded by remember { mutableStateOf(false) }

    val overflowItem = TopBarItem(
        id = "overflow",
        icon = Icons.Default.MoreVert,
        label = "More",
        onClick = { expanded = true }
    )

    TopBarItemView(
        item = overflowItem,
        itemSize = itemSize,
        showLabel = showLabel,
        colors = colors
    )

    _OverflowMenu(
        expanded = expanded,
        onDismiss = { expanded = false },
        actionGroups = actionGroups,
        colors = colors
    )
}
