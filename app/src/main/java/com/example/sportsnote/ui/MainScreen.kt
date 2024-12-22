package com.example.sportsnote.ui

import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.sportsnote.R
import com.example.sportsnote.ui.components.BottomNavigationBar
import com.example.sportsnote.ui.components.CustomTopAppBar
import com.example.sportsnote.ui.components.DrawerContent
import com.example.sportsnote.ui.components.NavigationHost
import kotlinx.coroutines.launch

// CompositionLocalでNavControllerを共有
val LocalNavController = compositionLocalOf<NavHostController> {
    error("No NavController provided")
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val drawerState = scaffoldState.drawerState
    val coroutineScope = rememberCoroutineScope()
    val taskTitle = stringResource(R.string.task)
    val topBarTitle = remember { mutableStateOf(taskTitle) }

    CompositionLocalProvider(LocalNavController provides navController) {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                CustomTopAppBar(
                    onNavigationIconClick = {
                        coroutineScope.launch {
                            if (drawerState.isClosed) drawerState.open()
                            else drawerState.close()
                        }
                    },
                    title = topBarTitle.value
                )
            },
            bottomBar = {
                BottomNavigationBar(
                    onTitleChange = { newTitle -> topBarTitle.value = newTitle }
                )
            },
            drawerContent = { DrawerContent() }
        ) { paddingValues ->
            NavigationHost(paddingValues)
        }
    }
}
