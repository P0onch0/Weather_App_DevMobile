package com.legaut.devmobile.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.legaut.devmobile.domain.Weather
import com.legaut.devmobile.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(
    cityName: String,
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var weather by remember { mutableStateOf<Weather?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(cityName) {
        scope.launch {
            isLoading = true
            weather = viewModel.getWeatherDetail(cityName)
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Détails météo pour $cityName", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            weather?.let { w ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Ville : ${w.cityName}, ${w.country}", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Température : ${w.temperature}°C")
                        Text("Condition : ${w.condition}")
                        Text("Humidité : ${w.humidity}%")
                        Text("Vent : ${w.windSpeed} km/h")
                    }
                }
            } ?: Text("Aucun détail disponible")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onBack) {
            Text("Retour")
        }
    }
}
