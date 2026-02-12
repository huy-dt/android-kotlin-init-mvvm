package com.huydt.uikit.topbar.sample

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.huydt.uikit.topbar.TopBar
import com.huydt.uikit.topbar.model.TopBarActionGroup
import com.huydt.uikit.topbar.model.TopBarItem
import com.huydt.uikit.icon.model.IconSize

/**
 * Sample: Simple TopBar với Back và Search
 */
@Composable
fun SimpleTopBarSample() {
    Scaffold(
        topBar = {
            TopBar(
                leftActions = listOf(
                    TopBarItem(
                        id = "back",
                        icon = Icons.Default.ArrowBack,
                        label = "Back",
                        onClick = { /* Navigate back */ }
                    )
                ),
                rightActions = listOf(
                    TopBarItem(
                        id = "search",
                        icon = Icons.Default.Search,
                        label = "Search",
                        onClick = { /* Open search */ }
                    )
                )
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Text("Content here")
        }
    }
}

/**
 * Sample: TopBar với tabs ở giữa
 */
@Composable
fun TabsTopBarSample() {
    var selectedTab by remember { mutableStateOf("all") }

    Scaffold(
        topBar = {
            TopBar(
                leftActions = listOf(
                    TopBarItem(
                        id = "menu",
                        icon = Icons.Default.Menu,
                        label = "Menu",
                        onClick = { /* Open drawer */ }
                    )
                ),
                midActions = listOf(
                    TopBarItem(
                        id = "all",
                        label = "All",
                        selected = selectedTab == "all",
                        onClick = { selectedTab = "all" }
                    ),
                    TopBarItem(
                        id = "active",
                        label = "Active",
                        selected = selectedTab == "active",
                        onClick = { selectedTab = "active" }
                    ),
                    TopBarItem(
                        id = "completed",
                        label = "Completed",
                        selected = selectedTab == "completed",
                        onClick = { selectedTab = "completed" }
                    ),
                    TopBarItem(
                        id = "archived",
                        label = "Archived",
                        selected = selectedTab == "archived",
                        onClick = { selectedTab = "archived" }
                    )
                ),
                rightActions = listOf(
                    TopBarItem(
                        id = "filter",
                        icon = Icons.Default.FilterList,
                        label = "Filter",
                        onClick = { /* Open filter */ }
                    )
                )
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Text("Tab content: $selectedTab")
        }
    }
}

/**
 * Sample: Selection mode TopBar
 */
@Composable
fun SelectionModeTopBarSample() {
    var selectedCount by remember { mutableStateOf(3) }

    Scaffold(
        topBar = {
            TopBar(
                leftActions = listOf(
                    TopBarItem(
                        id = "close",
                        icon = Icons.Default.Close,
                        label = "Close",
                        onClick = { /* Exit selection mode */ }
                    ),
                    TopBarItem(
                        id = "count",
                        label = "$selectedCount selected"
                    )
                ),
                rightActions = listOf(
                    TopBarItem(
                        id = "delete",
                        icon = Icons.Default.Delete,
                        label = "Delete",
                        onClick = { /* Delete selected */ }
                    ),
                    TopBarItem(
                        id = "share",
                        icon = Icons.Default.Share,
                        label = "Share",
                        onClick = { /* Share selected */ }
                    )
                ),
                moreActions = listOf(
                    TopBarActionGroup(
                        id = "actions",
                        items = listOf(
                            TopBarItem(
                                id = "edit",
                                icon = Icons.Default.Edit,
                                label = "Edit",
                                onClick = { /* Edit selected */ }
                            ),
                            TopBarItem(
                                id = "favorite",
                                icon = Icons.Default.Favorite,
                                label = "Add to favorites",
                                onClick = { /* Add to favorites */ }
                            )
                        )
                    )
                )
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Text("$selectedCount items selected")
        }
    }
}

/**
 * Sample: TopBar với badge counts
 */
@Composable
fun BadgeTopBarSample() {
    Scaffold(
        topBar = {
            TopBar(
                leftActions = listOf(
                    TopBarItem(
                        id = "back",
                        icon = Icons.Default.ArrowBack,
                        onClick = { /* Back */ }
                    )
                ),
                midActions = listOf(
                    TopBarItem(
                        id = "inbox",
                        label = "Inbox",
                        badgeCount = 12,
                        selected = true,
                        onClick = { /* Show inbox */ }
                    ),
                    TopBarItem(
                        id = "sent",
                        label = "Sent",
                        onClick = { /* Show sent */ }
                    ),
                    TopBarItem(
                        id = "draft",
                        label = "Draft",
                        badgeCount = 3,
                        onClick = { /* Show draft */ }
                    )
                ),
                rightActions = listOf(
                    TopBarItem(
                        id = "add",
                        icon = Icons.Default.Add,
                        label = "Compose",
                        onClick = { /* Compose new */ }
                    )
                )
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Text("Mail content")
        }
    }
}

/**
 * Sample: TopBar với overflow menu groups
 */
@Composable
fun OverflowMenuTopBarSample() {
    Scaffold(
        topBar = {
            TopBar(
                leftActions = listOf(
                    TopBarItem(
                        id = "menu",
                        icon = Icons.Default.Menu,
                        onClick = { /* Open drawer */ }
                    )
                ),
                rightActions = listOf(
                    TopBarItem(
                        id = "search",
                        icon = Icons.Default.Search,
                        onClick = { /* Search */ }
                    ),
                    TopBarItem(
                        id = "filter",
                        icon = Icons.Default.FilterList,
                        onClick = { /* Filter */ }
                    )
                ),
                moreActions = listOf(
                    TopBarActionGroup(
                        id = "view",
                        title = "View Options",
                        items = listOf(
                            TopBarItem(
                                id = "sort",
                                icon = Icons.Default.Sort,
                                label = "Sort",
                                onClick = { /* Sort */ }
                            )
                        )
                    ),
                    TopBarActionGroup(
                        id = "actions",
                        title = "Actions",
                        items = listOf(
                            TopBarItem(
                                id = "edit",
                                icon = Icons.Default.Edit,
                                label = "Edit",
                                onClick = { /* Edit */ }
                            ),
                            TopBarItem(
                                id = "delete",
                                icon = Icons.Default.Delete,
                                label = "Delete",
                                onClick = { /* Delete */ }
                            ),
                            TopBarItem(
                                id = "share",
                                icon = Icons.Default.Share,
                                label = "Share",
                                onClick = { /* Share */ }
                            )
                        )
                    ),
                    TopBarActionGroup(
                        id = "settings",
                        items = listOf(
                            TopBarItem(
                                id = "settings",
                                icon = Icons.Default.Settings,
                                label = "Settings",
                                onClick = { /* Settings */ }
                            )
                        )
                    )
                )
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Text("Content with overflow menu")
        }
    }
}

/**
 * Sample: TopBar với different sizes
 */
@Composable
fun DifferentSizesTopBarSample() {
    Column(modifier = Modifier.fillMaxSize()) {
        // Small
        TopBar(
            leftActions = listOf(
                TopBarItem(
                    id = "back",
                    icon = Icons.Default.ArrowBack,
                    label = "Back",
                    onClick = { }
                )
            ),
            itemSize = IconSize.SMALL,
            showLabel = true
        )

        // Medium (default)
        TopBar(
            leftActions = listOf(
                TopBarItem(
                    id = "back",
                    icon = Icons.Default.ArrowBack,
                    label = "Back",
                    onClick = { }
                )
            ),
            itemSize = IconSize.MEDIUM,
            showLabel = true
        )

        // Large
        TopBar(
            leftActions = listOf(
                TopBarItem(
                    id = "back",
                    icon = Icons.Default.ArrowBack,
                    label = "Back",
                    onClick = { }
                )
            ),
            itemSize = IconSize.LARGE,
            showLabel = true
        )
    }
}

/**
 * Sample: TopBar without labels
 */
@Composable
fun NoLabelsTopBarSample() {
    Scaffold(
        topBar = {
            TopBar(
                leftActions = listOf(
                    TopBarItem(
                        id = "menu",
                        icon = Icons.Default.Menu,
                        onClick = { }
                    )
                ),
                rightActions = listOf(
                    TopBarItem(
                        id = "search",
                        icon = Icons.Default.Search,
                        onClick = { }
                    ),
                    TopBarItem(
                        id = "filter",
                        icon = Icons.Default.FilterList,
                        onClick = { }
                    )
                ),
                showLabel = false
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Text("Compact TopBar without labels")
        }
    }
}