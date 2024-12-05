package com.example.sportsnote.ui.components

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.sportsnote.R

@Composable
fun BottomNavigationBar(navController: NavController) {
    BottomNavigation {
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.List, contentDescription = "Home") },
            label = { Text(stringResource(R.string.task)) },
            selected = true,
            onClick = { navController.navigate("home") }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Dashboard") },
            label = { Text(stringResource(R.string.note)) },
            selected = false,
            onClick = { navController.navigate("dashboard") }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.DateRange, contentDescription = "Notifications") },
            label = { Text(stringResource(R.string.target)) },
            selected = false,
            onClick = { navController.navigate("notifications") }
        )
    }
}
