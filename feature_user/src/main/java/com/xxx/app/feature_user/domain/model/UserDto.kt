package com.xxx.app.feature_user.domain.model

import com.xxx.app.core.domain.model.Role

data class UserDto(
    val id: Long,
    val username: String,
    val password: String,
    val email: String,
    val role: Role
)
