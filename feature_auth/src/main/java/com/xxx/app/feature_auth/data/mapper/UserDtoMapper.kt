// data/mapper/UserDtoMapper.kt
package com.xxx.app.feature_auth.data.mapper

import com.xxx.app.feature_auth.domain.model.UserDto
import com.xxx.app.core.domain.model.User
import com.xxx.app.core.domain.model.Role

fun UserDto.toDomain(): User = User(
    id = id,
    username = username,
    email = email.orEmpty(),
    role = Role.valueOf(role)
)
