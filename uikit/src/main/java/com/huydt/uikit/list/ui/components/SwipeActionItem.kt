package com.huydt.uikit.list.ui.components

import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

enum class SwipeDragValue {
    Settled,        // Đóng
    StartAction,    // Mở bên trái
    EndAction       // Mở bên phải
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeActionItem(
    isOpened: Boolean = false,
    onOpened: () -> Unit = {},
    onClosed: () -> Unit = {},
    leftActions: List<@Composable (onClose: () -> Unit) -> Unit> = emptyList(),
    rightActions: List<@Composable (onClose: () -> Unit) -> Unit> = emptyList(),
    leftTotalPx: Float,  // Pixel đã đo từ lớp Base
    rightTotalPx: Float, // Pixel đã đo từ lớp Base
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    val dragState = remember(leftTotalPx, rightTotalPx) {
        AnchoredDraggableState(
            initialValue = SwipeDragValue.Settled,
            anchors = DraggableAnchors {
                SwipeDragValue.Settled at 0f
                if (leftActions.isNotEmpty()) SwipeDragValue.StartAction at leftTotalPx
                if (rightActions.isNotEmpty()) SwipeDragValue.EndAction at -rightTotalPx
            },
            positionalThreshold = { it * 0.5f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            animationSpec = spring()
        )
    }

    // Giữ logic mở nhanh như class cũ
    LaunchedEffect(dragState.targetValue) {
        if (dragState.targetValue != SwipeDragValue.Settled) onOpened()
    }

    LaunchedEffect(isOpened) {
        if (!isOpened &&
            (dragState.currentValue != SwipeDragValue.Settled ||
             dragState.targetValue != SwipeDragValue.Settled)
        ) {
            scope.launch {
                withContext(NonCancellable) {
                    runCatching { dragState.animateTo(SwipeDragValue.Settled) }
                        .onFailure { dragState.snapTo(SwipeDragValue.Settled) }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 64.dp)
            .height(IntrinsicSize.Min)
            .clip(RectangleShape)
    ) {

        /* BACKGROUND */
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            leftActions.forEach { action ->
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                ) {
                    action {
                        scope.launch {
                            withContext(NonCancellable) {
                                dragState.animateTo(SwipeDragValue.Settled)
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            rightActions.forEach { action ->
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                ) {
                    action {
                        scope.launch {
                            withContext(NonCancellable) {
                                dragState.animateTo(SwipeDragValue.Settled)
                            }
                        }
                    }
                }
            }
        }

        /* FOREGROUND */
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset {
                    IntOffset(dragState.requireOffset().roundToInt(), 0)
                }
                .anchoredDraggable(
                    state = dragState,
                    orientation = Orientation.Horizontal,
                    enabled = leftActions.isNotEmpty() || rightActions.isNotEmpty()
                )
                .background(MaterialTheme.colorScheme.surface)
        ) {
            content()
        }
    }
}
