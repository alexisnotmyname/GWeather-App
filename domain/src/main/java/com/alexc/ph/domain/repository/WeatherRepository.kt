package com.alexc.ph.domain.repository

import com.alexc.ph.domain.model.weather.Weather
import com.alexc.ph.domain.model.weather.WeatherHistory
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun getCurrentWeatherData(
        lat: String,
        lon: String,
        units: String
    ): Flow<Weather>

    suspend fun insertWeatherHistory(weatherHistory: WeatherHistory)

    fun getWeatherHistory(): Flow<List<WeatherHistory>>

}