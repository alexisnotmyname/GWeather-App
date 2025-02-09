package com.alexc.ph.weatherapp

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import com.alexc.ph.weatherapp.ui.components.GradientBackground
import com.alexc.ph.weatherapp.ui.home.homeScreen
import com.alexc.ph.weatherapp.ui.login.loginScreen
import com.alexc.ph.weatherapp.ui.signup.signupScreen
import com.alexc.ph.weatherapp.ui.splash.SplashRoute
import com.alexc.ph.weatherapp.ui.splash.splashScreen
import com.alexc.ph.weatherapp.ui.theme.LocalGradientColors


@Composable
fun GWeatherApp() {
    GradientBackground (
        gradientColors = LocalGradientColors.current
    ){
        val context = LocalContext.current
        val accountManager = remember {
            AccountManager(context as ComponentActivity)
        }
        val appState = rememberAppState()
        val snackBarHostState = remember { SnackbarHostState() }

        Scaffold(
            snackbarHost = {
                SnackbarHost(snackBarHostState)
            },
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
        ) { innerPaddingModifier ->
            NavHost(
                navController = appState.navController,
                startDestination = SplashRoute,
                modifier = Modifier.padding(innerPaddingModifier)
            ) {
                splashScreen(
                    navigateToHome = appState::navigateToHome,
                    navigateToSignIn = appState::navigateToLogin
                )
                loginScreen(
                    accountManager = accountManager,
                    showSnackBar = snackBarHostState::showSnackbar,
                    navigateToHome = appState::navigateToHome,
                    navigateToSignUp = appState::navigateToSignUp
                )
                signupScreen(
                    onBackClick = appState::popUp,
                    accountManager = accountManager,
                    showSnackBar = snackBarHostState::showSnackbar
                )
                homeScreen(
                    onSignOut = appState::clearAndNavigate
                )
            }
        }
    }
}