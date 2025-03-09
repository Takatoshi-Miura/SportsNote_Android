package com.it6210.sportsnote.ui.target

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.it6210.sportsnote.R
import com.it6210.sportsnote.model.manager.PreferencesManager
import com.it6210.sportsnote.model.manager.SyncManager
import com.it6210.sportsnote.ui.LocalNavController
import com.it6210.sportsnote.ui.components.ActionBottomSheetContent
import com.it6210.sportsnote.ui.components.DialogType
import com.it6210.sportsnote.ui.components.items.CustomFloatingActionButton
import com.it6210.sportsnote.ui.note.NoteViewModel
import com.it6210.sportsnote.ui.target.components.CalendarDisplay
import com.it6210.sportsnote.ui.target.components.NoteListSection
import com.it6210.sportsnote.ui.target.components.TargetDisplaySection
import com.it6210.sportsnote.utils.NoteType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.YearMonth

/**
 * 目標画面
 *
 * @param reloadTrigger リロードトリガー
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TargetScreen(reloadTrigger: Int) {
    val targetViewModel = TargetViewModel()
    val noteViewModel = NoteViewModel()
    val targetNotes by noteViewModel.targetNotes.collectAsState()
    val yearlyTarget by targetViewModel.yearlyTarget.collectAsState()
    val monthlyTarget by targetViewModel.monthlyTarget.collectAsState()
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    val navController = LocalNavController.current
    var isDialogVisible by remember { mutableStateOf(false) }
    var dialogType by remember { mutableStateOf(DialogType.None) }
    val visibleMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf<java.time.LocalDate?>(null) }

    LaunchedEffect(visibleMonth, isDialogVisible, reloadTrigger) {
        coroutineScope.launch {
            if (PreferencesManager.get(PreferencesManager.Keys.IS_LOGIN, false)) {
                withContext(Dispatchers.IO) {
                    SyncManager.syncAllData()
                }
            }
            targetViewModel.getTargetByYearMonth(visibleMonth.year, visibleMonth.monthValue)
        }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            val actionItems =
                listOf(
                    stringResource(R.string.AddYearTarget) to {
                        isDialogVisible = true
                        dialogType = DialogType.AddYearTarget
                        coroutineScope.launch { sheetState.hide() }
                    },
                    stringResource(R.string.AddMonthTarget) to {
                        isDialogVisible = true
                        dialogType = DialogType.AddMonthTarget
                        coroutineScope.launch { sheetState.hide() }
                    },
                    stringResource(R.string.cancel) to {
                        coroutineScope.launch { sheetState.hide() }
                    },
                )
            ActionBottomSheetContent(items = actionItems)
        },
    ) {
        Box(
            modifier =
                Modifier
                    .background(MaterialTheme.colors.surface)
                    .fillMaxSize(),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize(),
            ) {
                // 年間目標と月間目標
                TargetDisplaySection(yearlyTarget, monthlyTarget)
                Spacer(modifier = Modifier.height(16.dp))

                // カレンダー
                CalendarDisplay(
                    modifier = Modifier.background(MaterialTheme.colors.background),
                    targetViewModel = targetViewModel,
                    selectedDate = selectedDate,
                    onDateSelected = { date ->
                        selectedDate = date
                        noteViewModel.getNoteListByDate(date)
                    },
                )
                Spacer(modifier = Modifier.height(16.dp))

                // ノートリスト
                NoteListSection(
                    notes = targetNotes,
                    onNoteClick = { note ->
                        when (NoteType.fromInt(note.noteType)) {
                            NoteType.FREE -> { }
                            NoteType.PRACTICE -> {
                                navController.navigate("practice_note_view/${note.noteID}")
                            }
                            NoteType.TOURNAMENT -> {
                                navController.navigate("tournament_note_view/${note.noteID}")
                            }
                        }
                    },
                )
            }

            // +ボタン
            CustomFloatingActionButton {
                coroutineScope.launch { sheetState.show() }
            }
        }
    }

    // ダイアログでフルスクリーンモーダルを表示
    if (!isDialogVisible) return
    if (dialogType == DialogType.AddYearTarget) {
        // 年間目標を追加
        AddTargetScreen(
            isYearlyTarget = true,
            onDismiss = { isDialogVisible = false },
        )
    } else if (dialogType == DialogType.AddMonthTarget) {
        // 月間目標を追加
        AddTargetScreen(
            isYearlyTarget = false,
            onDismiss = { isDialogVisible = false },
        )
    }
}
