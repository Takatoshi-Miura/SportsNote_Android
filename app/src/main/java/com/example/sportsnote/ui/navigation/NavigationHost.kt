package com.example.sportsnote.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavigationHost(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(navController, startDestination = "home") {
        composable("home") { HomeScreen() }
        composable("dashboard") { DashboardScreen() }
        composable("notifications") { NotificationScreen() }
    }
}
