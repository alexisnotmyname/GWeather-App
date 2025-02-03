package com.alexc.ph.domain

import com.alexc.ph.domain.model.Result
import com.alexc.ph.domain.model.weather.Weather
import com.alexc.ph.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    operator fun invoke(
        lat: String,
        lon: String,
        units: String
    ): Flow<Result<Weather>> = flow {
        emit(Result.Loading )
        try {
            weatherRepository.getCurrentWeatherData(lat = lat, lon = lon, units = units)
                .map { Result.Success(it) }
                .collect { result ->
                    emit(result)
                }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }

    }
}