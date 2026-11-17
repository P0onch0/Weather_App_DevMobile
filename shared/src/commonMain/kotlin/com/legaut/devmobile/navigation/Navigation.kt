package com.legaut.devmobile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.entryProvider
import androidx.savedstate.serialization.SavedStateConfiguration
import androidx.navigation3.ui.NavDisplay
import com.legaut.devmobile.ui.DetailScreen
import com.legaut.devmobile.ui.SearchScreen
import com.legaut.devmobile.viewmodel.MainViewModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.koin.compose.viewmodel.koinViewModel

@Serializable
sealed interface Routes : NavKey {
    @Serializable
    data object SearchRoute : Routes

    @Serializable
    data class DetailRoute(val cityName: String) : Routes
}

private val navConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Routes.SearchRoute::class, Routes.SearchRoute.serializer())
            subclass(Routes.DetailRoute::class, Routes.DetailRoute.serializer())
        }
    }
}

@Composable
fun AppNavigation() {
    val viewModel: MainViewModel = koinViewModel()
    val backStack = rememberNavBackStack(navConfig, Routes.SearchRoute)

    val entryProvider = entryProvider<NavKey> {
        entry<Routes.SearchRoute> {
            SearchScreen(
                viewModel = viewModel,
                onNavigateToDetail = { cityName ->
                    backStack.add(Routes.DetailRoute(cityName))
                }
            )
        }
        entry<Routes.DetailRoute> { key ->
            DetailScreen(
                cityName = key.cityName,
                viewModel = viewModel,
                onBack = {
                    if (backStack.size > 1) {
                        backStack.removeAt(backStack.size - 1)
                    }
                }
            )
        }
    }

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider
    )
}
