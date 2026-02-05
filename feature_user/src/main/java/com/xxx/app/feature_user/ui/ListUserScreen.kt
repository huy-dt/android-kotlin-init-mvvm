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
import com.huydt.uikit.list.UiKitListView
import com.huydt.uikit.list.domain.ListViewModel
import com.huydt.uikit.list.domain.ListViewModelFactory

@Composable
fun ListUserScreen() {
    val repository = remember { UserListRepository() }
    val userViewModel: ListViewModel<UserDto> = viewModel(factory = ListViewModelFactory(repository))
    
    var textSearch by remember { mutableStateOf("") }
    val selectedItems by userViewModel.selectedItems.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize()) {
        TextField(
            value = textSearch,
            onValueChange = { textSearch = it; userViewModel.search(it) },
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            placeholder = { Text("Tìm kiếm User...") }
        )

        if (selectedItems.isNotEmpty()) {
            Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("Đã chọn: ${selectedItems.size}", style = MaterialTheme.typography.titleSmall)
                Spacer(Modifier.weight(1f))
                TextButton(onClick = { userViewModel.selectAll() }) { Text("Tất cả") }
                TextButton(onClick = { userViewModel.clearSelection() }) { Text("Xóa chọn") }
            }
        }

        UiKitListView(
            repository = repository,
            viewModel = userViewModel, // Dùng chung VM
            itemKey = { it.id }
        ) { user, isSelected, vm ->
            UserItem(user = user, isSelected = isSelected, onRemove = { vm.remove(user) })
        }
    }
}

@Composable
fun UserItem(user: UserDto, isSelected: Boolean, onRemove: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer 
                            else MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(user.username, style = MaterialTheme.typography.titleMedium)
                Text(user.email, style = MaterialTheme.typography.bodySmall)
            }
            TextButton(onClick = onRemove) {
                Text("Remove", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}