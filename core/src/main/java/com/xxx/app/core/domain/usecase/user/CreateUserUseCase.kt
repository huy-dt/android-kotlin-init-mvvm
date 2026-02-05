package com.xxx.app.core.domain.usecase.user

import com.xxx.app.core.domain.model.User
import com.xxx.app.core.domain.repository.UserRepository

class CreateUserUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(user: User) {
        repository.createUser(user)
    }
}
