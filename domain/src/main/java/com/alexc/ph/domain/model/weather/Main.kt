package com.alexc.ph.domain.model.weather


data class Main(
    val temp: Double,
    val humidity: Int,
    val pressure: Int,
    val feelsLike: Double,
    val grndLevel: Int,
    val seaLevel: Int,
    val tempMax: Double,
    val tempMin: Double
)