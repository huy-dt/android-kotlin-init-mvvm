package com.huydt.uikit.list.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> ListItemContent(
    item: T,
    isSelected: Boolean,
    isSelectionMode: Boolean,
    haptic: HapticFeedback,
    onItemClick: ((T) -> Unit)?,
    onToggleSelection: ((T) -> Unit)?,
    itemContent: @Composable (T, Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    if (isSelectionMode) onToggleSelection?.invoke(item)
                    else onItemClick?.invoke(item)
                },
                onLongClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onToggleSelection?.invoke(item)
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isSelectionMode) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onToggleSelection?.invoke(item) },
                modifier = Modifier.padding(start = 12.dp)
            )
        }

        Box(modifier = Modifier.weight(1f)) {
            itemContent(item, isSelected)
        }
    }
}
