package com.xxx.app.core.domain.repository

import com.xxx.app.core.domain.model.User

interface UserRepository {

    suspend fun getAllUsers(): List<User>

    suspend fun createUser(user: User)

    suspend fun updateUser(user: User)

    suspend fun deleteUser(userId: Long)
}
