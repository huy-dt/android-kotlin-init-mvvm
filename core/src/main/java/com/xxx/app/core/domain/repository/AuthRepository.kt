package com.xxx.app.core.domain.repository

import com.xxx.app.core.domain.model.User
import com.xxx.app.core.domain.valueobject.Username
import com.xxx.app.core.domain.valueobject.Password
import com.xxx.app.core.domain.valueobject.Email

interface AuthRepository {

    suspend fun login(
        username: Username,
        password: Password
    ): User

    suspend fun isUsernameTaken(
        username: Username
    ): Boolean

    suspend fun register(
        username: Username,
        email: Email,
        password: Password
    ): User

    suspend fun logout()
}
