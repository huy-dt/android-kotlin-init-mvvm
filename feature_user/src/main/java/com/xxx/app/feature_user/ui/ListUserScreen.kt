package com.xxx.app.feature_user.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xxx.app.feature_user.data.datasource.UserListFakeDataSource
import com.xxx.app.feature_user.domain.model.UserDto
import com.xxx.app.feature_user.ui.viewmodel.*
import com.huydt.uikit.list.ui.UiKitListView

import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ListUserScreen(
    userViewModel: UserListViewModel = hiltViewModel()
) {
    // val repository = remember { UserListFakeDataSource() }

    // val userViewModel: UserListViewModel = viewModel(
    //     factory = UserListViewModelFactory(repository)
    // )

    val selectedItems by userViewModel.selectedItems.collectAsStateWithLifecycle()
    // Lấy status từ VM để hiển thị hiệu ứng xoay icon nếu muốn
    val uiState by userViewModel.uiState.collectAsStateWithLifecycle()
    var textSearch by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize()) {
        // --- Hàng tìm kiếm & Nút Refresh ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = textSearch,
                onValueChange = {
                    textSearch = it
                    userViewModel.search(it)
                },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Tìm kiếm User...") },
                trailingIcon = {
                    // Nút refresh nhỏ bên trong TextField (Option 1)
                    IconButton(onClick = { userViewModel.refresh() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
            
            /* // Nếu muốn nút Refresh nằm ngoài TextField (Option 2)
            IconButton(
                onClick = { userViewModel.refresh() },
                modifier = Modifier.padding(start = 4.dp)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Refresh")
            }
            */
        }

        // --- Thanh công cụ khi chọn nhiều ---
        if (selectedItems.isNotEmpty()) {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Đã chọn: ${selectedItems.size}", 
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(Modifier.weight(1f))
                    TextButton(onClick = { userViewModel.selectAll() }) {
                        Text("Tất cả")
                    }
                    TextButton(onClick = { userViewModel.clearSelection() }) {
                        Text("Xóa chọn")
                    }
                }
            }
        }

        // --- Danh sách hiển thị ---
        UiKitListView(
            vm = userViewModel,
            modifier = Modifier.weight(1f)
        ) { user, isSelected ->
            UserItem(
                user = user,
                isSelected = isSelected,
                // onRemove = { userViewModel.remove(user) }
            )
        }
    }
}