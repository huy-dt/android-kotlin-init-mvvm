package com.huydt.uikit.topbar.model

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.Color

@Stable
data class TopBarItem(
    val id: String,
    val icon: ImageVector,
    val label: String? = null,
    val onClick: () -> Unit,
    val enabled: Boolean = true,
    val selected: Boolean = false,
    val badgeCount: Int? = null,
    val contentColor: Color? = null    // 👈 NEW
)
