package com.xxx.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.xxx.app.feature_user.ui.ListUserScreen
// import com.xxx.app.feature_auth.ui.AuthTabScreen

@Composable
fun AppNavHost() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "list_user"
    ) {

        composable("list_user") {
            ListUserScreen()
        }

        // sau này mở lại auth thì thêm vào
        // composable("auth") { AuthTabScreen() }

        // composable("home") { HomeScreen() }
    }
}
