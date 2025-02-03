package com.alexc.ph.data.network.model

import com.alexc.ph.domain.model.weather.WeatherInfo
import kotlinx.serialization.Serializable

@Serializable
data class WeatherInfoResponse(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)

fun WeatherInfoResponse.toDomainWeatherInfo() = WeatherInfo(
    description = description,
    icon = icon,
    id = id,
    main = main
)