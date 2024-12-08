package com.example.sportsnote.ui.components

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.sportsnote.R

@Composable
fun BottomNavigationBar(navController: NavController) {
    BottomNavigation {
        BottomNavigationItem(
            icon = { Icon(painterResource(id = R.drawable.baseline_format_list_bulleted_24), contentDescription = "Home") },
            label = { Text(stringResource(R.string.task)) },
            selected = true,
            onClick = { navController.navigate("task") }
        )
        BottomNavigationItem(
            icon = { Icon(painterResource(id = R.drawable.baseline_menu_book_24), contentDescription = "Dashboard") },
            label = { Text(stringResource(R.string.note)) },
            selected = false,
            onClick = { navController.navigate("note") }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.DateRange, contentDescription = "Notifications") },
            label = { Text(stringResource(R.string.target)) },
            selected = false,
            onClick = { navController.navigate("target") }
        )
    }
}
