package com.example.sportsnote.ui.group

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GroupViewScreen(
    viewModel: GroupViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    groupId: String,
    onBack: () -> Unit
) {
    AddGroupContent(
        viewModel = viewModel,
        onDismiss = { onBack() },
    )
}