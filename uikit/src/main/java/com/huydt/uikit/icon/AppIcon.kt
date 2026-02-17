package com.huydt.uikit.icon

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.sp
import com.huydt.uikit.icon.model.*
import androidx.compose.material3.MaterialTheme

enum class AppIconOrientation {
    Vertical,
    Horizontal
}
@Composable
fun AppIcon(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    label: String? = null,
    isVertical: Boolean = true,
    size: IconSize = IconSize.MEDIUM,
    colors: IconColors = IconColors(),
    enabled: Boolean = true,
    selected: Boolean = false,
    showIndicator: Boolean = false,
    badgeCount: Int? = null,
    badgeConfig: BadgeConfig = BadgeConfig.Default,
    onClick: (() -> Unit)? = null,
    shape: Shape = RectangleShape,
    contentPadding: Dp = 8.dp,
    spacing: Dp = 2.dp,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    if (icon == null && label == null) return

    val density = LocalDensity.current

    val orientation =
        if (isVertical) AppIconOrientation.Vertical
        else AppIconOrientation.Horizontal

    // ===== Animations =====

    val animatedBackground by animateColorAsState(
        targetValue = when {
            !selected -> colors.background
            colors.background != Color.Transparent -> colors.background
            else -> colors.color.copy(alpha = 0.12f)
        },
        label = "bg_animation"
    )

    val scale by animateFloatAsState(
        targetValue = if (selected) 1.08f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 300f),
        label = "scale_animation"
    )

    val indicatorAlpha by animateFloatAsState(
        targetValue = if (selected && showIndicator) 1f else 0f,
        label = "indicator_animation"
    )

    val contentAlpha = if (enabled) 1f else 0.5f

    Box(
        modifier = modifier
            .fillMaxHeight()
            .scale(scale)
            .alpha(contentAlpha)
            .clip(shape)
            .background(animatedBackground)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = rememberRipple(bounded = true),
                        enabled = enabled,
                        onClick = onClick
                    )
                } else Modifier
            )
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {

        val content: @Composable () -> Unit = {

            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = label,
                    modifier = Modifier.size(size.icon),
                    tint = colors.color
                )
            }

            if (icon != null && label != null) {
                if (orientation == AppIconOrientation.Vertical) {
                    Spacer(modifier = Modifier.height(spacing))
                } else {
                    Spacer(modifier = Modifier.width(spacing))
                }
            }

            label?.let {
                Text(
                    text = it,
                    fontSize = if (icon == null)
                        with(density) { size.icon.toSp() }
                    else
                        size.label,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = colors.color,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        if (orientation == AppIconOrientation.Vertical) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                content()
            }
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                content()
            }
        }

        // ===== Indicator (gạch dưới) =====
        if (showIndicator) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(0.6f)
                    .height(2.dp)
                    .alpha(indicatorAlpha)
                    .background(colors.color)
            )
        }

        // ===== Badge =====
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
