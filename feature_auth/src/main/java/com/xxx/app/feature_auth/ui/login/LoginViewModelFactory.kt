// Tham khảo khi không dùng hilt
// package com.xxx.app.feature_auth.ui.login

// import androidx.lifecycle.ViewModel
// import androidx.lifecycle.ViewModelProvider
// import com.xxx.app.core.domain.usecase.auth.LoginUseCase

// class LoginViewModelFactory(
//     private val loginUseCase: LoginUseCase
// ) : ViewModelProvider.Factory {

//     override fun <T : ViewModel> create(modelClass: Class<T>): T {
//         if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
//             @Suppress("UNCHECKED_CAST")
//             return LoginViewModel(loginUseCase) as T
//         }
//         throw IllegalArgumentException("Unknown ViewModel class")
//     }
// }
