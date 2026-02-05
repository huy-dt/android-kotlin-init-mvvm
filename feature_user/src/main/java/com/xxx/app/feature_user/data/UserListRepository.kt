package com.xxx.app.feature_user.data

import com.xxx.app.core.domain.model.Role
import com.xxx.app.feature_user.domain.model.UserDto
import com.huydt.uikit.list.UiKitListRepository
import kotlinx.coroutines.delay

class UserListRepository : UiKitListRepository<UserDto> {
    
private val users = mutableListOf(
    UserDto(1, "admin", "12345678", "admin@email.com", Role.ADMIN),

    UserDto(2, "staff01", "12345678", "staff01@email.com", Role.STAFF),
    UserDto(3, "staff02", "12345678", "staff02@email.com", Role.STAFF),
    UserDto(4, "staff03", "12345678", "staff03@email.com", Role.STAFF),
    UserDto(5, "staff04", "12345678", "staff04@email.com", Role.STAFF),
    UserDto(6, "staff05", "12345678", "staff05@email.com", Role.STAFF),

    UserDto(7, "customer01", "12345678", "customer01@email.com", Role.CUSTOMER),
    UserDto(8, "customer02", "12345678", "customer02@email.com", Role.CUSTOMER),
    UserDto(9, "customer03", "12345678", "customer03@email.com", Role.CUSTOMER),
    UserDto(10, "customer04", "12345678", "customer04@email.com", Role.CUSTOMER),
    UserDto(11, "customer05", "12345678", "customer05@email.com", Role.CUSTOMER),
    UserDto(12, "customer06", "12345678", "customer06@email.com", Role.CUSTOMER),
    UserDto(13, "customer07", "12345678", "customer07@email.com", Role.CUSTOMER),
    UserDto(14, "customer08", "12345678", "customer08@email.com", Role.CUSTOMER),
    UserDto(15, "customer09", "12345678", "customer09@email.com", Role.CUSTOMER),
    UserDto(16, "customer10", "12345678", "customer10@email.com", Role.CUSTOMER),
    UserDto(17, "customer11", "12345678", "customer11@email.com", Role.CUSTOMER),
    UserDto(18, "customer12", "12345678", "customer12@email.com", Role.CUSTOMER),
    UserDto(19, "customer13", "12345678", "customer13@email.com", Role.CUSTOMER),
    UserDto(20, "customer14", "12345678", "customer14@email.com", Role.CUSTOMER),
    UserDto(21, "customer15", "12345678", "customer15@email.com", Role.CUSTOMER),
    UserDto(22, "customer16", "12345678", "customer16@email.com", Role.CUSTOMER),
    UserDto(23, "customer17", "12345678", "customer17@email.com", Role.CUSTOMER),
    UserDto(24, "customer18", "12345678", "customer18@email.com", Role.CUSTOMER),
    UserDto(25, "customer19", "12345678", "customer19@email.com", Role.CUSTOMER),
    UserDto(26, "customer20", "12345678", "customer20@email.com", Role.CUSTOMER),
    UserDto(27, "customer21", "12345678", "customer21@email.com", Role.CUSTOMER),
    UserDto(28, "customer22", "12345678", "customer22@email.com", Role.CUSTOMER),
    UserDto(29, "customer23", "12345678", "customer23@email.com", Role.CUSTOMER),
    UserDto(30, "customer24", "12345678", "customer24@email.com", Role.CUSTOMER),
    UserDto(31, "customer25", "12345678", "customer25@email.com", Role.CUSTOMER),
    UserDto(32, "customer26", "12345678", "customer26@email.com", Role.CUSTOMER),
    UserDto(33, "customer27", "12345678", "customer27@email.com", Role.CUSTOMER),
    UserDto(34, "customer28", "12345678", "customer28@email.com", Role.CUSTOMER),
    UserDto(35, "customer29", "12345678", "customer29@email.com", Role.CUSTOMER),
    UserDto(36, "customer30", "12345678", "customer30@email.com", Role.CUSTOMER),
    UserDto(37, "customer31", "12345678", "customer31@email.com", Role.CUSTOMER),
    UserDto(38, "customer32", "12345678", "customer32@email.com", Role.CUSTOMER),
    UserDto(39, "customer33", "12345678", "customer33@email.com", Role.CUSTOMER),
    UserDto(40, "customer34", "12345678", "customer34@email.com", Role.CUSTOMER),
    UserDto(41, "customer35", "12345678", "customer35@email.com", Role.CUSTOMER),
    UserDto(42, "customer36", "12345678", "customer36@email.com", Role.CUSTOMER),
    UserDto(43, "customer37", "12345678", "customer37@email.com", Role.CUSTOMER),
    UserDto(44, "customer38", "12345678", "customer38@email.com", Role.CUSTOMER),
    UserDto(45, "customer39", "12345678", "customer39@email.com", Role.CUSTOMER),
    UserDto(46, "customer40", "12345678", "customer40@email.com", Role.CUSTOMER),
    UserDto(47, "customer41", "12345678", "customer41@email.com", Role.CUSTOMER),
    UserDto(48, "customer42", "12345678", "customer42@email.com", Role.CUSTOMER),
    UserDto(49, "customer43", "12345678", "customer43@email.com", Role.CUSTOMER),
    UserDto(50, "customer44", "12345678", "customer44@email.com", Role.CUSTOMER)
)


    override suspend fun getItems(
        page: Int,
        pageSize: Int,
        query: String?
    ): List<UserDto> {
        delay(300)

        val filtered = query?.let { q ->
            users.filter {
                it.username.contains(q, true) ||
                it.email.contains(q, true) ||
                it.role.name.contains(q, true)
            }
        } ?: users

        val start = page * pageSize
        return filtered.drop(start).take(pageSize)
    }

    override suspend fun add(item: UserDto) {
        users.add(item)
    }

    override suspend fun remove(item: UserDto) {
        users.removeIf { it.id == item.id }
    }
}
