package com.xxx.app.feature_auth.data.datasource

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xxx.app.feature_auth.domain.model.UserDto
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AuthFakeDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) : AuthDataSource {

    private val gson = Gson()

    private fun loadUsers(): List<UserDto> {
        val json = context.assets
            .open("users.json")
            .bufferedReader()
            .use { it.readText() }

        val type = object : TypeToken<List<UserDto>>() {}.type
        return gson.fromJson(json, type)
    }

    override suspend fun login(
        username: String,
        password: String
    ): UserDto? {
        val users = loadUsers()

        return users.firstOrNull {
            it.username == username && it.password == password
        }
    }

    override suspend fun existsByUsername(
        username: String
    ): Boolean {
        return loadUsers().any { it.username == username }
    }

    override suspend fun register(
        username: String,
        email: String,
        password: String
    ): UserDto {
        // Fake nên chỉ return mock data
        return UserDto(
            id = System.currentTimeMillis(),
            username = username,
            email = email,
            role = "CUSTOMER",
            password = password
        )
    }

    override suspend fun logout() {
        // no-op
    }
}
