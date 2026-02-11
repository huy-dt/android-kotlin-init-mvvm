package com.huydt.uikit.icon.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Định nghĩa màu sắc cho Icon component
 *
 * @param color Màu của icon và label
 * @param background Màu nền của component
 */
@Immutable
data class IconColors(
    val color: Color = Color.Black,
    val background: Color = Color.Transparent
) {
    companion object {

        fun primary(color: Color) = IconColors(
            color = color,
            background = Color.Transparent
        )

        fun filled(
            color: Color,
            background: Color
        ) = IconColors(
            color = color,
            background = background
        )
    }
}
