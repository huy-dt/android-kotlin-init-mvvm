package com.huydt.uikit.topbar.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.huydt.uikit.topbar.model.TopBarItem
import com.huydt.uikit.topbar.model.TopBarItemSize

@Composable
fun TopBarItemView(
    item: TopBarItem,
    size: TopBarItemSize,
    showLabel: Boolean = true
) {
    val contentColor =
        if (item.tint == Color.Unspecified)
            MaterialTheme.colorScheme.onSurface
        else
            item.tint

    val labelColor =
        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.65f)

    Box(
        modifier = Modifier
            .size(size.containerSize)
            .clickable(
                enabled = item.enabled && item.onClick != null
            ) {
                item.onClick?.invoke()
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Icon
            item.icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = item.label,
                    modifier = Modifier.size(size.iconSize),
                    tint = contentColor
                )
            }

            // Label
            if (showLabel) {
                item.label?.let {
                    Text(
                        text = it,
                        fontSize = (size.textSize.value - 2).sp, // nhỏ hơn
                        color = labelColor,                     // nhạt hơn
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
