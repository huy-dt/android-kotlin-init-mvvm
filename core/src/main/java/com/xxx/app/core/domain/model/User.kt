package com.xxx.app.core.domain.model

enum class Role {
    ADMIN, STAFF, CUSTOMER
}

data class User(
    val id: Long,
    val username: String,
    val email: String = "",
    val role: Role
)
