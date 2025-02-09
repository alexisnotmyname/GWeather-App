package com.alexc.ph.weatherapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexc.ph.domain.common.InvalidEmailException
import com.alexc.ph.domain.common.PasswordBlankException
import com.alexc.ph.domain.common.PasswordFormatInvalidException
import com.alexc.ph.domain.common.PasswordNotMatchException
import com.alexc.ph.weatherapp.R

@Composable
fun GenericErrorScreen(
    onRetry: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
        ) {
            Text(
                text = stringResource(id = R.string.generic_error),
                modifier = Modifier.padding(16.dp)
            )
            Button(onClick = onRetry) {
                Text(text = stringResource(id = R.string.try_again))
            }
        }
    }
}

@Composable
fun getErrorMessage(exception: Throwable): String {
    return when(exception) {
        is InvalidEmailException -> stringResource(R.string.invalid_email_format)
        is PasswordBlankException -> stringResource(R.string.empty_password_error)
        is PasswordNotMatchException -> stringResource(R.string.password_match_error)
        is PasswordFormatInvalidException -> stringResource(R.string.password_format_error)
        else -> exception.message ?: stringResource(R.string.generic_error)
    }
}

@Preview(showBackground = true)
@Composable
fun GenericErrorScreenPreview() {
    GenericErrorScreen(onRetry = {})
}
