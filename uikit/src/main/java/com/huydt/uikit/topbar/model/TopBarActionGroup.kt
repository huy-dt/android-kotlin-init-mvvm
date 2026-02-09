package com.huydt.uikit.topbar.model

data class TopBarActionGroup(
    val id: String,
    val title: String? = null,
    val actions: List<TopBarItem>
)
