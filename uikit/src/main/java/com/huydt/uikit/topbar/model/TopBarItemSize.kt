package com.huydt.uikit.topbar.model

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class TopBarItemSize(
    val containerSize: Dp,
    val iconSize: Dp,
    val textSize: TextUnit
) {
    SMALL(
        containerSize = 40.dp,
        iconSize = 18.dp,
        textSize = 12.sp
    ),
    MEDIUM(
        containerSize = 48.dp,
        iconSize = 20.dp,
        textSize = 14.sp
    ),
    LARGE(
        containerSize = 56.dp,
        iconSize = 24.dp,
        textSize = 16.sp
    )
}
