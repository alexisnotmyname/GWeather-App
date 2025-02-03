package com.alexc.ph.data.network.model

import com.alexc.ph.domain.model.weather.Main
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MainResponse(
    val temp: Double,
    val humidity: Int,
    val pressure: Int,
    @SerialName("feels_like")
    val feelsLike: Double,
    @SerialName("grnd_level")
    val grndLevel: Int,
    @SerialName("sea_level")
    val seaLevel: Int,
    @SerialName("temp_max")
    val tempMax: Double,
    @SerialName("temp_min")
    val tempMin: Double
)

fun MainResponse.toDomainMain() = Main(
    temp = temp,
    humidity = humidity,
    pressure = pressure,
    feelsLike = feelsLike,
    grndLevel = grndLevel,
    seaLevel = seaLevel,
    tempMax = tempMax,
    tempMin = tempMin
)