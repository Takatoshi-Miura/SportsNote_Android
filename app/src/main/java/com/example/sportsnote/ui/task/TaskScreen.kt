package com.example.sportsnote.ui.task

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sportsnote.ui.components.LazySectionedColumn

/**
 * 課題一覧画面
 */
@Composable
fun TaskScreen(taskViewModel: TaskViewModel = viewModel()) {
    val sections by taskViewModel.sections.collectAsState()
    LazySectionedColumn(sections = sections)
}

@Preview(showBackground = true)
@Composable
fun PreviewLazySectionedColumn() {
    TaskScreen()
}