package com.huydt.uikit.topbar.internal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
internal fun TopBarDivider(
    color: Color
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(1.dp)
            .padding(vertical = 8.dp)
            .background(color)
    )
}
