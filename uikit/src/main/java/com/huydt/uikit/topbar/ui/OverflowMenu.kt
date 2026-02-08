package com.huydt.uikit.topbar.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.huydt.uikit.topbar.model.TopBarItem
import com.huydt.uikit.topbar.model.TopBarItemSize
import androidx.compose.ui.unit.DpOffset

@Composable
fun OverflowMenu(
    actions: List<TopBarItem>,
    itemSize: TopBarItemSize,
    showLabel: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }

    // 🔑 Box neo menu về góc phải của icon
    Box(contentAlignment = Alignment.TopEnd) {

        TopBarItemView(
            item = TopBarItem(
                id = "more",
                label = "Thêm",
                icon = Icons.Default.MoreVert,
                onClick = { expanded = true }
            ),
            showLabel = showLabel,
            size = itemSize
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(x = (-6).dp, y = 4.dp)
        ) {
            actions.forEach { action ->
                val contentColor =
                    if (action.tint == Color.Unspecified)
                        MaterialTheme.colorScheme.onSurface
                    else
                        action.tint

                DropdownMenuItem(
                    enabled = action.onClick != null,
                    onClick = {
                        expanded = false
                        action.onClick?.invoke()
                    },
                    text = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            action.icon?.let {
                                Icon(
                                    imageVector = it,
                                    contentDescription = action.label,
                                    tint = contentColor,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                            }

                            Text(
                                text = action.label.orEmpty(),
                                color = contentColor,
                                maxLines = 1
                            )
                        }
                    }
                )
            }
        }
    }
}
