package com.huydt.uikit.icon.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Định nghĩa cấu hình cho Badge
 *
 * @param backgroundColor Màu nền của badge
 * @param textColor Màu chữ của badge
 * @param maxCount Số lượng tối đa hiển thị trước khi hiển thị "99+"
 * @param minSize Kích thước tối thiểu của badge
 */
@Immutable
data class BadgeConfig(
    val backgroundColor: Color = Color.Red,
    val textColor: Color = Color.White,
    val maxCount: Int = 99,
    val minSize: Dp = 16.dp
) {
    companion object {
        val Default = BadgeConfig()
    }
}
