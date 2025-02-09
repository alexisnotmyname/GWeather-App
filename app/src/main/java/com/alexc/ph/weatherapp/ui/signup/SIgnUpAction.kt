package com.alexc.ph.weatherapp.ui.signup

sealed interface SignUpAction {
    data class OnEmailChange(val email: String) : SignUpAction
    data class OnPasswordChange(val password: String) : SignUpAction
    data class OnRepeatPasswordChange(val repeatPassword: String) : SignUpAction
    data class OnSignUpClick(val email: String, val password: String, val repeatPassword: String) : SignUpAction
}