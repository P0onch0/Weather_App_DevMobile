package com.legaut.devmobile.data

import com.legaut.devmobile.domain.Weather
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable

interface WeatherDataSource {
    suspend fun searchWeather(query: String): List<Weather>
    suspend fun getWeatherDetail(cityName: String): Weather?
}

@Serializable
data class GeocodingResponse(val results: List<CityDto>? = null)

@Serializable
data class CityDto(
    val id: Long,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String? = null
)

@Serializable
data class ForecastResponse(val current: CurrentWeatherDto? = null)

@Serializable
data class CurrentWeatherDto(
    val temperature_2m: Double,
    val relative_humidity_2m: Int,
    val weather_code: Int,
    val wind_speed_10m: Double
)

class WeatherApiDataSource(val client: HttpClient) : WeatherDataSource {

    companion object {
        private const val GEOCODE_URL = "https://geocoding-api.open-meteo.com/v1/search"
        private const val FORECAST_URL = "https://api.open-meteo.com/v1/forecast"
    }

    private val cachedWeather = mutableMapOf<String, Weather>()

    override suspend fun searchWeather(query: String): List<Weather> {
        if (query.isBlank()) return emptyList()
        return try {
            val geoResponse: GeocodingResponse = client.get(GEOCODE_URL) {
                parameter("name", query)
                parameter("count", 10)
                parameter("format", "json")
            }.body()

            val cities = geoResponse.results ?: emptyList()
            val weatherList = mutableListOf<Weather>()

            for (city in cities) {
                try {
                    val forecast: ForecastResponse = client.get(FORECAST_URL) {
                        parameter("latitude", city.latitude)
                        parameter("longitude", city.longitude)
                        parameter("current", "temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m")
                    }.body()

                    val current = forecast.current
                    val condition = getWeatherCondition(current?.weather_code ?: 0)
                    val weather = Weather(
                        id = city.id,
                        cityName = city.name,
                        country = city.country ?: "",
                        temperature = current?.temperature_2m ?: 0.0,
                        condition = condition,
                        humidity = current?.relative_humidity_2m ?: 0,
                        windSpeed = current?.wind_speed_10m ?: 0.0
                    )
                    cachedWeather[city.name.lowercase()] = weather
                    weatherList.add(weather)
                } catch (e: Exception) {
                    // skip city on error
                }
            }
            weatherList
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getWeatherDetail(cityName: String): Weather? {
        val key = cityName.lowercase()
        return cachedWeather[key] ?: searchWeather(cityName).firstOrNull { it.cityName.equals(cityName, ignoreCase = true) }
    }

    private fun getWeatherCondition(code: Int): String {
        return when (code) {
            0 -> "Ensoleillé"
            1, 2, 3 -> "Partiellement nuageux"
            45, 48 -> "Brouillard"
            51, 53, 55, 56, 57 -> "Bruine"
            61, 63, 65, 66, 67 -> "Pluie"
            71, 73, 75, 77 -> "Neige"
            80, 81, 82 -> "Averses"
            95, 96, 99 -> "Orage"
            else -> "Inconnu"
        }
    }
}
