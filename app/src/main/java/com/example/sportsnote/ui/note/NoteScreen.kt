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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sportsnote.R
import com.example.sportsnote.model.NoteListItem
import com.example.sportsnote.model.PreferencesManager
import com.example.sportsnote.model.SyncManager
import com.example.sportsnote.ui.LocalNavController
import com.example.sportsnote.ui.components.ActionBottomSheetContent
import com.example.sportsnote.ui.components.AdMobBanner
import com.example.sportsnote.ui.components.CustomFloatingActionButton
import com.example.sportsnote.ui.components.DialogType
import com.example.sportsnote.utils.NoteType
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ノート一覧画面
 *
 * @param reloadTrigger リロードトリガー
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NoteScreen(reloadTrigger: Int) {
    val noteViewModel = NoteViewModel()
    val notes by noteViewModel.noteListItems.collectAsState()
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    val navController = LocalNavController.current
    val isLoading by noteViewModel.isLoading.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)
    var isDialogVisible by remember { mutableStateOf(false) }
    var dialogType by remember { mutableStateOf(DialogType.None) }
    var searchQuery by remember { mutableStateOf("") }

    // 一覧のリフレッシュ処理
    val onRefresh = {
        coroutineScope.launch {
            if (PreferencesManager.get(PreferencesManager.Keys.IS_LOGIN, false)) {
                withContext(Dispatchers.IO) {
                    SyncManager.syncAllData()
                }
            }
            noteViewModel.searchNotesByQuery(searchQuery)
        }
    }

    LaunchedEffect(Unit, reloadTrigger) {
        onRefresh()
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            val actionItems =
                listOf(
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
                    },
                )
            ActionBottomSheetContent(items = actionItems)
        },
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.surface),
        ) {
            Column {
                // 検索バー
                SearchBar(
                    query = searchQuery,
                    onQueryChanged = {
                        searchQuery = it
                        onRefresh()
                    },
                )
                SwipeRefresh(
                    state = swipeRefreshState,
                    onRefresh = { onRefresh() },
                ) {
                    // ノート一覧
                    NoteListScreen(
                        notes = notes,
                        onNoteClick = { note ->
                            when (NoteType.fromInt(note.noteType)) {
                                NoteType.FREE -> {
                                    navController.navigate("free_note_view/${note.noteID}")
                                }
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
            onDismiss = {
                isDialogVisible = false
                onRefresh()
            },
        )
    } else if (dialogType == DialogType.AddTournamentNote) {
        AddTournamentNoteScreen(
            onDismiss = {
                isDialogVisible = false
                onRefresh()
            },
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
    onQueryChanged: (String) -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(MaterialTheme.colors.background, shape = MaterialTheme.shapes.medium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // 虫眼鏡アイコン
        Icon(
            painter = painterResource(id = R.drawable.search_24px),
            contentDescription = null,
            modifier =
                Modifier
                    .padding(start = 4.dp)
                    .size(24.dp),
            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
        )

        // テキスト入力フィールド
        TextField(
            value = query,
            onValueChange = onQueryChanged,
            modifier = Modifier.weight(1f),
            placeholder = {
                Text(text = stringResource(R.string.searchNote))
            },
            colors =
                TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
            singleLine = true,
        )
    }
}

@Composable
fun NoteListScreen(
    notes: List<NoteListItem>,
    onNoteClick: (NoteListItem) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.background),
        ) {
            items(notes) { note ->
                NoteListItem(
                    note = note,
                    onClick = { onNoteClick(note) },
                )
                Divider()
            }
        }
        // バナー広告
        AdMobBanner()
    }
}

@Composable
fun NoteListItem(
    note: NoteListItem,
    onClick: () -> Unit = {},
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .height(56.dp),
    ) {
        if (NoteType.fromInt(note.noteType) == NoteType.FREE) {
            // ピン留めアイコン
            Box(
                modifier =
                    Modifier
                        .width(24.dp)
                        .fillMaxHeight(),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.keep_24dp),
                    contentDescription = "Item Icon",
                    modifier =
                        Modifier
                            .size(24.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground),
                )
            }
        } else {
            // 背景色だけの部品
            Box(
                modifier =
                    Modifier
                        .fillMaxHeight()
                        .width(24.dp)
                        .background(note.backGroundColor),
            )
        }

        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically),
        ) {
            // 情報表示
            Text(
                text = note.title,
                fontSize = 16.sp,
                style = MaterialTheme.typography.body1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = note.subTitle,
                fontSize = 12.sp,
                style = MaterialTheme.typography.body2,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
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
        modifier =
            Modifier
                .fillMaxWidth()
                .height(56.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically),
        ) {
            // 情報表示
            Text(
                text = stringResource(R.string.emptyNote),
                fontSize = 16.sp,
                style = MaterialTheme.typography.body1,
            )
        }
    }
}
