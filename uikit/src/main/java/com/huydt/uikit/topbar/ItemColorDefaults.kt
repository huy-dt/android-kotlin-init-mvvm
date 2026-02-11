package com.huydt.uikit.topbar

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.huydt.uikit.topbar.model.ItemColors

object ItemColorDefaults {

    @Composable
    fun colors(
        content: Color = MaterialTheme.colorScheme.onSurface,
        background: Color = Color.Transparent
    ): ItemColors {
        return ItemColors(
            content = content,
            background = background
        )
    }
}
