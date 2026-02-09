package com.huydt.uikit.topbar.model

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

sealed class TopBarItemSize(
    val containerSize: Dp,
    val iconSize: Dp,
    val textSize: TextUnit
) {
    object SMALL : TopBarItemSize(
        containerSize = 40.dp,
        iconSize = 18.dp,
        textSize = 12.sp
    )

    object MEDIUM : TopBarItemSize(
        containerSize = 48.dp,
        iconSize = 20.dp,
        textSize = 14.sp
    )

    object LARGE : TopBarItemSize(
        containerSize = 56.dp,
        iconSize = 24.dp,
        textSize = 16.sp
    )
}
