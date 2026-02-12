package com.huydt.uikit.topbar.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

import com.huydt.uikit.icon.model.IconColors

@Immutable
data class TopBarItem(
    val id: String,
    val icon: ImageVector? = null,
    val label: String? = null,
    val enabled: Boolean = true,
    val selected: Boolean = false,
    val badgeCount: Int? = null,
    val colors: IconColors? = null,
    val onClick: (() -> Unit)? = null
) {
    init {
        require(icon != null || label != null) {
            "TopBarItem phải có ít nhất icon hoặc label"
        }
    }
}

@Immutable
data class IconColors(
    val content: Color = Color.Unspecified,
    val background: Color = Color.Transparent
)
