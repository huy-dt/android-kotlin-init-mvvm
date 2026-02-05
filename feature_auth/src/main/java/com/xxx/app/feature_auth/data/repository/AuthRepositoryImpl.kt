package com.xxx.app.feature_auth.data.repository

import com.xxx.app.core.domain.model.User
import com.xxx.app.core.domain.repository.AuthRepository
import com.xxx.app.core.domain.valueobject.Username
import com.xxx.app.core.domain.valueobject.Password
import com.xxx.app.core.domain.valueobject.Email
import com.xxx.app.core.domain.exception.auth.InvalidCredentialsException
import com.xxx.app.feature_auth.data.datasource.AuthDataSource
import com.xxx.app.feature_auth.data.mapper.toDomain
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dataSource: AuthDataSource
) : AuthRepository {

    override suspend fun login(
        username: Username,
        password: Password
    ): User {
        val dto = dataSource.login(
            username = username.value,
            password = password.value
        ) ?: throw InvalidCredentialsException()

        return dto.toDomain()
    }

    override suspend fun isUsernameTaken(
        username: Username
    ): Boolean {
        return dataSource.existsByUsername(username.value)
    }

    override suspend fun register(
        username: Username,
        email: Email,
        password: Password
    ): User {
        val dto = dataSource.register(
            username = username.value,
            email = email.value,
            password = password.value
        )

        return dto.toDomain()
    }

    override suspend fun logout() {
        dataSource.logout()
    }
}
