package com.it6210.sportsnote.ui.target

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
import com.it6210.sportsnote.R
import com.it6210.sportsnote.ui.components.header.AddScreenHeader
import com.it6210.sportsnote.ui.target.components.TargetFormContent
import com.it6210.sportsnote.viewModel.TargetViewModel
import kotlinx.coroutines.launch

/**
 * Target登録画面
 *
 * @param isYearlyTarget 年間目標か？
 * @param onDismiss 閉じる際の処理
 */
@Composable
fun AddTargetScreen(
    isYearlyTarget: Boolean,
    onDismiss: () -> Unit,
) {
    val targetViewModel = TargetViewModel()
    val coroutineScope = rememberCoroutineScope()
    var title by remember { mutableStateOf("") }
    var year by remember { mutableStateOf(2025) }
    var month by remember { mutableStateOf(1) }

    Dialog(
        onDismissRequest = onDismiss,
        properties =
            DialogProperties(
                usePlatformDefaultWidth = false,
            ),
    ) {
        val context = LocalContext.current

        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.surface),
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // ヘッダー
                AddScreenHeader(
                    title = stringResource(R.string.AddTarget),
                    onCancel = onDismiss,
                    onSave = {
                        // タイトル未入力の場合
                        if (title.isBlank()) {
                            coroutineScope.launch {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.ErrorTitleEmpty),
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                            return@AddScreenHeader
                        }
                        // 保存処理
                        targetViewModel.saveTarget(
                            title = title,
                            year = year,
                            month = month,
                            isYearlyTarget = isYearlyTarget,
                        )
                        onDismiss()
                    },
                    coroutineScope = coroutineScope,
                )

                // Target共通フォーム
                TargetFormContent(
                    isYearlyTarget = isYearlyTarget,
                    onTitleChange = { title = it },
                    onYearChange = { year = it },
                    onMonthChange = { month = it },
                )
            }
        }
    }
}
