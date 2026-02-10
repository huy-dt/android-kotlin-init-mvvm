package com.huydt.uikit.topbar.internal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
// import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.huydt.uikit.topbar.TopBarColors
import com.huydt.uikit.topbar.model.TopBarItem
import com.huydt.uikit.topbar.model.TopBarItemSize


@Composable
internal fun TopBarItemView(
    item: TopBarItem,
    itemSize: TopBarItemSize,
    showLabel: Boolean
) {
    val indicatorColor =
        if (item.selected) MaterialTheme.colorScheme.primary
        else androidx.compose.ui.graphics.Color.Transparent

    val tint = when {
        !item.enabled -> item.colors.content.copy(alpha = 0.38f)
        item.colors.content != null -> item.colors.content
        item.selected -> MaterialTheme.colorScheme.primary
        else -> item.colors.content
    }

    Column(
        modifier = Modifier
            .height(itemSize.containerSize)
            .background(item.colors.background)
            .clickable(enabled = item.enabled) { item.onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 👉 CONTENT BLOCK (mập mạp ở đây)
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(
                    horizontal = 6.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (item.icon != null) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = tint,
                    modifier = Modifier.size(itemSize.iconSize)
                )

                if (showLabel && item.label != null) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = item.label,
                        color = tint.copy(alpha = 0.8f),
                        fontSize = itemSize.textSize,
                        fontWeight = FontWeight.SemiBold 
                    )
                }
            } else if (showLabel && item.label != null) {
                Text(
                    text = item.label,
                    color = tint.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // 👉 INDICATOR GẦN CONTENT
            if (item.selected) {
                Spacer(modifier = Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .height(2.dp)
                        .width(16.dp)
                        .background(indicatorColor)
                )
            }
        }
    }

}
