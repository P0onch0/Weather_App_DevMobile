package com.legaut.devmobile.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.legaut.devmobile.domain.Weather

@Composable
expect fun WeatherGallery(
    modifier: Modifier = Modifier,
    list: List<Weather>,
    onPictureClick: (Weather) -> Unit
)
