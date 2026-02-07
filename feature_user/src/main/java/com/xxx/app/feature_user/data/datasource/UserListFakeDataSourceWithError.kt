package com.xxx.app.feature_user.data.datasource

import com.xxx.app.core.domain.model.Role
import com.xxx.app.feature_user.domain.model.UserDto
import kotlinx.coroutines.delay
import java.util.Collections
import javax.inject.Inject

/**
 * Fake datasource dùng để test:
 * - Loading
 * - Error (internet)
 * - Empty state
 * - Retry / Pull to refresh
 */
class UserListFakeDataSourceWithError @Inject constructor() : UserListDataSource {

    // Thread-safe list
    private val users = Collections.synchronizedList(mutableListOf<UserDto>())

    // Đếm số lần gọi API (giả lập refresh)
    private var requestCount = 0

    // Cấu hình số lần lỗi / empty
    private val errorUntil = 2   // 2 lần đầu lỗi mạng
    private val emptyUntil = 4   // 2 lần tiếp theo empty

    init {
        generateMockData()
    }

    private fun generateMockData() {
        val roles = listOf(Role.ADMIN, Role.STAFF, Role.CUSTOMER)
        val mockUsers = (1..50).map { i ->
            UserDto(
                id = i.toLong(),
                username = when (i) {
                    1 -> "admin"
                    2 -> "staff01"
                    3 -> "customer01"
                    else -> "user_test_$i"
                },
                password = "password123",
                email = "user$i@example.com",
                role = roles[i % roles.size]
            )
        }
        users.addAll(mockUsers)
    }

    override suspend fun getItems(
        page: Int,
        pageSize: Int,
        query: String?
    ): List<UserDto> {

        // Reset state khi load lại từ đầu (pull refresh)
        if (page == 0) {
            requestCount++
        }

        delay(800) // giả lập lag mạng

        // 1️⃣ Giả lập lỗi mạng
        if (requestCount <= errorUntil) {
            throw FakeNetworkException()
        }

        // 2️⃣ Giả lập empty
        if (requestCount <= emptyUntil) {
            return emptyList()
        }

        // 3️⃣ Trả data bình thường
        val filtered = if (!query.isNullOrBlank()) {
            users.filter {
                it.username.contains(query, ignoreCase = true)
            }
        } else {
            users.toList()
        }

        val start = page * pageSize
        if (start >= filtered.size) return emptyList()

        return filtered.drop(start).take(pageSize)
    }

    override suspend fun add(item: UserDto) {
        delay(300)
        users.add(0, item)
    }

    override suspend fun remove(item: UserDto) {
        delay(300)
        users.removeIf { it.id == item.id }
    }
}

/**
 * Fake exception để test error UI
 */
class FakeNetworkException : Exception("No internet connection")
