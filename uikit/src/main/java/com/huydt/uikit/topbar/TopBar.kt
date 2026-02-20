package com.huydt.uikit.topbar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.huydt.uikit.icon.model.IconColors
import com.huydt.uikit.icon.model.IconSize
import com.huydt.uikit.topbar.internal.OverflowMenu
import com.huydt.uikit.topbar.internal.TopBarDivider
import com.huydt.uikit.topbar.internal.TopBarItemView
import com.huydt.uikit.topbar.model.TopBarActionGroup
import com.huydt.uikit.topbar.model.TopBarItem

@Composable
fun TopBar(
    leftActions: List<TopBarItem> = emptyList(),
    midActions: List<TopBarItem> = emptyList(),
    rightActions: List<TopBarItem> = emptyList(),
    moreActions: List<TopBarActionGroup> = emptyList(),

    // 🔥 Hoisted state
    selectedId: String? = null,
    onSelectedChange: (String?) -> Unit = {},

    itemSize: IconSize = IconSize.MEDIUM,
    showLabel: Boolean = true,
    colors: IconColors = IconColorDefaults.colors(),
    tonalElevation: Dp = TopBarDefaults.tonalElevation,
    modifier: Modifier = Modifier
) {

    Surface(
        color = colors.background,
        tonalElevation = tonalElevation
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(itemSize.containerSize),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // ===== LEFT =====
            if (leftActions.isNotEmpty()) {
                Row {
                    leftActions.forEach { item ->
                        TopBarItemView(
                            item = item,
                            itemSize = itemSize,
                            colors = colors,
                            showLabel = showLabel,
                            onItemClick = { clicked ->
                                clicked.onClick?.invoke()
                            }
                        )
                    }
                }
            }

            if (leftActions.isNotEmpty() && midActions.isNotEmpty()) {
                TopBarDivider(colors.background)
            }

            // ===== MID =====
            if (midActions.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    items(
                        items = midActions,
                        key = { it.id }
                    ) { item ->

                        val isSelected = item.id == selectedId

                        TopBarItemView(
                            item = item,
                            selected = isSelected,
                            itemSize = itemSize,
                            colors = colors,
                            showLabel = showLabel,
                            onItemClick = { clicked ->
                                val newId =
                                    if (selectedId == clicked.id) null
                                    else clicked.id

                                onSelectedChange(newId)
                                clicked.onClick?.invoke()
                            }
                        )
                    }
                }
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            if (
                (leftActions.isNotEmpty() || midActions.isNotEmpty()) &&
                (rightActions.isNotEmpty() || moreActions.isNotEmpty())
            ) {
                TopBarDivider(colors.background)
            }

            // ===== RIGHT =====
            if (rightActions.isNotEmpty() || moreActions.isNotEmpty()) {
                Row {
                    rightActions.forEach { item ->
                        TopBarItemView(
                            item = item,
                            itemSize = itemSize,
                            colors = colors,
                            showLabel = showLabel,
                            onItemClick = { clicked ->
                                clicked.onClick?.invoke()
                            }
                        )
                    }

                    if (moreActions.isNotEmpty()) {
                        OverflowMenu(
                            actionGroups = moreActions,
                            itemSize = itemSize,
                            showLabel = showLabel,
                            colors = colors
                        )
                    }
                }
            }
        }
    }
}

object TopBarDefaults {
    val tonalElevation: Dp = 3.dp
}