@file:Suppress("NAME_SHADOWING")

package com.example.sportsnote.ui.target

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.sportsnote.R
import com.example.sportsnote.model.NoteListItem
import com.example.sportsnote.model.Target
import com.example.sportsnote.ui.LocalNavController
import com.example.sportsnote.ui.components.ActionBottomSheetContent
import com.example.sportsnote.ui.components.CalendarDisplay
import com.example.sportsnote.ui.components.CustomFloatingActionButton
import com.example.sportsnote.ui.components.DialogType
import com.example.sportsnote.ui.note.NoteEmptyItem
import com.example.sportsnote.ui.note.NoteListItem
import com.example.sportsnote.ui.note.NoteViewModel
import com.example.sportsnote.utils.NoteType
import kotlinx.coroutines.launch
import java.time.YearMonth

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TargetScreen() {
    val targetViewModel = TargetViewModel()
    val noteViewModel = NoteViewModel()
    val targetNotes by noteViewModel.targetNotes.collectAsState()
    val yearlyTarget by targetViewModel.yearlyTarget.collectAsState()
    val monthlyTarget by targetViewModel.monthlyTarget.collectAsState()
    val systemGray6 = Color(0xFFF2F2F7)
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    val navController = LocalNavController.current
    var isDialogVisible by remember { mutableStateOf(false) }
    var dialogType by remember { mutableStateOf(DialogType.None) }
    val visibleMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf<java.time.LocalDate?>(null) }

    LaunchedEffect(visibleMonth, isDialogVisible) {
        targetViewModel.getTargetByYearMonth(visibleMonth.year, visibleMonth.monthValue)
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            val actionItems = listOf(
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
                }
            )
            ActionBottomSheetContent(items = actionItems)
        }
    ) {
        Box(
            modifier = Modifier
                .background(systemGray6)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // 年間目標と月間目標
                TargetDisplaySection(yearlyTarget, monthlyTarget)

                Spacer(modifier = Modifier.height(16.dp))

                // カレンダー
                CalendarDisplay(
                    modifier = Modifier.background(Color.White),
                    targetViewModel = targetViewModel,
                    selectedDate = selectedDate,
                    onDateSelected = { date ->
                        selectedDate = date
                        noteViewModel.getNoteListByDate(date)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ノートリスト
                NoteListSection(
                    notes = targetNotes,
                    onNoteClick = { note ->
                        when(NoteType.fromInt(note.noteType)) {
                            NoteType.FREE -> { }
                            NoteType.PRACTICE -> {
                                navController.navigate("practice_note_view/${note.noteID}")
                            }
                            NoteType.TOURNAMENT -> {
                                navController.navigate("tournament_note_view/${note.noteID}")
                            }
                        }
                    }
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
            onDismiss = { isDialogVisible = false }
        )
    } else if (dialogType == DialogType.AddMonthTarget) {
        // 月間目標を追加
        AddTargetScreen(
            isYearlyTarget = false,
            onDismiss = { isDialogVisible = false }
        )
    }
}

/**
 * 年間目標と月間目標を表示するコンポーネント
 *
 * @param yearlyTarget 年間目標
 * @param monthlyTarget 月間目標
 */
@Composable
fun TargetDisplaySection(
    yearlyTarget: Target?,
    monthlyTarget: Target?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp)
    ) {
        Column {
            // 年間目標
            yearlyTarget?.let {
                TargetLabel(stringResource(R.string.targetYear, it.title))
            } ?: TargetLabel(stringResource(R.string.targetYear, stringResource(R.string.targetNotFound)))

            // 月間目標
            monthlyTarget?.let {
                TargetLabel(stringResource(R.string.targetMonth, it.title))
            } ?: TargetLabel(stringResource(R.string.targetMonth, stringResource(R.string.targetNotFound)))
        }
    }
}

/**
 * 目標表示ラベル
 *
 * @param text 目標文字列
 */
@Composable
fun TargetLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.body1,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    )
}

/**
 * ノートリストを縦にスクロール表示するコンポーネント
 *
 * @param notes ノートリスト
 * @param onNoteClick ノート押下時の処理
 */
@Composable
fun NoteListSection(
    notes: List<NoteListItem>,
    onNoteClick: (NoteListItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp)
    ) {
        if (notes.isEmpty()) {
            item {
                NoteEmptyItem()
            }
        }
        items(notes) { note ->
            NoteListItem(
                note = note,
                onClick = { onNoteClick(note) }
            )
            Divider()
        }
    }
}