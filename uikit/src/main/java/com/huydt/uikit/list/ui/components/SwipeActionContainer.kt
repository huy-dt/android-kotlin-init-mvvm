package com.huydt.uikit.list.ui.components

import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import androidx.compose.foundation.gestures.animateTo

enum class SwipeDragValue {
    Settled,
    StartAction,
    EndAction
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeActionContainer(
    isOpened: Boolean,
    swipeEnabled: Boolean,
    startWidthPx: Float,
    endWidthPx: Float,
    onRequestOpen: () -> Unit,
    onRequestClose: () -> Unit,
    startActions: @Composable RowScope.(onClose: () -> Unit) -> Unit,
    endActions: @Composable RowScope.(onClose: () -> Unit) -> Unit,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()

    val dragState = remember(startWidthPx, endWidthPx) {
        AnchoredDraggableState(
            initialValue = SwipeDragValue.Settled,
            anchors = DraggableAnchors {
                SwipeDragValue.Settled at 0f
                if (startWidthPx > 0f)
                    SwipeDragValue.StartAction at startWidthPx
                if (endWidthPx > 0f)
                    SwipeDragValue.EndAction at -endWidthPx
            },
            positionalThreshold = { it * 0.4f },
            velocityThreshold = { 800f },
            animationSpec = spring()
        )
    }

    /* coordinator control */
    LaunchedEffect(isOpened) {
        if (!isOpened) {
            dragState.animateTo(SwipeDragValue.Settled)
        }
    }

    LaunchedEffect(dragState.currentValue) {
        if (dragState.currentValue == SwipeDragValue.Settled)
            onRequestClose()
        else
            onRequestOpen()
    }

    Box(Modifier.fillMaxWidth()) {

        /* ---------- BACKGROUND ---------- */
        Row(Modifier.fillMaxSize()) {

            Row {
                startActions {
                    scope.launch {
                        dragState.animateTo(SwipeDragValue.Settled)
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            Row {
                endActions {
                    scope.launch {
                        dragState.animateTo(SwipeDragValue.Settled)
                    }
                }
            }
        }

        /* ---------- FOREGROUND ---------- */
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(dragState.requireOffset().roundToInt(), 0)
                }
                .then(
                    if (swipeEnabled)
                        Modifier.anchoredDraggable(
                            state = dragState,
                            orientation = Orientation.Horizontal
                        )
                    else Modifier
                )
                .background(MaterialTheme.colorScheme.surface)
        ) {
            content()
        }
    }
}
