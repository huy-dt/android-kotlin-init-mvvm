package com.xxx.app.feature_auth.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xxx.app.core.domain.result.UseCaseResult
import com.xxx.app.feature_auth.domain.usecase.FeatureRegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val featureRegisterUseCase: FeatureRegisterUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun register(
        username: String,
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            _uiState.value = RegisterUiState.Loading

            when (
                val result = featureRegisterUseCase(
                    username = username,
                    email = email,
                    password = password
                )
            ) {
                is UseCaseResult.Success -> {
                    _uiState.value =
                        RegisterUiState.Success(result.data)
                }

                is UseCaseResult.Error -> {
                    _uiState.value =
                        RegisterUiState.Error(
                            result.exception.toRegisterUiMessage()
                        )
                }
            }
        }
    }

    fun resetState() {
        _uiState.value = RegisterUiState.Idle
    }
}
