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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.alexc.ph.weatherapp.ui.components.EmailField
import com.alexc.ph.weatherapp.ui.components.GWeatherTopAppBar
import com.alexc.ph.weatherapp.ui.components.LoadingScreen
import com.alexc.ph.weatherapp.ui.components.PasswordField
import com.alexc.ph.weatherapp.ui.components.RepeatPasswordField
import com.alexc.ph.weatherapp.ui.components.getErrorMessage
import com.alexc.ph.weatherapp.ui.icons.GWeatherIcons
import com.alexc.ph.weatherapp.ui.login.MediumDp

@Composable
fun SignUpScreen(
    onBackClick: () -> Unit,
    showSnackBar: suspend (message: String) -> Unit,
    accountManager: AccountManager,
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    SignUpScreen(
        uiState = state,
        modifier = Modifier.fillMaxSize(),
        onBackClick = onBackClick,
        onAction = viewModel::onAction
    )

    val message = stringResource(R.string.account_creation_success)
    LaunchedEffect(state.isSuccessSignUp) {
        if(state.isSuccessSignUp) {
            accountManager.signUp(state.email, state.password)
            showSnackBar(message)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    uiState: SignUpState,
    onBackClick: () -> Unit,
    onAction: (SignUpAction) -> Unit,
) {
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
            EmailField(value = uiState.email, onNewValue = { onAction(SignUpAction.OnEmailChange(it)) }, modifier = fieldModifier)
            PasswordField(value = uiState.password, onNewValue = { onAction(SignUpAction.OnPasswordChange(it)) }, modifier = fieldModifier)
            RepeatPasswordField(value = uiState.repeatPassword, onNewValue = { onAction(SignUpAction.OnRepeatPasswordChange(it)) }, modifier = fieldModifier)

            if(uiState.error != null) {
                Text(
                    text = getErrorMessage(uiState.error),
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = MediumDp)
                )
            }
            Button(
                onClick = {
                    onAction(SignUpAction.OnSignUpClick(uiState.email, uiState.password, uiState.repeatPassword))
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

        if(uiState.isLoading) {
            LoadingScreen(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(
        uiState = SignUpState(),
        onBackClick = { },
        onAction = { }
    )
}