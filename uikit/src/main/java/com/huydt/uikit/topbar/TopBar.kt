package com.huydt.uikit.topbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.huydt.uikit.topbar.internal.OverflowMenu
import com.huydt.uikit.topbar.internal.TopBarDivider
import com.huydt.uikit.topbar.internal.TopBarItemView
import com.huydt.uikit.topbar.model.TopBarActionGroup
import com.huydt.uikit.topbar.model.TopBarItem
import com.huydt.uikit.icon.model.IconSize
import com.huydt.uikit.icon.model.IconColors

/**
 * TopBar component cho list data với support cho left/mid/right actions và overflow menu
 *
 * @param leftActions Danh sách actions bên trái (thường là back, menu)
 * @param midActions Danh sách actions ở giữa (có thể scroll)
 * @param rightActions Danh sách actions bên phải (thường là search, filter)
 * @param moreActions Danh sách action groups trong overflow menu
 * @param itemSize Kích thước của items
 * @param showLabel Hiển thị label hay không
 * @param colors Màu sắc cho TopBar
 * @param tonalElevation Độ cao của surface
 * @param modifier Modifier cho component
 */
@Composable
fun TopBar(
    leftActions: List<TopBarItem> = emptyList(),
    midActions: List<TopBarItem> = emptyList(),
    rightActions: List<TopBarItem> = emptyList(),
    moreActions: List<TopBarActionGroup> = emptyList(),
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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // LEFT ACTIONS
            if (leftActions.isNotEmpty()) {
                Row {
                    leftActions.forEach { item ->
                        TopBarItemView(
                            item = item,
                            itemSize = itemSize,
                            colors = colors,
                            showLabel = showLabel
                        )
                    }
                }
            }

            // Divider giữa LEFT và MID
            if (leftActions.isNotEmpty() && midActions.isNotEmpty()) {
                TopBarDivider(colors.background)
            }

            // MID ACTIONS (scrollable)
            if (midActions.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(
                        items = midActions,
                        key = { it.id }
                    ) { item ->
                        TopBarItemView(
                            item = item,
                            itemSize = itemSize,
                            colors = colors,
                            showLabel = showLabel
                        )
                    }
                }
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            // Divider giữa MID và RIGHT
            if (
                (leftActions.isNotEmpty() || midActions.isNotEmpty()) &&
                (rightActions.isNotEmpty() || moreActions.isNotEmpty())
            ) {
                TopBarDivider(colors.background)
            }

            // RIGHT ACTIONS + OVERFLOW MENU
            if (rightActions.isNotEmpty() || moreActions.isNotEmpty()) {
                Row {
                    rightActions.forEach { item ->
                        TopBarItemView(
                            item = item,
                            itemSize = itemSize,
                            colors = colors,
                            showLabel = showLabel
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

/**
 * Default values cho TopBar
 */
object TopBarDefaults {
    val tonalElevation: Dp = 3.dp
}