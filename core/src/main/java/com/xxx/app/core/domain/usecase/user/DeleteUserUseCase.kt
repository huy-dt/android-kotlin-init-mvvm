package com.xxx.app.core.domain.usecase.user

import com.xxx.app.core.domain.model.User
import com.xxx.app.core.domain.repository.UserRepository

class DeleteUserUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(userId: Long) {
        repository.deleteUser(userId)
    }
}
