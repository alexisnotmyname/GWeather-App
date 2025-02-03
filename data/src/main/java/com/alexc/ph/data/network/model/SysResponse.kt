package com.alexc.ph.data.network.model

import com.alexc.ph.domain.model.weather.Sys
import kotlinx.serialization.Serializable

@Serializable
data class SysResponse(
    val country: String,
    val id: Int,
    val sunrise: Long,
    val sunset: Long,
    val type: Int
)

fun SysResponse.toDomainSys() = Sys(
    country = country,
    id = id,
    sunrise = sunrise,
    sunset = sunset,
    type = type
)