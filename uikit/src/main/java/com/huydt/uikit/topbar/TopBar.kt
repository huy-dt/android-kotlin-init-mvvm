package com.huydt.uikit.topbar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.statusBarsPadding
import com.huydt.uikit.topbar.internal.OverflowMenu
import com.huydt.uikit.topbar.internal.TopBarDivider
import com.huydt.uikit.topbar.internal.TopBarItemView
import com.huydt.uikit.topbar.model.TopBarActionGroup
import com.huydt.uikit.topbar.model.TopBarItem
import com.huydt.uikit.topbar.model.TopBarItemSize

@Composable
fun TopBar(
    leftActions: List<TopBarItem> = emptyList(),
    midActions: List<TopBarItem> = emptyList(),
    rightActions: List<TopBarItem> = emptyList(),
    moreActions: List<TopBarActionGroup> = emptyList(),
    itemSize: TopBarItemSize = TopBarItemSize.MEDIUM,
    showLabel: Boolean = true,
    colors: TopBarColors = TopBarDefaults.colors(),
    tonalElevation: androidx.compose.ui.unit.Dp = TopBarDefaults.tonalElevation,
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

            // LEFT
            if (leftActions.isNotEmpty()) {
                Row {
                    leftActions.forEach {
                        TopBarItemView(it, itemSize, showLabel, colors)
                    }
                }
            }

            if (leftActions.isNotEmpty() && midActions.isNotEmpty()) {
                TopBarDivider(colors)
            }

            // MID
            if (midActions.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(midActions, key = { it.id }) {
                        TopBarItemView(it, itemSize, showLabel, colors)
                    }
                }
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            if (
                (leftActions.isNotEmpty() || midActions.isNotEmpty()) &&
                (rightActions.isNotEmpty() || moreActions.isNotEmpty())
            ) {
                TopBarDivider(colors)
            }

            // RIGHT + MORE
            if (rightActions.isNotEmpty() || moreActions.isNotEmpty()) {
                Row {
                    rightActions.forEach {
                        TopBarItemView(it, itemSize, showLabel, colors)
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
