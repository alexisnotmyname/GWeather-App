package com.alexc.ph.weatherapp.ui.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.alexc.ph.weatherapp.AccountManager
import kotlinx.serialization.Serializable

@Serializable
data object LoginRoute

fun NavController.navigateToLogin(navOptions: NavOptions? = null) =
    navigate(route = LoginRoute, navOptions = navOptions)

fun NavGraphBuilder.loginScreen(
    accountManager: AccountManager,
    showSnackBar: suspend (message: String) -> Unit,
    navigateToHome: () -> Unit,
    navigateToSignUp: () -> Unit
) {
    composable<LoginRoute> {
        LoginScreen(
            accountManager = accountManager,
            showSnackBar = showSnackBar,
            navigateToHome = navigateToHome,
            navigateToSignUp = navigateToSignUp
        )
    }
}