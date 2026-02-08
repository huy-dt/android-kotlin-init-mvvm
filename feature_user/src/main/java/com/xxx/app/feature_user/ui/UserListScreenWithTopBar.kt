package com.xxx.app.feature_user.ui

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.xxx.app.feature_user.ui.topbar.UserListTopBar
import com.xxx.app.feature_user.ui.viewmodel.UserListViewModel

@Composable
fun ListUserScreenWithTopBar(
    onBack: (() -> Unit)?,
    vm: UserListViewModel
) {
    Scaffold(
        topBar = {
            UserListTopBar(
                onBack = onBack,
                onRefresh = vm::refresh
            )
        }
    ) { padding ->
        _ListUserScreen(
            vm = vm,
            contentPadding = padding
        )
    }
}
