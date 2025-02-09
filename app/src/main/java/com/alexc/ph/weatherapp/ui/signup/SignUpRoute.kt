package com.alexc.ph.weatherapp.ui.signup

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.alexc.ph.weatherapp.AccountManager
import kotlinx.serialization.Serializable

@Serializable
data object SignUpRoute

fun NavController.navigateToSignUp(navOptions: NavOptions? = null) =
    navigate(route = SignUpRoute, navOptions = navOptions)

fun NavGraphBuilder.signupScreen(
    onBackClick: () -> Unit,
    accountManager: AccountManager,
    showSnackBar: suspend (message: String) -> Unit,
) {
    composable<SignUpRoute> {
        SignUpScreen(
            onBackClick = onBackClick,
            accountManager = accountManager,
            showSnackBar = {
                showSnackBar(it)
            }
        )
    }
}

