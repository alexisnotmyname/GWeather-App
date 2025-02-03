package com.alexc.ph.weatherapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.alexc.ph.weatherapp.ui.home.navigateToHome
import com.alexc.ph.weatherapp.ui.login.navigateToLogin
import com.alexc.ph.weatherapp.ui.signup.navigateToSignUp

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController()
)  = remember(navController) {
    GWeatherAppState(navController)
}


class GWeatherAppState(
    val navController: NavHostController
) {

    fun popUp() {
        navController.popBackStack()
    }

    fun navigateToSignUp() {
        navController.navigateToSignUp()
    }

    fun navigateToLogin() {
        navController.navigateToLogin()
    }

    fun navigateToHome() {
        navController.navigateToHome()
    }

    fun navigateAndPopUp(route: String, popUp: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(popUp) { inclusive = true }
        }
    }

    fun clearAndNavigate(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(0) { inclusive = true }
        }
    }
}