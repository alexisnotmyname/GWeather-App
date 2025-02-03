package com.alexc.ph.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alexc.ph.domain.model.weather.WeatherHistory

@Entity(tableName = "weather_history")
data class WeatherHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val city: String,
    val country: String,
    val temperature: String,
    val sunrise: Long,
    val sunset: Long,
    val weatherCondition: String,
    val timeStamp: Long
)

fun WeatherHistoryEntity.toDomainWeatherHistory() = WeatherHistory(
    id = id,
    city = city,
    country = country,
    temperature = temperature,
    sunrise = sunrise,
    sunset = sunset,
    weatherCondition = weatherCondition,
    timeStamp = timeStamp
)

fun WeatherHistory.toEntityWeatherHistory() = WeatherHistoryEntity(
    id = id,
    city = city,
    country = country,
    temperature = temperature,
    sunrise = sunrise,
    sunset = sunset,
    weatherCondition = weatherCondition,
    timeStamp = timeStamp
)
