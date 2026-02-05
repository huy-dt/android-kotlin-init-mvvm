package com.xxx.app.core.domain.usecase.user

import com.xxx.app.core.domain.model.User
import com.xxx.app.core.domain.repository.UserRepository

class GetUsersUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): List<User> {
        return repository.getAllUsers()
    }
}
