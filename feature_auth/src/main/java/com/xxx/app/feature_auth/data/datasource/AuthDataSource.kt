package com.xxx.app.feature_auth.data.datasource

import com.xxx.app.feature_auth.domain.model.UserDto

interface AuthDataSource {

    suspend fun login(
        username: String,
        password: String
    ): UserDto?

    suspend fun register(
        username: String,
        email: String,
        password: String
    ): UserDto

    suspend fun existsByUsername(
        username: String
    ): Boolean

    suspend fun logout()
}
