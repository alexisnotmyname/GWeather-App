package com.alexc.ph.domain

import com.alexc.ph.domain.model.weather.WeatherHistory
import com.alexc.ph.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeatherHistoryUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    operator fun invoke(): Flow<List<WeatherHistory>> {
        return weatherRepository.getWeatherHistory()
    }
}