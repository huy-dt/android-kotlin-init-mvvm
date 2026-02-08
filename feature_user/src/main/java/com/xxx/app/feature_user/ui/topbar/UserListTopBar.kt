package com.xxx.app.feature_user.ui.topbar

import androidx.compose.runtime.Composable
import com.huydt.uikit.topbar.ui.TopBar
import com.huydt.uikit.topbar.model.TopBarItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

@Composable
fun UserListTopBar(
    onBack: (() -> Unit)?,
    onRefresh: () -> Unit,
    onSearchClick: (() -> Unit)? = null
) {
    TopBar(
        showLabel = false,
        leftActions = listOf(
            TopBarItem(
                id = "back",
                icon = Icons.Default.ArrowBack,
                label = "Back",
                onClick = onBack
            )
        ),

        // 🔥 MID – nhiều item để vuốt
        midActions = listOf(
            TopBarItem("user1", Icons.Default.Person, "User 1") {
                println("User 1")
            },
            TopBarItem("user2", Icons.Default.Person, "User 2") {
                println("User 2")
            },
            TopBarItem("star", Icons.Default.Star, "Star") {
                println("Star")
            },
            TopBarItem("fav", Icons.Default.Favorite, "Fav") {
                println("Favorite")
            },
            TopBarItem("info", Icons.Default.Info, "Info") {
                println("Info")
            },
            TopBarItem("settings", Icons.Default.Settings, "Settings") {
                println("Settings")
            },
            TopBarItem("more1", Icons.Default.MoreVert, "More 1") {
                println("More 1")
            },
            TopBarItem("more2", Icons.Default.MoreVert, "More 2") {
                println("More 2")
            }
        ),

        rightActions = listOf(
            TopBarItem(
                id = "search",
                icon = Icons.Default.Search,
                label = "Search",
                onClick = onSearchClick
            ),
            TopBarItem(
                id = "refresh",
                icon = Icons.Default.Refresh,
                label = "Refresh",
                onClick = onRefresh
            )
        ),

        // 🔥 OVERFLOW MENU
        moreActions = listOf(
            TopBarItem("more1", Icons.Default.MoreVert, "More 1") {
                println("More 1")
            },
            TopBarItem("more1", Icons.Default.MoreVert, "More 1") {
                println("More 1")
            },
            TopBarItem("more1", Icons.Default.MoreVert, "More 1") {
                println("More 1")
            },TopBarItem("more1", Icons.Default.MoreVert, "More 1") {
                println("More 1")
            }    
        )
    )
}
