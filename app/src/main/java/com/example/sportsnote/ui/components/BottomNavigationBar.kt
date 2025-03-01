package com.example.sportsnote.ui.components

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.sportsnote.R
import com.example.sportsnote.ui.LocalNavController

@Composable
fun BottomNavigationBar() {
    val navController = LocalNavController.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomNavigation(
        backgroundColor = MaterialTheme.colors.primary,
    ) {
        val items =
            listOf(
                Triple("task", R.drawable.baseline_format_list_bulleted_24, R.string.task),
                Triple("note", R.drawable.baseline_menu_book_24, R.string.note),
                Triple("target", Icons.Filled.DateRange, R.string.target),
            )

        items.forEach { (route, icon, labelResId) ->
            BottomNavigationItem(
                icon = {
                    when (icon) {
                        is Int -> Icon(painterResource(id = icon), contentDescription = null)
                        is ImageVector -> Icon(icon, contentDescription = null)
                    }
                },
                label = { Text(stringResource(labelResId)) },
                selected = currentRoute?.startsWith(route) == true,
                onClick = {
                    if (currentRoute != route) {
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
            )
        }
    }
}
