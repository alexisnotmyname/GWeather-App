package com.alexc.ph.weatherapp.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexc.ph.weatherapp.utils.AccountManager
import com.alexc.ph.weatherapp.R
import com.alexc.ph.weatherapp.utils.SignInResult
import com.alexc.ph.weatherapp.ui.components.EmailField
import com.alexc.ph.weatherapp.ui.components.LoadingScreen
import com.alexc.ph.weatherapp.ui.components.PasswordField
import com.alexc.ph.weatherapp.ui.components.getErrorMessage


val SmallDp = 8.dp
val MediumDp = 16.dp

@Composable
fun LoginScreen(
    accountManager: AccountManager,
    loginViewModel: LoginViewModel = hiltViewModel(),
    showSnackBar: suspend (message: String) -> Unit,
    navigateToHome: () -> Unit,
    navigateToSignUp: () -> Unit
) {
    var signingIn by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = isFocused) {
        if(isFocused) {
            val result = accountManager.signIn()
            if(result is SignInResult.Success) {
                signingIn = true
                loginViewModel.login(result.email, result.password)
            }
        }
    }

    LoginScreen(
        modifier = Modifier.fillMaxSize(),
        loginClick = { email, password ->
            signingIn = true
            loginViewModel.login(email, password)
        },
        onFocused = { isFocused = it },
        signUpClick = navigateToSignUp
    )


    val uiState by loginViewModel.loginState.collectAsStateWithLifecycle()
    if(signingIn) {
        when(uiState) {
            is LoginUiState.Error -> {
                val message = getErrorMessage((uiState as LoginUiState.Error).error)
                LaunchedEffect(Unit) {
                    showSnackBar(message)
                    signingIn = false
                }
            }
            LoginUiState.Loading -> LoadingScreen()
            is LoginUiState.Success -> navigateToHome()
        }
    }
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginError: Boolean = false,
    onLoginErrorDismiss:() -> Unit = {},
    loginClick: (email: String, password: String) -> Unit = { _, _ -> },
    onFocused: (Boolean) -> Unit = {},
    signUpClick: () -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .padding(MediumDp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailField(
            value = email,
            onNewValue = {
                email = it
                if(loginError) onLoginErrorDismiss()
            },
            isError = loginError,
            modifier = Modifier.fillMaxWidth().onFocusChanged {
                onFocused(it.isFocused)
            }
        )
        Spacer(modifier = Modifier.height(SmallDp))
        PasswordField(
            value = password,
            onNewValue = {
                password = it
                if(loginError) onLoginErrorDismiss()
            },
            isError = loginError,
            modifier = Modifier.fillMaxWidth().onFocusChanged {
                onFocused(it.isFocused)
            }
        )
        Spacer(modifier = Modifier.height(MediumDp))

        if(loginError) {
            Text(
                text = stringResource(R.string.login_error),
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = MediumDp)
            )
        }

        Button(
            onClick = {
                keyboardController?.hide()
                loginClick(email, password)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
        ) {
            Text(
                text = stringResource(R.string.signin),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
            )
        }
        Spacer(modifier = Modifier.height(MediumDp))
        Button(
            onClick = signUpClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
        ) {
            Text(
                text = stringResource(R.string.register),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen()
}
