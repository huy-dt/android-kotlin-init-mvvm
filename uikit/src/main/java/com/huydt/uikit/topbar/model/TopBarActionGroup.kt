package com.huydt.uikit.topbar.model

import androidx.compose.runtime.Immutable

/**
 * Group các actions trong overflow menu
 *
 * @param id Unique identifier cho group
 * @param title Tiêu đề của group (optional)
 * @param items Danh sách TopBarItem trong group
 */
@Immutable
data class TopBarActionGroup(
    val id: String,
    val title: String? = null,
    val items: List<TopBarItem>
) {
    init {
        // require(items.isNotEmpty()) {
        //     "TopBarActionGroup phải có ít nhất 1 item"
        // }
    }
}