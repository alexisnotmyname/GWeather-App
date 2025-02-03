

package com.alexc.ph.weatherapp.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexc.ph.weatherapp.ui.icons.GWeatherIcons.Lock
import com.alexc.ph.weatherapp.ui.icons.GWeatherIcons.Visibility
import com.alexc.ph.weatherapp.ui.icons.GWeatherIcons.VisibilityOff
import com.alexc.ph.weatherapp.R.string as AppText

@Composable
fun BasicField(
  @StringRes text: Int,
  value: String,
  onNewValue: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  OutlinedTextField(
    singleLine = true,
    modifier = modifier,
    value = value,
    onValueChange = { onNewValue(it) },
    placeholder = { Text(stringResource(text)) }
  )
}

@Composable
fun EmailField(
  modifier: Modifier = Modifier,
  value: String,
  onNewValue: (String) -> Unit,
  isError: Boolean = false,
) {
  TextField(
    modifier = modifier,
    singleLine = true,
    value = value,
    onValueChange = { onNewValue(it) },
    placeholder = { Text(stringResource(AppText.email)) },
    leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email") },
    shape = RoundedCornerShape(8.dp),
    isError = isError,
  )
}

@Composable
fun PasswordField(
  modifier: Modifier = Modifier,
  value: String,
  onNewValue: (String) -> Unit,
  isError: Boolean = false,
) {
  PasswordField(modifier, value, AppText.password, onNewValue, isError)
}

@Composable
fun RepeatPasswordField(
  modifier: Modifier = Modifier,
  value: String,
  onNewValue: (String) -> Unit,
  isError: Boolean = false,
) {
  PasswordField(modifier, value, AppText.repeat_password, onNewValue, isError)
}

@Composable
private fun PasswordField(
  modifier: Modifier = Modifier,
  value: String,
  @StringRes placeholder: Int,
  onNewValue: (String) -> Unit,
  isError: Boolean = false
) {
  var isVisible by remember { mutableStateOf(false) }

  val icon =
    if (isVisible) Visibility
    else VisibilityOff

  val visualTransformation =
    if (isVisible) VisualTransformation.None else PasswordVisualTransformation()

  TextField(
    modifier = modifier,
    value = value,
    onValueChange = { onNewValue(it) },
    placeholder = { Text(text = stringResource(placeholder)) },
    leadingIcon = { Icon(imageVector = Lock, contentDescription = "Lock") },
    trailingIcon = {
      IconButton(onClick = { isVisible = !isVisible }) {
        Icon(imageVector = icon, contentDescription = "Visibility")
      }
    },
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
    visualTransformation = visualTransformation,
    shape = RoundedCornerShape(8.dp),
    isError = isError,
  )
}

@Preview(showBackground = true)
@Composable
fun EmailFieldPreview() {
  EmailField(Modifier,"email@test.com", {})
}

@Preview(showBackground = true)
@Composable
fun PasswordFieldPreview()  {
  PasswordField(
    value = "",
    onNewValue = { },
    modifier = Modifier
  )
}
