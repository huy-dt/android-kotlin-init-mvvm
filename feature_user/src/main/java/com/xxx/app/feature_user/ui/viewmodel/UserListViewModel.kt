package com.xxx.app.feature_user.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.huydt.uikit.list.config.ListConfig
import com.huydt.uikit.list.config.SelectionMode
import com.huydt.uikit.list.data.ListRepository
import com.huydt.uikit.list.ui.swipe.SwipeAction
import com.huydt.uikit.list.ui.swipe.SwipeActions
import com.huydt.uikit.list.viewmodel.ListViewModel
import com.huydt.uikit.topbar.FilterChipOption
import com.huydt.uikit.topbar.FilterGroup
import com.huydt.uikit.topbar.SortDirection
import com.huydt.uikit.topbar.SortOption as TopBarSortOption   // alias tránh conflict
import com.huydt.uikit.topbar.SortState
import com.xxx.app.feature_user.domain.model.UserDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// ── Enum định nghĩa các field filter/sort của User ──
enum class UserRole   { ADMIN, USER, GUEST }
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
        enableLoadMore = false,
        clearOnRefresh = true,
        selectionMode = SelectionMode.MULTI,
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
                initialValue = emptySet(),
            )

    /* =========================================================
     * TopBar mode
     * ========================================================= */

    private val _topBarMode = MutableStateFlow(TopBarMode.NORMAL)
    val topBarMode: StateFlow<TopBarMode> = _topBarMode

    fun openSearch() { _topBarMode.value = TopBarMode.SEARCH }
    fun openFilter() { _topBarMode.value = TopBarMode.FILTER }
    fun openSort()   { _topBarMode.value = TopBarMode.SORT   }
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

    override fun clearSearch() {
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
                FilterChipOption(
                    id = it.name,
                    label = it.name.lowercase().replaceFirstChar { c -> c.uppercase() },
                    value = it,
                )
            }
        ),
        FilterGroup(
            id = "status",
            label = "Status",
            options = UserStatus.entries.map {
                FilterChipOption(
                    id = it.name,
                    label = it.name.lowercase().replaceFirstChar { c -> c.uppercase() },
                    value = it,
                )
            }
        ),
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

    // Dùng alias TopBarSortOption để tránh conflict với data.SortOption
    val sortOptions: List<TopBarSortOption> = listOf(
        TopBarSortOption(id = UserSortField.NAME.name,       label = "Name"),
        TopBarSortOption(id = UserSortField.EMAIL.name,      label = "Email"),
        TopBarSortOption(id = UserSortField.CREATED_AT.name, label = "Created At"),
        TopBarSortOption(id = UserSortField.ROLE.name,       label = "Role"),
    )

    fun onSortSelect(optionId: String) {
        _sortState.update { current ->
            if (current.selectedId == optionId)
                current.copy(
                    direction = if (current.direction == SortDirection.ASC)
                        SortDirection.DESC else SortDirection.ASC
                )
            else
                current.copy(selectedId = optionId, direction = SortDirection.ASC)
        }
        refresh()
    }

    fun onToggleSortDirection() {
        _sortState.update {
            it.copy(
                direction = if (it.direction == SortDirection.ASC)
                    SortDirection.DESC else SortDirection.ASC
            )
        }
        refresh()
    }

    override fun clearSort() {
        _sortState.value = SortState()
        refresh()
    }

    /* =========================================================
     * Swipe Actions  →  dùng SwipeAction data class, không dùng lambda Composable
     * ========================================================= */

    override fun swipeActions(item: UserDto): SwipeActions {
        return SwipeActions(
            endActions = listOf(
                SwipeAction(
                    id = "edit",
                    label = "Edit",
                    // icon = R.drawable.ic_edit,         // thêm nếu có drawable
                    backgroundColor = 0xFF2196F3.toInt(),
                    textColor = 0xFFFFFFFF.toInt(),
                ),
                SwipeAction(
                    id = "delete",
                    label = "Delete",
                    // icon = R.drawable.ic_delete,
                    backgroundColor = 0xFFF44336.toInt(),
                    textColor = 0xFFFFFFFF.toInt(),
                ),
            )
        )
    }

    /**
     * Gọi từ UI khi swipe action được tap.
     * Ví dụ: onSwipeAction(item, "edit") / onSwipeAction(item, "delete")
     */
    fun onSwipeAction(user: UserDto, actionId: String) {
        when (actionId) {
            "edit"   -> onEditItem(user)
            "delete" -> remove(user)
        }
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
     * Selection actions
     * ========================================================= */

    override fun deleteSelected() {
        val targets = uiState.value.selectedItems.toSet()
        if (targets.isEmpty()) return
        viewModelScope.launch {
            targets.forEach { userRepository.remove(it) }
            // gọi super để xóa khỏi UI state
            super.deleteSelected()
        }
    }

    fun exportSelected() {
        val targets = uiState.value.selectedItems
        if (targets.isEmpty()) return
        // TODO: inject ExportUseCase
    }

    /* =========================================================
     * Normal actions
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