package com.alexc.ph.weatherapp.ui.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object LoginRoute

fun NavController.navigateToLogin(navOptions: NavOptions? = null) =
    navigate(route = LoginRoute, navOptions = navOptions)

fun NavGraphBuilder.loginScreen(
    showSnackBar: suspend (message: String) -> Unit,
    navigateToHome: () -> Unit,
    navigateToSignUp: () -> Unit
) {
    composable<LoginRoute> {
        LoginScreen(
            showSnackBar = showSnackBar,
            navigateToHome = navigateToHome,
            navigateToSignUp = navigateToSignUp
        )
    }
}