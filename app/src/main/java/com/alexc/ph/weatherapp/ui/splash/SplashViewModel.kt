package com.alexc.ph.weatherapp.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexc.ph.domain.IsAuthenticatedUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class SplashViewModel (
    val isAuthenticatedUseCase: IsAuthenticatedUseCase
): ViewModel() {

    private val _splashUiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val splashUiState = _splashUiState
        .onStart {
            checkAuthState() }
        .catch { emit(SplashUiState.Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SplashUiState.Loading)

    private fun checkAuthState() {
        if(isAuthenticatedUseCase()) {
            _splashUiState.value = SplashUiState.LoggedIn
        } else {
            _splashUiState.value = SplashUiState.NoExistingSession
        }
    }

}

sealed interface SplashUiState {
    object Loading : SplashUiState
    data class Error(val exception: Throwable) : SplashUiState
    object LoggedIn : SplashUiState
    object NoExistingSession : SplashUiState
}