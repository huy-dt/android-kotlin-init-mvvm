package com.xxx.app.feature_user.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.xxx.app.feature_user.ui.viewmodel.UserListViewModel
import com.huydt.uikit.list.ui.UiKitListView

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp

@Composable
fun _ListUserScreen(
    vm: UserListViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    UiKitListView(
        vm = vm,
        modifier = modifier
    ) { user, isSelected ->
        UserItem(
            user = user,
            isSelected = isSelected
        )
    }
}
