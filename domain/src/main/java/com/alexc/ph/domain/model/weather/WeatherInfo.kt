package com.alexc.ph.domain.model.weather

data class WeatherInfo(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)