package com.xxx.app.core.domain.usecase.auth

import com.xxx.app.core.domain.model.User
import com.xxx.app.core.domain.repository.AuthRepository
import com.xxx.app.core.domain.valueobject.Username
import com.xxx.app.core.domain.valueobject.Password
import com.xxx.app.core.domain.result.UseCaseResult
import com.xxx.app.core.domain.exception.DomainException

import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository,
) {

    suspend operator fun invoke(
        username: String,
        password: String
    ): UseCaseResult<User> =
        try {
            val user = repository.login(
                Username(username),
                Password(password)
            )
            UseCaseResult.Success(user)
        } catch (e: DomainException) {
            UseCaseResult.Error(e)
        } catch (e: Exception) {
            UseCaseResult.Error(
                DomainException(
                    message = e.message ?: "Unknown Error",
                    cause = e
                )
            )
        }
}
