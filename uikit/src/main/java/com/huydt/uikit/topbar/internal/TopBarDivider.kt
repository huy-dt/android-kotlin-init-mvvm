package com.huydt.uikit.topbar.internal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Internal divider component cho TopBar
 * Hiển thị vertical divider giữa các action groups
 *
 * @param backgroundColor Màu nền của TopBar
 */
@Composable
internal fun TopBarDivider(
    backgroundColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(1.dp)
            .padding(vertical = 8.dp)
            .background(backgroundColor.copy(alpha = 0.12f))
    )
}