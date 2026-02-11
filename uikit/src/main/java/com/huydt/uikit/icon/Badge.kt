package com.huydt.uikit.icon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.huydt.uikit.icon.model.BadgeConfig

/**
 * Kiểm tra có nên hiển thị badge hay không
 */
internal fun shouldShowBadge(count: Int?): Boolean {
    return count != null && count > 0
}

/**
 * Component Badge hiển thị số đếm
 */
@Composable
internal fun Badge(
    count: Int,
    config: BadgeConfig = BadgeConfig.Default,
    modifier: Modifier = Modifier
) {
    val displayText = if (count > config.maxCount) {
        "${config.maxCount}+"
    } else {
        count.toString()
    }

    Box(
        modifier = modifier
            .sizeIn(minWidth = config.minSize, minHeight = config.minSize)
            .background(config.backgroundColor, CircleShape)
            .padding(horizontal = 4.sp.value.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = displayText,
            color = config.textColor,
            fontSize = 9.sp,
            fontWeight = FontWeight.Black,
            maxLines = 1
        )
    }
}
