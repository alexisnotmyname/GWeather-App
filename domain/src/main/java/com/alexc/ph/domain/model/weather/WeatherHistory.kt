package com.alexc.ph.domain.model.weather

data class WeatherHistory(
    val id: Long = 0,
    val city: String,
    val country: String,
    val temperature: String,
    val sunrise: Long,
    val sunset: Long,
    val weatherCondition: String,
    val timeStamp: Long
)