package com.alexc.ph.weatherapp.ui.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexc.ph.domain.common.InvalidEmailException
import com.alexc.ph.domain.common.PasswordFormatInvalidException
import com.alexc.ph.domain.common.PasswordNotMatchException
import com.alexc.ph.domain.model.Result
import com.alexc.ph.weatherapp.R
import com.alexc.ph.weatherapp.ui.components.EmailField
import com.alexc.ph.weatherapp.ui.components.GWeatherTopAppBar
import com.alexc.ph.weatherapp.ui.components.LoadingScreen
import com.alexc.ph.weatherapp.ui.components.PasswordField
import com.alexc.ph.weatherapp.ui.components.RepeatPasswordField
import com.alexc.ph.weatherapp.ui.icons.GWeatherIcons

@Composable
fun SignUpScreen(
    onBackClick: () -> Unit,
    showSnackBar: suspend (message: String) -> Unit,
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var signingUp by remember { mutableStateOf(false) }

    SignUpScreen(
        modifier = Modifier.fillMaxSize(),
        onBackClick = onBackClick,
        onSignUpClick = { email, password, repeatPassword ->
            signingUp = true
            viewModel.signUp(email, password, repeatPassword)
        },
    )

    val message = when(uiState) {
        is SignUpUiState.Error -> getErrorMessage((uiState as SignUpUiState.Error).error)
        else -> stringResource(R.string.account_creation_success)
    }

    if(signingUp) {
        LaunchedEffect(uiState) {
            showSnackBar(message)
            signingUp = false
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onSignUpClick: (String, String, String) -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var repeatPassword by rememberSaveable { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current
    val fieldModifier = Modifier
        .fillMaxWidth()
        .padding(16.dp, 4.dp)

    Box(modifier = modifier.fillMaxSize()){
        GWeatherTopAppBar(
            title = stringResource(R.string.register),
            navigation = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = GWeatherIcons.Back,
                        contentDescription = null,
                    )
                }
            },
            modifier = Modifier.align(Alignment.TopCenter)
        )

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EmailField(value = email, onNewValue = { email = it }, modifier = fieldModifier)
            PasswordField(value = password, onNewValue = { password = it }, modifier = fieldModifier)
            RepeatPasswordField(value = repeatPassword, onNewValue = { repeatPassword = it }, modifier = fieldModifier)

            Button(
                onClick = {
                    onSignUpClick(email, password, repeatPassword)
                    keyboardController?.hide()

                },
                modifier = fieldModifier
            ) {
                Text(
                    text = stringResource(R.string.create_account),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }
    }
}

@Composable
fun getErrorMessage(exception: Throwable): String {
    return when(exception) {
        is InvalidEmailException -> stringResource(R.string.invalid_email_format)
        is PasswordFormatInvalidException -> stringResource(R.string.password_format_error)
        is PasswordNotMatchException -> stringResource(R.string.password_match_error)
        else -> exception.message ?: stringResource(R.string.generic_error)
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {

    SignUpScreen(
        onBackClick = { },
        onSignUpClick = { _, _, _ -> }
    )
}