package com.huydt.uikit.topbar.model

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector

@Stable
data class TopBarItem(
    val id: String,
    val icon: ImageVector? = null,
    val label: String? = null,
    val onClick: () -> Unit,
    val enabled: Boolean = true,
    val selected: Boolean = false,
    val badgeCount: Int? = null,
    val colors: ItemColors = ItemColorDefaults.colors()
) {
    init {
        require(icon != null || label != null) {
            "TopBarItem must have at least icon or label"
        }
    }
}
