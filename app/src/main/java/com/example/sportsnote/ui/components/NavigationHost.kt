package com.example.sportsnote.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sportsnote.ui.LocalNavController
import com.example.sportsnote.ui.note.AddTournamentNoteScreen
import com.example.sportsnote.ui.note.NoteScreen
import com.example.sportsnote.ui.target.TargetScreen
import com.example.sportsnote.ui.task.TaskScreen

@Composable
fun NavigationHost(paddingValues: PaddingValues) {
    val navController = LocalNavController.current
    NavHost(navController, startDestination = "task") {
        composable("task") { TaskScreen() }
        composable("note") { NoteScreen() }
        composable("add_tournament_note") {
            AddTournamentNoteScreen(
                onDismiss = { navController.popBackStack() },
                isNavigation = true
            )
        }
        composable("target") { TargetScreen() }
    }
}
