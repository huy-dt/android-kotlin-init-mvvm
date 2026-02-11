package com.xxx.app.feature_user.ui.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.huydt.uikit.icon.AppIcon
import com.huydt.uikit.icon.model.*
import com.huydt.uikit.list.config.ListConfig
import com.huydt.uikit.list.config.SelectionMode
import com.huydt.uikit.list.data.ListRepository
import com.huydt.uikit.list.ui.swipe.SwipeActions
import com.huydt.uikit.list.viewmodel.ListViewModel
import com.xxx.app.feature_user.domain.model.UserDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val userRepository: ListRepository<UserDto>
) : ListViewModel<UserDto>(
    repository = userRepository,
    config = ListConfig(
        pageSize = 20,
        enableRefresh = true,
        enableLoadMore = true,
        clearOnRefresh = false,
        selectionMode = SelectionMode.MULTI
    )
) {
    
    /* ---- Expose selectedItems ---- */
    val selectedItems: StateFlow<Set<UserDto>> =
        uiState
            .map { it.selectedItems }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptySet()
            )

    /* ---- Swipe Actions Configuration ---- */
    override fun swipeActions(user: UserDto): SwipeActions<UserDto> {
        return SwipeActions(
            end = listOf(
                // 📝 EDIT ACTION
                { item, onClose ->
                    AppIcon(
                        icon = Icons.Default.Edit,
                        label = "Edit",
                        size = IconSize.Small,
                        colors = IconColors(
                            color = Color.White,
                            background = Color(0xFF2196F3) // Blue
                        ),
                        onClick = {
                            println("Edit user: ${item.id}")
                            onClose()
                        }
                    )
                },
                
                // 🗑 DELETE ACTION
                { item, onClose ->
                    AppIcon(
                        icon = Icons.Default.Delete,
                        label = "Delete",
                        size = IconSize.Small,
                        colors = IconColors(
                            color = Color.White,
                            background = Color.Red
                        ),
                        onClick = {
                            remove(item)
                            onClose()
                        }
                    )
                }
            )
        )
    }

    /* ---- Custom Behaviors ---- */
    fun remove(user: UserDto) {
        viewModelScope.launch {
            userRepository.remove(user)
            removeItem(user) // API base
        }
    }
}