package com.legaut.devmobile

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.legaut.devmobile.navigation.AppNavigation
import com.legaut.devmobile.ui.theme.AppTheme

@Composable
@Preview
fun App() {
    AppTheme {
        AppNavigation()
    }
}
