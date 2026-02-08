package com.xxx.app.feature_user.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.xxx.app.feature_user.ui.viewmodel.UserListViewModel

@Composable
fun ListUserScreen(
    onBack: (() -> Unit)? = null,
    vm: UserListViewModel = hiltViewModel()
) {
    ListUserScreenWithTopBar(
        onBack = onBack,
        vm = vm
    )
}
