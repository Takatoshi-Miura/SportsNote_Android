package com.it6210.sportsnote.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.it6210.sportsnote.ui.components.NavigationHost

// CompositionLocalでNavControllerを共有
val LocalNavController =
    compositionLocalOf<NavHostController> {
        error("No NavController provided")
    }

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    CompositionLocalProvider(LocalNavController provides navController) {
        NavigationHost()
    }
}
