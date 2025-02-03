package com.alexc.ph.weatherapp.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexc.ph.domain.SignUpUseCase
import com.alexc.ph.domain.model.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
): ViewModel(){

    private val _uiState = MutableStateFlow<SignUpUiState>(SignUpUiState.Loading)
    val uiState: StateFlow<SignUpUiState> = _uiState

    fun signUp(email: String, password: String, repeatPassword: String) {
        viewModelScope.launch {
            signUpUseCase(email, password, repeatPassword)
                .map {
                    when (it) {
                        is Result.Error -> SignUpUiState.Error(it.exception)
                        Result.Loading -> SignUpUiState.Loading
                        is Result.Success -> SignUpUiState.Success
                    }
                }
                .catch { Result.Error(it) }
                .collect { _uiState.value = it }
        }
    }
}

sealed interface SignUpUiState {
    object Loading : SignUpUiState
    object Success : SignUpUiState
    data class Error(val error: Throwable) : SignUpUiState
}

