package com.huydt.uikit.topbar

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.huydt.uikit.icon.model.IconColors

object IconColorDefaults { // Đổi tên từ ItemColorDefaults thành IconColorDefaults
    @Composable
    fun colors(
        color: Color = Color.White,
        background: Color = Color.Red
    ): IconColors = IconColors(color = color, background = background)
}