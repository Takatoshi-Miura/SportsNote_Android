package com.example.sportsnote.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.DrawerState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.sportsnote.ui.LocalNavController
import com.example.sportsnote.ui.Screen
import com.example.sportsnote.ui.group.GroupViewScreen
import com.example.sportsnote.ui.measures.MeasuresScreen
import com.example.sportsnote.ui.note.FreeNoteScreen
import com.example.sportsnote.ui.note.NoteScreen
import com.example.sportsnote.ui.note.TournamentNoteViewScreen
import com.example.sportsnote.ui.target.TargetScreen
import com.example.sportsnote.ui.task.CompletedTaskScreen
import com.example.sportsnote.ui.task.TaskDetailScreen
import com.example.sportsnote.ui.task.TaskScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavigationHost() {
    val navController = LocalNavController.current
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""
    val scaffoldState = rememberScaffoldState()
    val drawerState = scaffoldState.drawerState
    val coroutineScope = rememberCoroutineScope()

    // CustomTopAppBar の設定を管理する状態
    val appBarNavigationIcon = remember { mutableStateOf<(@Composable () -> Unit)?>(null) }
    val appBarRightIcon = remember { mutableStateOf<(@Composable () -> Unit)?>(null) }

    // 画面設定を動的に取得
    val screen = Screen.fromRoute(currentRoute)
    val screenConfig = screen.getConfig()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CustomTopAppBar(
                title = screenConfig.topBarTitle,
                navigationIcon = appBarNavigationIcon.value,
                rightIcon = appBarRightIcon.value
            )
        },
        bottomBar = {
            if (screenConfig.showBottomBar) {
                BottomNavigationBar()
            }
        },
        drawerContent = { DrawerContent() }
    ) { paddingValues ->
        NavHost(
            navController,
            startDestination = Screen.Task.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            // 課題タブTOP
            composable(Screen.Task.route) {
                appBarNavigationIcon.value = {
                    DrawerToggleButton(coroutineScope, drawerState)
                }
                appBarRightIcon.value = null
                TaskScreen()
            }
            // Group詳細
            composable(Screen.GroupView.route) { backStackEntry ->
                val groupId = backStackEntry.arguments?.getString("groupId") ?: ""
                GroupViewScreen(
                    groupId = groupId,
                    onBack = { navController.popBackStack() },
                    appBarNavigationIcon = appBarNavigationIcon,
                    appBarRightIcon = appBarRightIcon
                )
            }
            // Task詳細
            composable(Screen.TaskDetail.route) { backStackEntry ->
                val taskId = backStackEntry.arguments?.getString("taskId") ?: ""
                TaskDetailScreen(
                    taskId = taskId,
                    onBack = { navController.popBackStack() },
                    appBarNavigationIcon = appBarNavigationIcon,
                    appBarRightIcon = appBarRightIcon
                )
            }
            // Measures詳細
            composable(Screen.Measures.route) { backStackEntry ->
                val measuresId = backStackEntry.arguments?.getString("measuresId") ?: ""
                MeasuresScreen(
                    measuresID =  measuresId,
                    onBack = { navController.popBackStack() },
                    appBarNavigationIcon = appBarNavigationIcon,
                    appBarRightIcon = appBarRightIcon
                )
            }
            // 完了した課題一覧
            composable(Screen.CompletedTask.route) { backStackEntry ->
                val groupId = backStackEntry.arguments?.getString("groupId") ?: ""
                CompletedTaskScreen(
                    groupId = groupId,
                    onBack = { navController.popBackStack() },
                    appBarNavigationIcon = appBarNavigationIcon
                )
            }
            // ノートタブTOP
            composable(Screen.Note.route) {
                appBarNavigationIcon.value = {
                    DrawerToggleButton(coroutineScope, drawerState)
                }
                appBarRightIcon.value = null
                NoteScreen()
            }
            // 大会ノート詳細
            composable(Screen.TournamentNoteView.route) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getString("noteId") ?: ""
                TournamentNoteViewScreen(
                    noteId = noteId,
                    onBack = { navController.popBackStack() },
                    appBarNavigationIcon = appBarNavigationIcon,
                    appBarRightIcon = appBarRightIcon
                )
            }
            // フリーノート
            composable(Screen.FreeNoteView.route) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getString("noteId") ?: ""
                FreeNoteScreen(
                    noteID = noteId,
                    onBack = { navController.popBackStack() },
                    appBarNavigationIcon = appBarNavigationIcon,
                    appBarRightIcon = appBarRightIcon
                )
            }
            // 目標タブTOP
            composable(Screen.Target.route) {
                appBarNavigationIcon.value = {
                    DrawerToggleButton(coroutineScope, drawerState)
                }
                appBarRightIcon.value = null
                TargetScreen()
            }
        }
    }
}

/**
 * ハンバーガーメニューボタン
 *
 * @param coroutineScope
 * @param drawerState
 */
@Composable
fun DrawerToggleButton(
    coroutineScope: CoroutineScope,
    drawerState: DrawerState
) {
    IconButton(onClick = {
        coroutineScope.launch {
            if (drawerState.isClosed) drawerState.open()
            else drawerState.close()
        }
    }) {
        Icon(Icons.Filled.Menu, contentDescription = "Menu")
    }
}
