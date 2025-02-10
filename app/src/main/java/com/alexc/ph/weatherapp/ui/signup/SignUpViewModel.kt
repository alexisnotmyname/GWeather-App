package com.alexc.ph.weatherapp.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexc.ph.domain.SignUpUseCase
import com.alexc.ph.domain.model.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase
): ViewModel(){

    var state = MutableStateFlow(SignUpState())
        private set

    fun onAction(action: SignUpAction) {
        when(action) {
            is SignUpAction.OnEmailChange -> { state.update { it.copy(email = action.email, error = null) } }
            is SignUpAction.OnPasswordChange -> state.update { it.copy(password = action.password, error = null) }
            is SignUpAction.OnRepeatPasswordChange -> state.update { it.copy(repeatPassword = action.repeatPassword, error = null) }
            is SignUpAction.OnSignUpClick -> {
                viewModelScope.launch {
                    signUpUseCase(action.email, action.password, action.repeatPassword)
                        .collect { result ->
                            when (result) {
                                is Result.Error -> {
                                    state.update {
                                        it.copy(error = result.exception, isLoading = false, isSuccessSignUp = false)
                                    }
                                }
                                Result.Loading -> {
                                    state.update {
                                        it.copy(isLoading = true)
                                    }
                                }
                                is Result.Success -> {
                                    state.update {
                                        it.copy(isLoading = false, isSuccessSignUp = true)
                                    }
                                }
                            }
                        }
                }
            }
        }
    }
}
