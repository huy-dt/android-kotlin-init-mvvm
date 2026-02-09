package com.huydt.uikit.topbar.internal

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.huydt.uikit.topbar.TopBarColors
import com.huydt.uikit.topbar.model.TopBarActionGroup

@Composable
internal fun _OverflowMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    actionGroups: List<TopBarActionGroup>,
    colors: TopBarColors
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        actionGroups.forEachIndexed { index, group ->

            group.title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelMedium,
                    color = colors.content,
                    modifier = Modifier.padding(
                        start = 12.dp,
                        top = if (index > 0) 6.dp else 0.dp
                    )
                )
            }

            group.actions.forEach { action ->

                val itemColor = when {
                    !action.enabled ->
                        colors.content.copy(alpha = 0.38f)

                    action.contentColor != null ->
                        action.contentColor

                    action.selected ->
                        MaterialTheme.colorScheme.primary

                    else ->
                        colors.content
                }

                DropdownMenuItem(
                    text = {
                        Text(
                            text = action.label.orEmpty(),
                            color = itemColor
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = action.icon,
                            contentDescription = action.label,
                            tint = itemColor
                        )
                    },
                    enabled = action.enabled,
                    onClick = {
                        onDismiss()
                        action.onClick()
                    }
                )
            }

            if (index < actionGroups.lastIndex) {
                Divider(color = colors.divider)
            }
        }
    }
}
