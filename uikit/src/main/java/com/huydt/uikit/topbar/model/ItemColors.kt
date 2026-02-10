package com.huydt.uikit.topbar.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class ItemColors(
    val background: Color,
    val content: Color
)

object ItemColorDefaults {

    fun colors(
        background: Color = Color.Transparent,
        content: Color = Color.Unspecified
    ): ItemColors = ItemColors(
        background = background,
        content = content
    )
}
