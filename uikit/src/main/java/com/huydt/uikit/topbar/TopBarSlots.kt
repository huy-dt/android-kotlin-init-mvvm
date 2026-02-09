package com.huydt.uikit.topbar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    colors: TopBarColors = TopBarDefaults.colors(),
    tonalElevation: androidx.compose.ui.unit.Dp = TopBarDefaults.tonalElevation,
    containerHeight: androidx.compose.ui.unit.Dp,
    left: @Composable RowScope.() -> Unit = {},
    center: @Composable RowScope.() -> Unit = {},
    right: @Composable RowScope.() -> Unit = {},
) {
    Surface(
        color = colors.background,
        tonalElevation = tonalElevation
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(containerHeight),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row { left() }
            Row(modifier = Modifier.weight(1f), horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center) {
                center()
            }
            Row { right() }
        }
    }
}
