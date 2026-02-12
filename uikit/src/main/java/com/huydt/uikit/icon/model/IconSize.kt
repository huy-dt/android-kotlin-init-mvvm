package com.huydt.uikit.icon.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Định nghĩa kích thước cho TopBarItem
 *
 * @param containerSize Chiều cao của container
 * @param size Kích thước icon
 * @param label Kích thước text
 */
@Immutable
data class IconSize(
    val containerSize: Dp,
    val icon: Dp,
    val label: TextUnit
) {
    companion object {
        /**
         * Small size - compact topbar
         */
        val SMALL = IconSize(
            containerSize = 48.dp,
            icon = 20.dp,
            label = 10.sp
        )

        /**
         * Medium size - default
         */
        val MEDIUM = IconSize(
            containerSize = 56.dp,
            icon = 24.dp,
            label = 12.sp
        )

        /**
         * Large size - spacious topbar
         */
        val LARGE = IconSize(
            containerSize = 64.dp,
            icon = 28.dp,
            label = 14.sp
        )
    }
}