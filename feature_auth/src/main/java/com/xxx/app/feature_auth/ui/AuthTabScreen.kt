package com.xxx.app.feature_auth.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.xxx.app.feature_auth.ui.login.LoginScreen
import com.xxx.app.feature_auth.ui.login.LoginViewModel
import com.xxx.app.feature_auth.ui.register.RegisterScreen
import com.xxx.app.feature_auth.ui.register.RegisterViewModel

@Composable
fun AuthTabScreen() {

    var selectedTab by remember { mutableStateOf(AuthTab.Login) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        TabRow(
            selectedTabIndex = selectedTab.ordinal
        ) {
            AuthTab.values().forEach { tab ->
                Tab(
                    selected = selectedTab == tab,
                    onClick = { selectedTab = tab },
                    text = { Text(tab.title) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        when (selectedTab) {
            AuthTab.Login -> {
                val loginViewModel: LoginViewModel = hiltViewModel()
                LoginScreen(viewModel = loginViewModel)
            }

            AuthTab.Register -> {
                val registerViewModel: RegisterViewModel = hiltViewModel()
                RegisterScreen(viewModel = registerViewModel)
            }
        }
    }
}

private enum class AuthTab(val title: String) {
    Login("Login"),
    Register("Register")
}
