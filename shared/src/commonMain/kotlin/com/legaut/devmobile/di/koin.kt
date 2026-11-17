package com.legaut.devmobile.di

import com.legaut.devmobile.data.WeatherApiDataSource
import com.legaut.devmobile.viewmodel.MainViewModel
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel

val apiModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
    }
    single { WeatherApiDataSource(get()) }
    viewModel { MainViewModel(get()) }
}

fun initKoin(appDeclaration: org.koin.dsl.KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(apiModule)
    }
