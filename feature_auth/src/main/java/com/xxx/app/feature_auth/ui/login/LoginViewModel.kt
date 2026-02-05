package com.xxx.app.feature_auth.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xxx.app.core.domain.result.UseCaseResult
import com.xxx.app.feature_auth.domain.usecase.FeatureLoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val featureLoginUseCase: FeatureLoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            when (val result = featureLoginUseCase(username, password)) {
                is UseCaseResult.Success -> {
                    _uiState.value = LoginUiState.Success(result.data)
                }
                is UseCaseResult.Error -> {
                    _uiState.value = LoginUiState.Error(
                        result.exception.toUiMessage()
                    )
                }
            }
        }
    }
}
