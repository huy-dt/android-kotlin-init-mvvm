package com.huydt.uikit.list.ui.swipe

import androidx.compose.runtime.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SwipeCoordinator<K : Any> {

    private val _openedKey = MutableStateFlow<K?>(null)

    val openedKey: StateFlow<K?> = _openedKey

    fun open(key: K) {
        _openedKey.value = key
    }

    fun close(key: K) {
        if (_openedKey.value == key) {
            _openedKey.value = null
        }
    }

    fun closeAll() {
        _openedKey.value = null
    }

    @Composable
    fun isOpened(key: K): State<Boolean> {
        val opened by _openedKey.collectAsState()
        return remember(opened, key) {
            mutableStateOf(opened == key)
        }
    }
}
