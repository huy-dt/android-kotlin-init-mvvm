package com.huydt.uikit.list.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.huydt.uikit.list.ui.swipe.SwipeAction

/**
 * Render một swipe action button từ [SwipeAction] data class.
 * Caller truyền [onClose] để đóng swipe sau khi tap.
 */
@Composable
internal fun SwipeActionButton(
    action: SwipeAction,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bgColor = action.backgroundColor?.let { Color(it) }
        ?: MaterialTheme.colorScheme.primaryContainer
    val contentColor = action.textColor?.let { Color(it) }
        ?: MaterialTheme.colorScheme.onPrimaryContainer

    Box(
        modifier = modifier
            .fillMaxHeight()
            .background(bgColor)
            .widthIn(min = 64.dp)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        IconButton(
            onClick = {
                onClose()
                // caller gắn logic action bên ngoài nếu cần
            }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                action.icon?.let { iconRes ->
                    Icon(
                        painter = painterResource(iconRes),
                        contentDescription = action.label,
                        tint = contentColor,
                        modifier = Modifier.size(22.dp),
                    )
                    Spacer(Modifier.height(4.dp))
                }
                Text(
                    text = action.label,
                    style = MaterialTheme.typography.labelSmall,
                    color = contentColor,
                )
            }
        }
    }
}