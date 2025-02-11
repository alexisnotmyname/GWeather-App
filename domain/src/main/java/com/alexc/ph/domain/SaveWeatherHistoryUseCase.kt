package com.alexc.ph.domain

import com.alexc.ph.domain.model.weather.Weather
import com.alexc.ph.domain.model.weather.toWeatherHistory
import com.alexc.ph.domain.repository.WeatherRepository

class SaveWeatherHistoryUseCase(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(weather: Weather) {
        weatherRepository.insertWeatherHistory(weather.toWeatherHistory())
    }
}