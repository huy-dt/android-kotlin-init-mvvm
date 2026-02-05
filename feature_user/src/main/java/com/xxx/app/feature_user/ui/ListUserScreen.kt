package com.xxx.app.feature_user.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xxx.app.feature_user.data.UserListRepository
import com.xxx.app.feature_user.domain.model.UserDto
import com.huydt.uikit.list.UiKitListView

@Composable
fun ListUserScreen() {

    val repository = remember { UserListRepository() }
    var search by remember { mutableStateOf("") }

    Column {

        TextField(
            value = search,
            onValueChange = {
                search = it
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search user") }
        )

        UiKitListView(
            repository = repository,
            itemKey = { it.id }
        ) { user, viewModel ->

            UserItem(
                user = user,
                onRemove = { viewModel.remove(user) }
            )
        }
    }
}

@Composable
fun UserItem(
    user: UserDto,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(user.username, style = MaterialTheme.typography.titleMedium)
                Text(user.email, style = MaterialTheme.typography.bodySmall)
                Text(user.role.name, style = MaterialTheme.typography.labelSmall)
            }

            TextButton(onClick = onRemove) {
                Text("Remove")
            }
        }
    }
}
