package com.huydt.uikit.topbar.model

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class TopBarColors(
    val background: Color,
    val content: Color,
    val divider: Color
)

object TopBarDefaults {

    @Composable
    fun colors(
        background: Color = MaterialTheme.colorScheme.surface,
        content: Color = MaterialTheme.colorScheme.onSurface,
        divider: Color = MaterialTheme.colorScheme.outlineVariant,
    ): TopBarColors = TopBarColors(
        background = background,
        content = content,
        divider = divider
    )

    val tonalElevation: Dp = 2.dp
}
