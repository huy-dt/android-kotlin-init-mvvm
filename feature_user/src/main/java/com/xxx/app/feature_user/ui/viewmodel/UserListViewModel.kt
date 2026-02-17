package com.xxx.app.feature_user.ui.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.huydt.uikit.icon.AppIcon
import com.huydt.uikit.icon.model.*
import com.huydt.uikit.list.config.ListConfig
import com.huydt.uikit.list.config.SelectionMode
import com.huydt.uikit.list.data.ListRepository
import com.huydt.uikit.list.ui.swipe.SwipeActions
import com.huydt.uikit.topbar.FilterChipOption
import com.huydt.uikit.topbar.FilterGroup
import com.huydt.uikit.topbar.SortDirection
import com.huydt.uikit.topbar.SortOption
import com.huydt.uikit.topbar.SortState
import com.huydt.uikit.list.viewmodel.ListViewModel
import com.xxx.app.feature_user.domain.model.UserDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// ── Enum định nghĩa các field filter/sort của User ──

enum class UserRole { ADMIN, USER, GUEST }
enum class UserStatus { ACTIVE, INACTIVE, BANNED }
enum class UserSortField { NAME, EMAIL, CREATED_AT, ROLE }

// ── State cho inline bar đang mở ──

enum class TopBarMode { NORMAL, SEARCH, FILTER, SORT }

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

    /* =========================================================
     * Expose cho UI
     * ========================================================= */

    val selectionMode: SelectionMode get() = config.selectionMode

    val selectedItems: StateFlow<Set<UserDto>> =
        uiState
            .map { it.selectedItems }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptySet()
            )

    /* =========================================================
     * TopBar mode
     * ========================================================= */

    private val _topBarMode = MutableStateFlow(TopBarMode.NORMAL)
    val topBarMode: StateFlow<TopBarMode> = _topBarMode

    fun openSearch() { _topBarMode.value = TopBarMode.SEARCH }
    fun openFilter() { _topBarMode.value = TopBarMode.FILTER }
    fun openSort()   { _topBarMode.value = TopBarMode.SORT }
    fun closeBar()   { _topBarMode.value = TopBarMode.NORMAL }

    /* =========================================================
     * Search
     * ========================================================= */

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        search(query) // debounce nằm trong ListViewModel.search()
    }

    fun clearSearch() {
        _searchQuery.value = ""
        search("")
    }

    /* =========================================================
     * Filter
     * ========================================================= */

    private val _filterSelections = MutableStateFlow<Map<String, String?>>(emptyMap())
    val filterSelections: StateFlow<Map<String, String?>> = _filterSelections

    val activeFilterCount: StateFlow<Int> =
        _filterSelections
            .map { it.values.count { v -> v != null } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    val filterGroups: List<FilterGroup> = listOf(
        FilterGroup(
            id = "role",
            label = "Role",
            options = UserRole.entries.map {
                FilterChipOption(id = it.name, label = it.name.lowercase().replaceFirstChar { c -> c.uppercase() }, value = it)
            }
        ),
        FilterGroup(
            id = "status",
            label = "Status",
            options = UserStatus.entries.map {
                FilterChipOption(id = it.name, label = it.name.lowercase().replaceFirstChar { c -> c.uppercase() }, value = it)
            }
        )
    )

    val activeFilterGroups: StateFlow<List<FilterGroup>> =
        _filterSelections
            .map { selections ->
                filterGroups.map { group -> group.copy(selectedId = selections[group.id]) }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), filterGroups)

    fun onFilterSelect(groupId: String, optionId: String?) {
        _filterSelections.update { it + (groupId to optionId) }
        refresh()
    }

    fun clearAllFilters() {
        _filterSelections.value = emptyMap()
        refresh()
    }

    /* =========================================================
     * Sort
     * ========================================================= */

    private val _sortState = MutableStateFlow(SortState())
    val sortState: StateFlow<SortState> = _sortState

    val sortOptions: List<SortOption> = listOf(
        SortOption(id = UserSortField.NAME.name,       label = "Name"),
        SortOption(id = UserSortField.EMAIL.name,      label = "Email"),
        SortOption(id = UserSortField.CREATED_AT.name, label = "Created At"),
        SortOption(id = UserSortField.ROLE.name,       label = "Role"),
    )

    fun onSortSelect(optionId: String) {
        _sortState.update { current ->
            if (current.selectedId == optionId)
                current.copy(direction = if (current.direction == SortDirection.ASC) SortDirection.DESC else SortDirection.ASC)
            else
                current.copy(selectedId = optionId, direction = SortDirection.ASC)
        }
        refresh()
    }

    fun onToggleSortDirection() {
        _sortState.update {
            it.copy(direction = if (it.direction == SortDirection.ASC) SortDirection.DESC else SortDirection.ASC)
        }
        refresh()
    }

    fun clearSort() {
        _sortState.value = SortState()
        refresh()
    }

    /* =========================================================
     * Swipe Actions
     * ========================================================= */

    override fun swipeActions(user: UserDto): SwipeActions<UserDto> {
        return SwipeActions(
            end = listOf(
                { item, onClose ->
                    AppIcon(
                        icon = Icons.Default.Edit,
                        label = "Edit",
                        size = IconSize.SMALL,
                        colors = IconColors(color = Color.White, background = Color(0xFF2196F3)),
                        onClick = { onEditItem(item); onClose() }
                    )
                },
                { item, onClose ->
                    AppIcon(
                        icon = Icons.Default.Delete,
                        label = "Delete",
                        size = IconSize.SMALL,
                        colors = IconColors(color = Color.White, background = Color.Red),
                        onClick = { remove(item); onClose() }
                    )
                }
            )
        )
    }

    /* =========================================================
     * Single item actions
     * ========================================================= */

    fun remove(user: UserDto) {
        viewModelScope.launch {
            userRepository.remove(user)
            removeItem(user)
        }
    }

    open fun onEditItem(user: UserDto) = Unit

    /* =========================================================
     * TopBar — Selection actions
     * ========================================================= */

    fun deleteSelected() {
        val targets = uiState.value.selectedItems
        if (targets.isEmpty()) return
        viewModelScope.launch {
            targets.forEach { userRepository.remove(it) }
            removeItems { it in targets }
        }
    }

    fun exportSelected() {
        val targets = uiState.value.selectedItems
        if (targets.isEmpty()) return
        // TODO: inject ExportUseCase
    }

    /* =========================================================
     * TopBar — Normal actions
     * ========================================================= */

    fun deleteAll() {
        viewModelScope.launch {
            uiState.value.items.forEach { userRepository.remove(it) }
            clearItems()
        }
    }

    fun importData() { /* TODO */ }
    fun exportAll()  { /* TODO */ }
}