package com.alexc.ph.data.network.model

import com.alexc.ph.domain.model.weather.Coord
import kotlinx.serialization.Serializable

@Serializable
data class CoordResponse(
    val lat: Double,
    val lon: Double
)

fun CoordResponse.toDomainCoord() = Coord(
    lat = lat,
    lon = lon
)