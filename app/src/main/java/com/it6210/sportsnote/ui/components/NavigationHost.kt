package com.it6210.sportsnote.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.DrawerState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.it6210.sportsnote.ui.LocalNavController
import com.it6210.sportsnote.ui.Screen
import com.it6210.sportsnote.ui.components.header.CustomTopAppBar
import com.it6210.sportsnote.ui.group.GroupViewScreen
import com.it6210.sportsnote.ui.measures.MeasuresScreen
import com.it6210.sportsnote.ui.note.FreeNoteScreen
import com.it6210.sportsnote.ui.note.NoteScreen
import com.it6210.sportsnote.ui.note.PracticeNoteViewScreen
import com.it6210.sportsnote.ui.note.TournamentNoteViewScreen
import com.it6210.sportsnote.ui.setting.SettingScreen
import com.it6210.sportsnote.ui.target.TargetScreen
import com.it6210.sportsnote.ui.task.CompletedTaskScreen
import com.it6210.sportsnote.ui.task.TaskDetailScreen
import com.it6210.sportsnote.ui.task.TaskScreen
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

    // リロードのトリガー(ログイン・ログアウト後など)
    var reloadTrigger by remember { mutableStateOf(0) }

    // Drawerの開閉状態を監視し、閉じたら再描画
    LaunchedEffect(scaffoldState.drawerState.isClosed) {
        if (scaffoldState.drawerState.isClosed) {
            reloadTrigger++
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        contentWindowInsets = WindowInsets.systemBars,
        topBar = {
            CustomTopAppBar(
                title = screenConfig.topBarTitle,
                navigationIcon = appBarNavigationIcon.value,
                rightIcon = appBarRightIcon.value,
            )
        },
        bottomBar = {
            if (screenConfig.showBottomBar) {
                BottomNavigationBar()
            }
        },
        drawerContent = {
            // 設定画面を描画
            SettingScreen(
                onDismiss = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.close()
                    }
                },
            )
        },
    ) { paddingValues ->
        NavHost(
            navController,
            startDestination = Screen.Task.route,
            modifier = Modifier.padding(paddingValues),
        ) {
            // 課題タブTOP
            composable(Screen.Task.route) {
                appBarNavigationIcon.value = {
                    DrawerToggleButton(coroutineScope, drawerState)
                }
                appBarRightIcon.value = null
                TaskScreen(reloadTrigger)
            }
            // Group詳細
            composable(Screen.GroupView.route) { backStackEntry ->
                val groupId = backStackEntry.arguments?.getString("groupId") ?: ""
                GroupViewScreen(
                    groupId = groupId,
                    onBack = { navController.popBackStack() },
                    appBarNavigationIcon = appBarNavigationIcon,
                    appBarRightIcon = appBarRightIcon,
                )
            }
            // Task詳細
            composable(Screen.TaskDetail.route) { backStackEntry ->
                val taskId = backStackEntry.arguments?.getString("taskId") ?: ""
                TaskDetailScreen(
                    taskId = taskId,
                    onBack = { navController.popBackStack() },
                    appBarNavigationIcon = appBarNavigationIcon,
                    appBarRightIcon = appBarRightIcon,
                )
            }
            // Measures詳細
            composable(Screen.Measures.route) { backStackEntry ->
                val measuresId = backStackEntry.arguments?.getString("measuresId") ?: ""
                MeasuresScreen(
                    measuresID = measuresId,
                    onBack = { navController.popBackStack() },
                    appBarNavigationIcon = appBarNavigationIcon,
                    appBarRightIcon = appBarRightIcon,
                )
            }
            // 完了した課題一覧
            composable(Screen.CompletedTask.route) { backStackEntry ->
                appBarRightIcon.value = null
                val groupId = backStackEntry.arguments?.getString("groupId") ?: ""
                CompletedTaskScreen(
                    groupId = groupId,
                    onBack = { navController.popBackStack() },
                    appBarNavigationIcon = appBarNavigationIcon,
                )
            }
            // ノートタブTOP
            composable(Screen.Note.route) {
                appBarNavigationIcon.value = {
                    DrawerToggleButton(coroutineScope, drawerState)
                }
                appBarRightIcon.value = null
                NoteScreen(reloadTrigger)
            }
            // 大会ノート詳細
            composable(Screen.TournamentNoteView.route) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getString("noteId") ?: ""
                TournamentNoteViewScreen(
                    noteId = noteId,
                    onBack = { navController.popBackStack() },
                    appBarNavigationIcon = appBarNavigationIcon,
                    appBarRightIcon = appBarRightIcon,
                )
            }
            // 練習ノート詳細
            composable(Screen.PracticeNoteView.route) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getString("noteId") ?: ""
                PracticeNoteViewScreen(
                    noteId = noteId,
                    onBack = { navController.popBackStack() },
                    appBarNavigationIcon = appBarNavigationIcon,
                    appBarRightIcon = appBarRightIcon,
                )
            }
            // フリーノート
            composable(Screen.FreeNoteView.route) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getString("noteId") ?: ""
                FreeNoteScreen(
                    noteID = noteId,
                    onBack = { navController.popBackStack() },
                    appBarNavigationIcon = appBarNavigationIcon,
                    appBarRightIcon = appBarRightIcon,
                )
            }
            // 目標タブTOP
            composable(Screen.Target.route) {
                appBarNavigationIcon.value = {
                    DrawerToggleButton(coroutineScope, drawerState)
                }
                appBarRightIcon.value = null
                TargetScreen(reloadTrigger)
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
    drawerState: DrawerState,
) {
    IconButton(onClick = {
        coroutineScope.launch {
            if (drawerState.isClosed) {
                drawerState.open()
            } else {
                drawerState.close()
            }
        }
    }) {
        Icon(Icons.Filled.Menu, contentDescription = "Menu")
    }
}
