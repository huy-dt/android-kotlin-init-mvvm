package com.xxx.app.feature_.domain.model

data class UserDto(
    val id: Long,
    val username: String,
    val password: String,
    val email: String,
    val role: String
)
