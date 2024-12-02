package com.example.sportsnote.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Divider
import androidx.compose.material.DrawerState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun DrawerContent(drawerState: DrawerState) {
    Column {
        Text("Menu Item 1")
        Divider()
        Text("Menu Item 2")
        Divider()
    }
}
