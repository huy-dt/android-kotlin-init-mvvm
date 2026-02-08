package com.huydt.uikit.topbar.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class TopBarItem(
    val id: String,
    val icon: ImageVector? = null,
    val label: String? = null,
    val tint: Color = Color.Unspecified,
    val enabled: Boolean = true,
    val onClick: (() -> Unit)? = null
)
