package com.xxx.app.database.mapper

import com.xxx.app.core.domain.model.User
import com.xxx.app.core.domain.model.Role
import com.xxx.app.database.entity.UserEntity

fun UserEntity.toDomain(): User = User(
    id = id,
    username = username,
    email = email,
    role = Role.valueOf(role)
)
