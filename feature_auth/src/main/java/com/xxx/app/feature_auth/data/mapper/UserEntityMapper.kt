package com.xxx.app.feature_auth.data.mapper

import com.xxx.app.database.entity.UserEntity
import com.xxx.app.feature_auth.domain.model.UserDto

fun UserEntity.toDto(): UserDto =
    UserDto(
        id = id,
        username = username,
        email = email,
        role = role,
        password = password
    )
