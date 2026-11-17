package com.legaut.devmobile.domain

import kotlinx.serialization.Serializable

@Serializable
data class Weather(
    val id: Long,
    val cityName: String,
    val country: String,
    val temperature: Double,
    val condition: String,
    val humidity: Int,
    val windSpeed: Double
)
