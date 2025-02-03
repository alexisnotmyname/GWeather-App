package com.alexc.ph.weatherapp.ui.splash

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexc.ph.weatherapp.R
import com.alexc.ph.weatherapp.ui.components.LoadingScreen

@Composable
fun SplashScreen(
    splashViewModel: SplashViewModel = hiltViewModel(),
    navigateToHome: () -> Unit,
    navigateToSignIn:() -> Unit
) {

    val uiState by splashViewModel.splashUiState.collectAsStateWithLifecycle()
    when(uiState) {
        is SplashUiState.Error ->  SplashScreenError { navigateToSignIn() }
        SplashUiState.Loading -> LoadingScreen()
        SplashUiState.LoggedIn -> navigateToHome()
        SplashUiState.NoExistingSession -> navigateToSignIn()
    }
}

@Composable
fun SplashScreenError(
    modifier: Modifier = Modifier,
    navigateToSignIn: () -> Unit
) {
    Column(
        modifier =
        modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.generic_error))
        Button (
            onClick = navigateToSignIn,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp),
            colors =
            ButtonDefaults.buttonColors()
        ) {
            Text(text = stringResource(R.string.try_again), fontSize = 16.sp)
        }
    }

}