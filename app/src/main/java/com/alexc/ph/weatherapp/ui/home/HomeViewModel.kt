package com.alexc.ph.weatherapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexc.ph.domain.GetCurrentWeatherUseCase
import com.alexc.ph.domain.SaveWeatherHistoryUseCase
import com.alexc.ph.domain.SignOutUseCase
import com.alexc.ph.domain.model.Result
import com.alexc.ph.domain.model.weather.Weather
import com.alexc.ph.weatherapp.utils.LocationProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val saveWeatherHistoryUseCase: SaveWeatherHistoryUseCase,
    private val locationProvider: LocationProvider,

    ): ViewModel() {

    private val _homeUiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val homeUiState = _homeUiState
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState.Loading)

    fun signOut() {
        signOutUseCase()
    }

    fun fetchWeatherData() {
        viewModelScope.launch {
            val location = locationProvider.getLastUserLocation()
            location?.let {
                val lat = location.latitude.toString()
                val long = location.longitude.toString()

                fetchWeatherData(lat, long).collect{
                    _homeUiState.value = it
                }
            } ?: run {
                val currentLocation = locationProvider.getCurrentLocation()
                val lat = currentLocation?.latitude.toString()
                val long = currentLocation?.longitude.toString()
                fetchWeatherData(lat, long).collect{
                    _homeUiState.value = it
                }
            }
        }
    }

    private fun fetchWeatherData(lat: String, long: String): Flow<HomeUiState> {
        return getCurrentWeatherUseCase(lat, long, "metric").map {
            when(it) {
                is Result.Error -> {
                    HomeUiState.Error(it.exception)
                }
                Result.Loading -> HomeUiState.Loading
                is Result.Success -> {
                    saveWeatherHistoryUseCase(it.data)
                    HomeUiState.Success(it.data)
                }
            }
        }
    }
}



sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(val weather: Weather) : HomeUiState
    data class Error(val error: Throwable) : HomeUiState
}
