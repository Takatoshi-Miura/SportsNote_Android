package com.example.sportsnote.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.sportsnote.ui.LocalNavController
import com.example.sportsnote.ui.group.GroupViewScreen
import com.example.sportsnote.ui.note.AddTournamentNoteScreen
import com.example.sportsnote.ui.note.NoteScreen
import com.example.sportsnote.ui.target.TargetScreen
import com.example.sportsnote.ui.task.TaskScreen

@Composable
fun NavigationHost(paddingValues: PaddingValues) {
    val navController = LocalNavController.current
    NavHost(navController, startDestination = "task") {
        // 課題タブ
        composable("task") { TaskScreen() }
        composable(
            "group_view_screen/{groupId}",
            arguments = listOf(navArgument("groupId") { type = NavType.StringType })
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: ""
            GroupViewScreen(groupId = groupId, onBack = { navController.popBackStack() })
        }

        // ノートタブ
        composable("note") { NoteScreen() }
        composable("add_tournament_note") {
            AddTournamentNoteScreen(
                onDismiss = { navController.popBackStack() },
                isNavigation = true
            )
        }

        // 目標タブ
        composable("target") { TargetScreen() }
    }
}
