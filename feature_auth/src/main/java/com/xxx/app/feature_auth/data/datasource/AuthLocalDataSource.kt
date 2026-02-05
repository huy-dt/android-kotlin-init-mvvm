package com.xxx.app.feature_auth.data.datasource

import com.xxx.app.database.dao.AuthDao
import com.xxx.app.database.entity.UserEntity
import com.xxx.app.feature_auth.domain.model.UserDto
import com.xxx.app.feature_auth.data.mapper.toDto
import javax.inject.Inject

class AuthLocalDataSource @Inject constructor(
    private val authDao: AuthDao
) : AuthDataSource {

    override suspend fun login(
        username: String,
        password: String
    ): UserDto? {
        val entity = authDao.findByCredentials(username, password)
        return entity?.toDto()
    }

    override suspend fun existsByUsername(
        username: String
    ): Boolean {
        return authDao.existsByUsername(username)
    }

    override suspend fun register(
        username: String,
        email: String,
        password: String
    ): UserDto {
        val entity = UserEntity(
            id = 0,
            username = username,
            email = email,
            password = password,
            role = "CUSTOMER"
        )

        val id = authDao.insert(entity)

        return entity.copy(id = id).toDto()
    }

    override suspend fun logout() {
        // no-op for local
    }
}
