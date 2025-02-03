package com.alexc.ph.data.repository

import com.alexc.ph.data.database.dao.WeatherHistoryDao
import com.alexc.ph.data.database.model.toDomainWeatherHistory
import com.alexc.ph.data.database.model.toEntityWeatherHistory
import com.alexc.ph.data.network.model.toDomainWeather
import com.alexc.ph.data.network.retrofit.WeatherService
import com.alexc.ph.domain.model.weather.Weather
import com.alexc.ph.domain.model.weather.WeatherHistory
import com.alexc.ph.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherService: WeatherService,
    private val weatherHistoryDao: WeatherHistoryDao
) : WeatherRepository {
    override suspend fun getCurrentWeatherData(
        lat: String,
        lon: String,
        units: String
    ): Flow<Weather> = flow {
        emit(weatherService.getCurrentWeatherData(lat = lat, lon = lon, units = units).toDomainWeather())
    }

    override suspend fun insertWeatherHistory(weatherHistory: WeatherHistory) {
        weatherHistoryDao.insertWeatherHistory(weatherHistory.toEntityWeatherHistory())
    }

    override fun getWeatherHistory(): Flow<List<WeatherHistory>> {
        return weatherHistoryDao.getAllWeatherHistory().map {
            it.map { weather -> weather.toDomainWeatherHistory() }
        }
    }

}