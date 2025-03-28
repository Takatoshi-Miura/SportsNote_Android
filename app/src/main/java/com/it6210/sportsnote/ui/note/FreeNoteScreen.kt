package com.it6210.sportsnote.ui.note

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.it6210.sportsnote.R
import com.it6210.sportsnote.ui.components.header.NavigationScreenHeader
import com.it6210.sportsnote.ui.components.items.AutoSaveTimestamp
import com.it6210.sportsnote.ui.components.items.CustomSpacerColumn
import com.it6210.sportsnote.ui.components.items.MultiLineTextInputField
import com.it6210.sportsnote.ui.components.items.SingleLineTextInputField
import com.it6210.sportsnote.viewModel.NoteViewModel
import kotlinx.coroutines.delay
import java.util.Date

/**
 * フリーノート画面
 *
 * @param noteID noteID
 * @param onBack 前画面に戻る処理
 * @param appBarNavigationIcon TopBar左のボタンを変更するために必要
 * @param appBarRightIcon TopBar右のボタンを変更するために必要
 */
@Composable
fun FreeNoteScreen(
    noteID: String,
    onBack: () -> Unit,
    appBarNavigationIcon: MutableState<(@Composable () -> Unit)?>,
    appBarRightIcon: MutableState<(@Composable () -> Unit)?>,
) {
    val noteViewModel = NoteViewModel()
    val freeNote = noteViewModel.getNoteById(noteID)
    val context = LocalContext.current

    // 入力データ
    var title by remember { mutableStateOf(freeNote?.title ?: "") }
    var detail by remember { mutableStateOf(freeNote?.detail ?: "") }
    var lastSavedAt by remember { mutableStateOf(freeNote?.updated_at ?: Date()) }

    val inputFields: List<@Composable () -> Unit> =
        listOf(
            // タイトル
            {
                SingleLineTextInputField(
                    title = stringResource(R.string.title),
                    onTextChanged = { updatedText -> title = updatedText },
                    initialText = title,
                )
            },
            // 詳細
            {
                MultiLineTextInputField(
                    title = stringResource(R.string.noteDetail),
                    defaultLines = calcMaxLines(),
                    onTextChanged = { updatedText -> detail = updatedText },
                    initialText = detail,
                )
            },
        )

    // 自動保存処理
    LaunchedEffect(title, detail) {
        delay(1000)
        if (title.isNotEmpty()) {
            val now = Date()
            noteViewModel.saveFreeNote(
                noteId = noteID,
                title = title,
                detail = detail,
                created_at = freeNote?.created_at ?: now,
            )
            lastSavedAt = now
        }
    }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.surface),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // ヘッダー
            NavigationScreenHeader(
                onDismiss = onBack,
                onSave = {
                    if (title.isEmpty()) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.emptyTitle),
                            Toast.LENGTH_LONG,
                        ).show()
                        return@NavigationScreenHeader
                    }
                    noteViewModel.saveFreeNote(
                        noteId = noteID,
                        title = title,
                        detail = detail,
                        created_at = freeNote!!.created_at,
                    )
                },
                onDelete = { },
                onEdit = null,
                updateAppBar = { navigationIcon, _ ->
                    appBarNavigationIcon.value = navigationIcon
                    appBarRightIcon.value = null
                },
            )

            AutoSaveTimestamp(lastSavedAt)

            // 共通フォーム
            CustomSpacerColumn(items = inputFields)
        }
    }
}

/**
 * 画面高さ一杯の行数を取得
 *
 * @return 行数
 */
@Composable
private fun calcMaxLines(): Int {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val lineHeight = 24.dp // 1行の高さ
    val padding = 16.dp // 余白
    val maxLines = ((screenHeight - padding) / lineHeight).toInt()
    return maxLines
}
