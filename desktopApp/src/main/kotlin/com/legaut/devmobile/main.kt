package com.legaut.devmobile

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.legaut.devmobile.di.initKoin

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "DevMobile",
    ) {
        App()
    }
}
