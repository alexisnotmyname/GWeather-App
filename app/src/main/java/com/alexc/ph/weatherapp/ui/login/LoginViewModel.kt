package com.alexc.ph.weatherapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexc.ph.domain.SignInUseCase
import com.alexc.ph.domain.model.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class LoginViewModel(
    private val signInUseCase: SignInUseCase
): ViewModel() {

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Loading)
    val loginState: StateFlow<LoginUiState> = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            signInUseCase(email, password)
                .map {
                    when(it) {
                        is Result.Error -> {
                            LoginUiState.Error(it.exception)
                        }
                        Result.Loading -> LoginUiState.Loading
                        is Result.Success -> LoginUiState.Success(it.data)
                    }
                 }
                .catch { LoginUiState.Error(Exception(it)) }
                .collect { _loginState.value = it }
        }
    }
}

sealed interface LoginUiState {
    object Loading : LoginUiState
    data class Success(val token: String?) : LoginUiState
    data class Error(val error: Throwable) : LoginUiState
}