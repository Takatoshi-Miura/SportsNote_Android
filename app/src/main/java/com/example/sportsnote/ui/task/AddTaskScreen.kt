package com.example.sportsnote.ui.task

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.sportsnote.R
import com.example.sportsnote.ui.components.header.AddScreenHeader
import com.example.sportsnote.ui.measures.MeasuresViewModel
import com.example.sportsnote.ui.task.components.AddTaskFormContent

/**
 * 課題追加画面
 *
 * @param onDismiss 画面閉じる時の処理
 */
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AddTaskScreen(onDismiss: () -> Unit) {
    val viewModel = TaskViewModel()
    val measuresViewModel = MeasuresViewModel()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var cause by remember { mutableStateOf("") }
    var measures by remember { mutableStateOf("") }
    var groupId by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismiss,
        properties =
            DialogProperties(
                usePlatformDefaultWidth = false,
            ),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.surface),
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // ヘッダー
                AddScreenHeader(
                    title = stringResource(R.string.addTask),
                    onCancel = onDismiss,
                    onSave = {
                        if (title.isEmpty()) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.emptyTitle),
                                Toast.LENGTH_LONG,
                            ).show()
                            return@AddScreenHeader
                        }
                        // 課題を保存
                        val taskId =
                            viewModel.saveTask(
                                title = title,
                                cause = cause,
                                groupId = groupId,
                            )
                        // 対策を保存
                        if (measures.isNotBlank()) {
                            measuresViewModel.saveMeasures(
                                taskId = taskId,
                                title = measures,
                            )
                        }
                        onDismiss()
                    },
                    coroutineScope = coroutineScope,
                )

                // フォーム
                AddTaskFormContent(
                    onTitleChange = { title = it },
                    onCauseChange = { cause = it },
                    onMeasuresChange = { measures = it },
                    onGroupChange = { groupId = it.groupID },
                )
            }
        }
    }
}
