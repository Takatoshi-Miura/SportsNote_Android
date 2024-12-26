package com.example.sportsnote.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.sportsnote.ui.LocalNavController
import com.example.sportsnote.ui.Screen
import com.example.sportsnote.ui.group.GroupViewScreen
import com.example.sportsnote.ui.note.AddTournamentNoteScreen
import com.example.sportsnote.ui.note.NoteScreen
import com.example.sportsnote.ui.target.TargetScreen
import com.example.sportsnote.ui.task.TaskScreen
import kotlinx.coroutines.launch

@Composable
fun NavigationHost() {
    val navController = LocalNavController.current
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""
    val scaffoldState = rememberScaffoldState()
    val drawerState = scaffoldState.drawerState
    val coroutineScope = rememberCoroutineScope()

    // 画面設定を動的に取得
    val screen = Screen.fromRoute(currentRoute)
    val screenConfig = screen.getConfig()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CustomTopAppBar(
                title = screenConfig.topBarTitle,
                navigationIcon = {
                    // TOPの場合はメニューを表示
                    if (screenConfig.showBottomBar) {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                if (drawerState.isClosed) drawerState.open()
                                else drawerState.close()
                            }
                        }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    } else {
                        // デフォルトは前の画面に戻る
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
            )
        },
        bottomBar = {
            if (screenConfig.showBottomBar) {
                BottomNavigationBar()
            }
        },
        drawerContent = { DrawerContent() }
    ) { paddingValues ->
        // 各画面の遷移処理
        NavHost(
            navController,
            startDestination = Screen.Task.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Task.route) { TaskScreen() }
            composable(Screen.GroupView.route) { backStackEntry ->
                val groupId = backStackEntry.arguments?.getString("groupId") ?: ""
                GroupViewScreen(groupId = groupId, onBack = { navController.popBackStack() })
            }
            composable(Screen.Note.route) { NoteScreen() }
            composable(Screen.AddTournamentNote.route) {
                AddTournamentNoteScreen(
                    onDismiss = { navController.popBackStack() },
                    isNavigation = true
                )
            }
            composable(Screen.Target.route) { TargetScreen() }
        }
    }
}
