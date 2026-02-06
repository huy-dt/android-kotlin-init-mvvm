package com.xxx.app.feature_user.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xxx.app.feature_user.data.UserListRepository
import com.xxx.app.feature_user.domain.model.UserDto
import com.huydt.uikit.list.ui.UiKitListView
import com.huydt.uikit.list.viewmodel.ListViewModel
import com.huydt.uikit.list.viewmodel.ListViewModelFactory

@Composable
fun ListUserScreen() {
    val repository = remember { UserListRepository() }

    val userViewModel: UserListViewModel = viewModel(
        factory = UserListViewModelFactory(repository)
    )

    val selectedItems by userViewModel.selectedItems.collectAsStateWithLifecycle()
    var textSearch by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize()) {

        TextField(
            value = textSearch,
            onValueChange = {
                textSearch = it
                userViewModel.search(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            placeholder = { Text("Tìm kiếm User...") }
        )

        if (selectedItems.isNotEmpty()) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Đã chọn: ${selectedItems.size}")
                Spacer(Modifier.weight(1f))
                TextButton(onClick = { userViewModel.selectAll() }) {
                    Text("Tất cả")
                }
                TextButton(onClick = { userViewModel.clearSelection() }) {
                    Text("Xóa chọn")
                }
            }
        }

        UiKitListView(
            repository = repository,
            viewModel = userViewModel,
            itemKey = { it.id }
        ) { user, isSelected, vm ->
            UserItem(
                user = user,
                isSelected = isSelected,
                onRemove = { userViewModel.remove(user) }
            )
        }
    }
}
