package com.alexc.ph.data.network.model

import com.alexc.ph.domain.model.weather.Weather
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val id: Int,
    val name: String,
    val timezone: Int,
    val coord: CoordResponse,
    val main: MainResponse,
    val sys: SysResponse,
    val weather: List<WeatherInfoResponse>,
)

fun WeatherResponse.toDomainWeather() = Weather(
    id = id,
    name = name,
    timezone = timezone,
    coord = coord.toDomainCoord(),
    main = main.toDomainMain(),
    sys = sys.toDomainSys(),
    weather = weather.map { it.toDomainWeatherInfo() }
)