package com.xxx.app.core.domain.usecase.auth

import com.xxx.app.core.domain.model.User
import com.xxx.app.core.domain.repository.AuthRepository
import com.xxx.app.core.domain.valueobject.Username
import com.xxx.app.core.domain.valueobject.Password
import com.xxx.app.core.domain.valueobject.Email
import com.xxx.app.core.domain.result.UseCaseResult
import com.xxx.app.core.domain.exception.DomainException
import com.xxx.app.core.domain.exception.auth.UsernameAlreadyExistsException
import com.xxx.app.core.domain.validator.RegisterPasswordValidator
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(
        username: String,
        email: String,
        password: String
    ): UseCaseResult<User> =
        try {
            // 1. Policy riêng của register (nếu có)
            RegisterPasswordValidator.validate(password)

            // 2. Invariant
            val usernameVO = Username(username)
            val passwordVO = Password(password)
            val emailVO = Email(email)

            // 3. Business rule: unique username
            if (repository.isUsernameTaken(usernameVO)) {
                throw UsernameAlreadyExistsException()
            }

            // 4. Delegate IO
            val user = repository.register(
                username = usernameVO,
                email = emailVO,
                password = passwordVO
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
