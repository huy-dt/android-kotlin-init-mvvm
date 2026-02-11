package com.huydt.uikit.icon.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Định nghĩa kích thước cho Icon component
 *
 * @param icon Kích thước của icon
 * @param label Kích thước font của label
 */
@Immutable
data class IconSize(
    val icon: Dp = 24.dp,
    val label: TextUnit = 12.sp
) {
    companion object {
        val Small = IconSize(icon = 20.dp, label = 10.sp)
        val Medium = IconSize(icon = 24.dp, label = 12.sp)
        val Large = IconSize(icon = 32.dp, label = 14.sp)
    }
}
