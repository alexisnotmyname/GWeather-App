package com.alexc.ph.weatherapp.ui.home.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexc.ph.domain.GetWeatherHistoryUseCase
import com.alexc.ph.domain.model.weather.WeatherHistory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WeatherHistoryViewModel(
    private val getWeatherHistoryUseCase: GetWeatherHistoryUseCase
) : ViewModel() {
    private val _weatherHistoryUiState = MutableStateFlow<WeatherHistoryUiState>(WeatherHistoryUiState.Loading)
    val weatherHistoryUiState: StateFlow<WeatherHistoryUiState> = _weatherHistoryUiState
        .onStart { fetchWeatherHistory() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), WeatherHistoryUiState.Loading)

    private fun fetchWeatherHistory() {
        viewModelScope.launch {
            try {
                getWeatherHistoryUseCase().collect { history ->
                    _weatherHistoryUiState.value = WeatherHistoryUiState.Success(history)
                }
            } catch (e: Exception) {
                _weatherHistoryUiState.value = WeatherHistoryUiState.Error(e)
            }
        }
    }
}

sealed interface WeatherHistoryUiState {
    object Loading : WeatherHistoryUiState
    data class Success(val weatherHistory: List<WeatherHistory>) : WeatherHistoryUiState
    data class Error(val error: Throwable) : WeatherHistoryUiState

}