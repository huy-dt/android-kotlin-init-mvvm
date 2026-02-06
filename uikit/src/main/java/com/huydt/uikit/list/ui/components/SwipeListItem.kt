package com.huydt.uikit.list.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import com.huydt.uikit.list.ui.swipe.*

@Composable
fun <T> SwipeListItem(
    item: T,
    key: Any,
    coordinator: SwipeCoordinator<Any>,
    swipeEnabled: Boolean,
    startActions: List<SwipeAction<T>>,
    endActions: List<SwipeAction<T>>,
    content: @Composable () -> Unit
) {
    var startWidthPx by remember { mutableStateOf(0f) }
    var endWidthPx by remember { mutableStateOf(0f) }

    val isOpened = coordinator.isOpened(key).value

    SwipeActionContainer(
        isOpened = isOpened,
        swipeEnabled = swipeEnabled,
        startWidthPx = startWidthPx,
        endWidthPx = endWidthPx,
        onRequestOpen = { coordinator.open(key) },
        onRequestClose = { coordinator.close(key) },
        startActions = { onClose ->
            Row(
                modifier = Modifier.onSizeChanged {
                    startWidthPx = it.width.toFloat()
                }
            ) {
                startActions.forEach {
                    it(item, onClose)
                }
            }
        },
        endActions = { onClose ->
            Row(
                modifier = Modifier.onSizeChanged {
                    endWidthPx = it.width.toFloat()
                }
            ) {
                endActions.forEach {
                    it(item, onClose)
                }
            }
        },
        content = content
    )
}
