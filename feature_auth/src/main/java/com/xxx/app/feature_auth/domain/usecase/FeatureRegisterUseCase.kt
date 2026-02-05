package com.xxx.app.feature_auth.domain.usecase

import com.xxx.app.core.domain.valueobject.Username
import com.xxx.app.core.domain.usecase.auth.RegisterUseCase
import com.xxx.app.core.domain.repository.AuthRepository
import com.xxx.app.core.domain.result.UseCaseResult
import com.xxx.app.core.domain.model.User
import com.xxx.app.core.domain.exception.auth.UsernameAlreadyExistsException
import com.xxx.app.core.domain.exception.auth.WeakPasswordException
import javax.inject.Inject

class FeatureRegisterUseCase @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(
        username: String,
        email: String,
        password: String
    ): UseCaseResult<User> {

        // Rule feature: username unique
        if (authRepository.isUsernameTaken(Username(username))) {
            return UseCaseResult.Error(
                UsernameAlreadyExistsException()
            )
        }

        // Rule feature: password mạnh hơn
        if (!password.contains("!")) {
            return UseCaseResult.Error(
                WeakPasswordException()
            )
        }

        return registerUseCase(username, email, password)
    }
}
