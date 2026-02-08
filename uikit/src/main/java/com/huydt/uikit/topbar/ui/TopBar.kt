package com.huydt.uikit.topbar.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.huydt.uikit.topbar.model.TopBarItem
import com.huydt.uikit.topbar.model.TopBarItemSize
import androidx.compose.foundation.background
import androidx.compose.ui.Alignment

@Composable
private fun TopBarDivider() {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(1.dp)
            .padding(vertical = 8.dp)
            .background(MaterialTheme.colorScheme.outlineVariant)
    )
}

@Composable
fun TopBar(
    leftActions: List<TopBarItem> = emptyList(),
    midActions: List<TopBarItem> = emptyList(),
    rightActions: List<TopBarItem> = emptyList(),
    moreActions: List<TopBarItem> = emptyList(),
    itemSize: TopBarItemSize = TopBarItemSize.Medium,
    showLabel: Boolean = true,
    backgroundColor: androidx.compose.ui.graphics.Color =
        MaterialTheme.colorScheme.surface,
    tonalElevation: androidx.compose.ui.unit.Dp = 2.dp,
    modifier: Modifier = Modifier
) {
    Surface(
        color = backgroundColor,
        tonalElevation = tonalElevation
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(itemSize.containerSize),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // --- LEFT ---
            if (leftActions.isNotEmpty()) {
                Row {
                    leftActions.forEach {
                        TopBarItemView(it, itemSize, showLabel)
                    }
                }
            }

            // Divider: Left | Mid
            if (leftActions.isNotEmpty() && midActions.isNotEmpty()) {
                TopBarDivider()
            }

            // --- MID (scrollable) ---
            if (midActions.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(midActions, key = { it.id }) {
                        TopBarItemView(it, itemSize, showLabel)
                    }
                }
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            // Divider: Mid | Right
            if (
                (midActions.isNotEmpty() || leftActions.isNotEmpty()) &&
                (rightActions.isNotEmpty() || moreActions.isNotEmpty())
            ) {
                TopBarDivider()
            }

            // --- RIGHT + MORE ---
            if (rightActions.isNotEmpty() || moreActions.isNotEmpty()) {
                Row {
                    rightActions.forEach {
                        TopBarItemView(it, itemSize, showLabel)
                    }

                    if (moreActions.isNotEmpty()) {
                        OverflowMenu(
                            actions = moreActions,
                            itemSize = itemSize,
                            showLabel = showLabel
                        )
                    }
                }
            }
        }
    }
}

