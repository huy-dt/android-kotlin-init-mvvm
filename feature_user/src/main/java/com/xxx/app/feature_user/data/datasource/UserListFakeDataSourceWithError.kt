package com.xxx.app.feature_user.data.datasource

import com.xxx.app.core.domain.model.Role
import com.xxx.app.feature_user.domain.model.UserDto
import com.huydt.uikit.list.data.SortOption
import com.huydt.uikit.list.data.result.PagedResult
import kotlinx.coroutines.delay
import java.util.Collections
import javax.inject.Inject

/**
 * Fake datasource dùng để test:
 * - Loading / Error / Empty / Retry / Pull to refresh
 */
class UserListFakeDataSourceWithError @Inject constructor() : UserListDataSource {

    private val users = Collections.synchronizedList(mutableListOf<UserDto>())
    private var requestCount = 0
    private val errorUntil = 1
    private val emptyUntil = 2

    init { generateMockData() }

    private fun generateMockData() {
        val roles = listOf(Role.ADMIN, Role.STAFF, Role.CUSTOMER)
        users.addAll((1..50).map { i ->
            UserDto(
                id = i.toLong(),
                username = when (i) {
                    1 -> "admin"; 2 -> "staff01"; 3 -> "customer01"
                    else -> "user_test_$i"
                },
                password = "password123",
                email = "user$i@example.com",
                role = roles[i % roles.size],
            )
        })
    }

    override suspend fun getItems(
        page: Int,
        pageSize: Int,
        query: String?,
        sortOption: SortOption?,
        filters: Map<String, Any>,
    ): List<UserDto> {
        if (page == 0) requestCount++
        if (requestCount <= errorUntil) throw FakeNetworkException()
        if (requestCount <= emptyUntil) return emptyList()

        // 1. Query
        var result = if (!query.isNullOrBlank())
            users.filter { it.username.contains(query, ignoreCase = true) }
        else users.toList()

        // 2. Filter map
        filters.forEach { (key, value) ->
            result = when (key) {
                "role" -> result.filter { it.role.name == value.toString() }
                else   -> result
            }
        }

        // 3. Sort
        result = when (sortOption?.field) {
            "username" -> if (sortOption.ascending) result.sortedBy { it.username }
                          else result.sortedByDescending { it.username }
            "email"    -> if (sortOption.ascending) result.sortedBy { it.email }
                          else result.sortedByDescending { it.email }
            "id"       -> if (sortOption.ascending) result.sortedBy { it.id }
                          else result.sortedByDescending { it.id }
            else       -> result
        }

        // 4. Phân trang
        val start = page * pageSize
        if (start >= result.size) return emptyList()
        return result.drop(start).take(pageSize)
    }

    override suspend fun getPagedItems(
        page: Int,
        pageSize: Int,
        query: String?,
        sortOption: SortOption?,
        filters: Map<String, Any>,
    ): PagedResult<UserDto> {
        delay(300)
        val items = getItems(page, pageSize, query, sortOption, filters)
        return PagedResult(items = items, totalCount = users.size)
    }

    override suspend fun add(item: UserDto) {
        delay(300)
        users.add(0, item)
    }

    override suspend fun remove(item: UserDto) {
        users.removeIf { it.id == item.id }
    }
}

class FakeNetworkException : Exception("No internet connection")