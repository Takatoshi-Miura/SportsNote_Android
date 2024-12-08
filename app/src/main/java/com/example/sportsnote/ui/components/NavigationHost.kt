package com.example.sportsnote.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sportsnote.ui.note.NoteScreen
import com.example.sportsnote.ui.target.TargetScreen
import com.example.sportsnote.ui.task.TaskScreen

@Composable
fun NavigationHost(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(navController, startDestination = "task") {
        composable("task") { TaskScreen() }
        composable("note") { NoteScreen() }
        composable("target") { TargetScreen() }
    }
}
