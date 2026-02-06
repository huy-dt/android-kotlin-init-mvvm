package com.xxx.app.feature_user.data

import com.xxx.app.core.domain.model.Role
import com.xxx.app.feature_user.domain.model.UserDto
import com.huydt.uikit.list.data.ListRepository
import kotlinx.coroutines.delay
import java.util.Collections

class UserListRepository : ListRepository<UserDto> {

    // Sử dụng Thread-safe list để tránh crash khi thao tác nhanh
    private val users = Collections.synchronizedList(mutableListOf<UserDto>())

    init {
        // Nạp dữ liệu giả lập ngay khi khởi tạo Repository
        generateMockData()
    }

    private fun generateMockData() {
        val roles = listOf(Role.ADMIN, Role.STAFF, Role.CUSTOMER)
        val mockUsers = (1..50).map { i ->
            UserDto(
                id = i.toLong(),
                username = when {
                    i == 1 -> "admin"
                    i == 2 -> "staff01"
                    i == 3 -> "customer01"
                    else -> "user_test_$i"
                },
                password = "password123",
                email = "user$i@example.com",
                role = roles[i % roles.size] // Phân bổ role xoay vòng
            )
        }
        users.addAll(mockUsers)
    }

    override suspend fun getItems(
        page: Int,
        pageSize: Int,
        query: String?
    ): List<UserDto> {
        delay(800) // Giả lập lag mạng để test Loading Shimmer/Indicator

        val filtered = if (!query.isNullOrBlank()) {
            users.filter {
                it.username.contains(query, true)
                // it.email.contains(query, true) ||
                // it.role.name.contains(query, true)
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
        // Thêm vào đầu list để user thấy ngay kết quả
        users.add(0, item)
    }

    override suspend fun remove(item: UserDto) {
        delay(300)
        users.removeIf { it.id == item.id }
    }
}