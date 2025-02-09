package com.alexc.ph.weatherapp.ui.signup

data class SignUpState(
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val isSuccessSignUp: Boolean = false
)