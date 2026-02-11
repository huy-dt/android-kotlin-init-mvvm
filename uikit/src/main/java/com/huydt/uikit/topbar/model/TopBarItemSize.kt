package com.huydt.uikit.topbar.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Định nghĩa kích thước cho TopBarItem
 *
 * @param containerSize Chiều cao của container
 * @param iconSize Kích thước icon
 * @param textSize Kích thước text
 */
@Immutable
data class TopBarItemSize(
    val containerSize: Dp,
    val iconSize: Dp,
    val textSize: TextUnit
) {
    companion object {
        /**
         * Small size - compact topbar
         */
        val SMALL = TopBarItemSize(
            containerSize = 48.dp,
            iconSize = 20.dp,
            textSize = 10.sp
        )

        /**
         * Medium size - default
         */
        val MEDIUM = TopBarItemSize(
            containerSize = 56.dp,
            iconSize = 24.dp,
            textSize = 12.sp
        )

        /**
         * Large size - spacious topbar
         */
        val LARGE = TopBarItemSize(
            containerSize = 64.dp,
            iconSize = 28.dp,
            textSize = 14.sp
        )
    }
}