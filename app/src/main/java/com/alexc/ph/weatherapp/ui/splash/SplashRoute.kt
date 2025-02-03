package com.alexc.ph.weatherapp.ui.splash

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object SplashRoute

fun NavGraphBuilder.splashScreen(
    navigateToHome: () -> Unit,
    navigateToSignIn: () -> Unit,
) {
    composable<SplashRoute> {
        SplashScreen(
            navigateToHome = navigateToHome,
            navigateToSignIn = navigateToSignIn
        )
    }
}