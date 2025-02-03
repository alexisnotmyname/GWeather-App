package com.alexc.ph.domain.model.weather

data class Weather(
    val id: Int,
    val name: String,
    val timezone: Int,
    val coord: Coord,
    val main: Main,
    val sys: Sys,
    val weather: List<WeatherInfo>,
)

fun Weather.toWeatherHistory() = WeatherHistory(
    city = name,
    country = sys.country,
    temperature = main.temp.toString(),
    sunrise = sys.sunrise,
    sunset = sys.sunset,
    weatherCondition = weather.first().main,
    timeStamp = System.currentTimeMillis()
)