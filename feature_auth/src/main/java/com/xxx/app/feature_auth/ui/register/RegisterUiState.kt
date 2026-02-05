package com.xxx.app.feature_auth.ui.register

import com.xxx.app.core.domain.model.User

sealed class RegisterUiState {

    object Idle : RegisterUiState()

    object Loading : RegisterUiState()

    data class Success(
        val user: User
    ) : RegisterUiState()

    data class Error(
        val message: String
    ) : RegisterUiState()
}
