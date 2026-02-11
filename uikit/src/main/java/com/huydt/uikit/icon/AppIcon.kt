package com.huydt.uikit.icon

import com.huydt.uikit.icon.model.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


/**
 * Component Icon có thể tùy chỉnh với label, badge và nhiều trạng thái
 *
 * @param modifier Modifier cho component
 * @param icon ImageVector cho icon (nullable, nhưng phải có ít nhất icon hoặc label)
 * @param label Text label cho icon (nullable, nhưng phải có ít nhất icon hoặc label)
 * @param size Kích thước của icon và label
 * @param colors Màu sắc của component
 * @param enabled Trạng thái enable/disable
 * @param selected Trạng thái selected (hiển thị background highlight)
 * @param badgeCount Số đếm badge (null hoặc <= 0 sẽ không hiển thị)
 * @param badgeConfig Cấu hình cho badge
 * @param onClick Callback khi click (nullable)
 * @param shape Hình dạng của background
 * @param contentPadding Padding bên trong component
 * @param spacing Khoảng cách giữa icon và label
 * @param interactionSource MutableInteractionSource để tracking interactions
 */
@Composable
fun AppIcon(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    label: String? = null,
    size: IconSize = IconSize.Medium,
    colors: IconColors = IconColors(),
    enabled: Boolean = true,
    selected: Boolean = false,
    badgeCount: Int? = null,
    badgeConfig: BadgeConfig = BadgeConfig.Default,
    onClick: (() -> Unit)? = null,
    shape: Shape = CircleShape,
    contentPadding: Dp = 8.dp,
    spacing: Dp = 2.dp,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    // Validation: Ít nhất phải có icon hoặc label
    if (icon == null && label == null) return

    // Tính toán background color dựa trên trạng thái selected
    val backgroundColor = when {
        !selected -> colors.background
        colors.background != Color.Transparent -> colors.background
        else -> colors.color.copy(alpha = 0.12f)
    }

    // Tính toán alpha dựa trên trạng thái enabled
    val contentAlpha = if (enabled) 1f else 0.5f

    Box(
        modifier = modifier
            .wrapContentSize()
            .alpha(contentAlpha)
            .clip(shape)
            .background(backgroundColor)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = rememberRipple(bounded = true),
                        enabled = enabled,
                        onClick = onClick
                    )
                } else {
                    Modifier
                }
            )
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        // Main content: Icon và Label
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icon
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = label,
                    modifier = Modifier.size(size.icon),
                    tint = colors.color
                )
            }

            // Spacing giữa icon và label
            if (icon != null && label != null) {
                Spacer(modifier = Modifier.height(spacing))
            }

            // Label
            label?.let {
                Text(
                    text = it,
                    fontSize = size.label,
                    fontWeight = FontWeight.Bold,
                    color = colors.color,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Badge
        if (shouldShowBadge(badgeCount)) {
            Badge(
                count = badgeCount!!,
                config = badgeConfig,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 4.dp, y = (-4).dp)
            )
        }
    }
}
