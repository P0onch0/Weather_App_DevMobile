package com.legaut.devmobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.legaut.devmobile.data.WeatherApiDataSource
import com.legaut.devmobile.domain.Weather
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(val weatherApiDataSource: WeatherApiDataSource) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _weatherList = MutableStateFlow<List<Weather>>(emptyList())
    val weatherList: StateFlow<List<Weather>> = _weatherList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun searchWeather(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val results = weatherApiDataSource.searchWeather(query)
                _weatherList.value = results
            } catch (e: Exception) {
                _weatherList.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun getWeatherDetail(cityName: String): Weather? {
        return weatherApiDataSource.getWeatherDetail(cityName)
    }
}
