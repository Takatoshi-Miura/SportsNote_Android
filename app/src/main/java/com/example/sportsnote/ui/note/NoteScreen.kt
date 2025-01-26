package com.example.sportsnote.ui.note

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sportsnote.R
import com.example.sportsnote.model.Note
import com.example.sportsnote.ui.LocalNavController
import com.example.sportsnote.ui.components.ActionBottomSheetContent
import com.example.sportsnote.ui.components.CustomFloatingActionButton
import com.example.sportsnote.ui.components.DialogType
import com.example.sportsnote.utils.NoteType
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * ノート一覧画面
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NoteScreen(noteViewModel: NoteViewModel = viewModel()) {
    val notes by noteViewModel.notes.collectAsState()
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    val navController = LocalNavController.current
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
    var isDialogVisible by remember { mutableStateOf(false) }
    var dialogType by remember { mutableStateOf(DialogType.None) }
    var searchQuery by remember { mutableStateOf("") }
    val systemGray6 = Color(0xFFF2F2F7)

    LaunchedEffect(notes) {
        noteViewModel.loadNotes()
    }

    // ノート一覧のリフレッシュ処理
    val onRefresh = {
        noteViewModel.loadNotes()
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            val actionItems = listOf(
                stringResource(R.string.addPracticeNoteAction) to {
                    isDialogVisible = true
                    dialogType = DialogType.AddPracticeNote
                    coroutineScope.launch { sheetState.hide() }
                },
                stringResource(R.string.addTournamentNoteAction) to {
                    isDialogVisible = true
                    dialogType = DialogType.AddTournamentNote
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
                .fillMaxSize()
                .background(systemGray6)
        ) {
            Column {
                // 検索バー
                SearchBar(
                    query = searchQuery,
                    onQueryChanged = {
                        searchQuery = it
                        noteViewModel.searchNotesByQuery(searchQuery)
                    }
                )
                SwipeRefresh(
                    state = swipeRefreshState,
                    onRefresh = onRefresh
                ) {
                    // ノート一覧
                    NoteListScreen(
                        notes = notes,
                        onNoteClick = { note ->
                            when (NoteType.fromInt(note.noteType)) {
                                NoteType.FREE -> {
                                    navController.navigate("free_note_view/${note.noteID}")
                                }
                                NoteType.PRACTICE -> {}
                                NoteType.TOURNAMENT -> {
                                    navController.navigate("tournament_note_view/${note.noteID}")
                                }
                            }
                        }
                    )
                }
            }

            // +ボタン
            CustomFloatingActionButton {
                coroutineScope.launch { sheetState.show() }
            }
        }
    }

    // ダイアログでフルスクリーンモーダルを表示
    if (!isDialogVisible) return
    if (dialogType == DialogType.AddPracticeNote) {
        AddPracticeNoteScreen(
            onDismiss = { isDialogVisible = false }
        )
    } else if (dialogType == DialogType.AddTournamentNote) {
        AddTournamentNoteScreen(
            onDismiss = { isDialogVisible = false }
        )
    }
}

/**
 * 検索バーのコンポーネント
 *
 * @param query 検索文字列
 * @param onQueryChanged 文字列変化時の処理
 */
@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colors.surface, shape = MaterialTheme.shapes.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 虫眼鏡アイコン
        Icon(
            painter = painterResource(id = R.drawable.search_24px),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 4.dp)
                .size(24.dp),
            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
        )

        // テキスト入力フィールド
        TextField(
            value = query,
            onValueChange = onQueryChanged,
            modifier = Modifier.weight(1f),
            placeholder = {
                Text(text = stringResource(R.string.searchNote))
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            singleLine = true
        )
    }
}

@Composable
fun NoteListScreen(
    notes: List<Note>,
    onNoteClick: (Note) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        items(notes) { note ->
            NoteListItem(
                note = note,
                onClick = { onNoteClick(note) }
            )
            Divider()
        }
    }
}

@Composable
fun NoteListItem(
    note: Note,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .height(56.dp)
    ) {
        // 左端部分
        if (NoteType.fromInt(note.noteType) == NoteType.FREE) {
            Box(
                modifier = Modifier
                    .width(24.dp)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.keep_24dp),
                    contentDescription = "Item Icon",
                    modifier = Modifier
                        .size(24.dp)
                )
            }
        } else {
            val backgroundColor = when (NoteType.fromInt(note.noteType)) {
                NoteType.FREE -> Color.White
                NoteType.PRACTICE -> Color.White // TODO: 含まれる課題のグループ色にする
                NoteType.TOURNAMENT -> Color.White
            }
            // 背景色だけの部品
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(24.dp)
                    .background(backgroundColor)
            )
        }

        // メインテキスト表示
        val displayText = when (NoteType.fromInt(note.noteType)) {
            NoteType.FREE -> note.title
            NoteType.PRACTICE -> note.detail
            NoteType.TOURNAMENT -> note.result
        }

        // サブテキスト表示
        val displaySubText = when (NoteType.fromInt(note.noteType)) {
            NoteType.FREE -> note.detail
            NoteType.PRACTICE -> formatDate(note.date)
            NoteType.TOURNAMENT -> formatDate(note.date)
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically)
        ) {
            // 情報表示
            Text(
                text = displayText,
                fontSize = 16.sp,
                style = MaterialTheme.typography.body1
            )
            // ノートの日付
            Text(
                text = displaySubText,
                fontSize = 12.sp,
                style = MaterialTheme.typography.body2,
                color = Color.Gray
            )
        }
    }
}

/**
 * 「ノートがありません。」と表示するコンポーネント
 */
@Composable
fun NoteEmptyItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically)
        ) {
            // 情報表示
            Text(
                text = stringResource(R.string.emptyNote),
                fontSize = 16.sp,
                style = MaterialTheme.typography.body1
            )
        }
    }
}

/**
 * 日付をyyyy/MM/dd (曜日)形式でフォーマットする
 */
fun formatDate(date: Date): String {
    val format = SimpleDateFormat("yyyy/MM/dd (E)", Locale.getDefault())
    return format.format(date)
}
