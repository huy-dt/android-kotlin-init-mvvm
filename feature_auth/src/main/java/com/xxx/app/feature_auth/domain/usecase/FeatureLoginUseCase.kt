package com.xxx.app.feature_auth.domain.usecase

import com.xxx.app.core.domain.usecase.auth.LoginUseCase
import com.xxx.app.core.domain.result.UseCaseResult
import com.xxx.app.core.domain.model.User
import com.xxx.app.core.domain.exception.auth.WeakPasswordException
import javax.inject.Inject

// chỉ cần bind DI khi là interface/abstract
class FeatureLoginUseCase @Inject constructor(
    private val loginUseCase: LoginUseCase
) {

    suspend operator fun invoke(
        username: String,
        password: String
    ): UseCaseResult<User> {

        // Rule RIÊNG của feature (nếu có)
        // if (!password.contains("!")) {
        //     return UseCaseResult.Error(
        //         WeakPasswordException()
        //     )
        // }

        // Delegate về core
        return loginUseCase(username, password)
    }
}
