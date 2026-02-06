package com.xxx.app.feature_user.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewModelScope
import com.huydt.uikit.list.viewmodel.*
import com.huydt.uikit.list.config.*
import com.huydt.uikit.list.ui.swipe.SwipeActions
import com.xxx.app.feature_user.data.UserListRepository
import com.xxx.app.feature_user.domain.model.UserDto
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserListViewModel(
    private val userRepository: UserListRepository
) : ListViewModel<UserDto>(
    repository = userRepository,
    config = ListConfig(
        pageSize = 20,
        enableRefresh = true,
        enableLoadMore = true,
        selectionMode = SelectionMode.MULTI
    )
) {

    /* ---- expose selectedItems ---- */

    val selectedItems: StateFlow<Set<UserDto>> =
        uiState
            .map { it.selectedItems }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptySet()
            )

    override fun swipeActions(user: UserDto): SwipeActions<UserDto> {
    return SwipeActions(
        end = listOf(

            // 📝 EDIT
            { item, onClose ->
                TextButton(
                    onClick = {
                        // TODO: navigate edit
                        println("Edit user: ${item.id}")
                        onClose()
                    }
                ) {
                    Text(
                        text = "Edit",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },

            // 🗑 DELETE
            { item, onClose ->
                TextButton(
                    onClick = {
                        remove(item)
                        onClose()
                    }
                ) {
                    Text(
                        text = "Delete",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )
    )
}


    /* ---- behavior riêng ---- */

    fun remove(user: UserDto) {
        viewModelScope.launch {
            userRepository.remove(user)
            removeItem(user) // API base
        }
    }
}
