package com.example.sportsnote.ui.note

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.sportsnote.R
import com.example.sportsnote.ui.components.CustomSpacerColumn
import com.example.sportsnote.ui.components.MultiLineTextInputField
import com.example.sportsnote.ui.components.header.NavigationScreenHeader

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
    appBarRightIcon: MutableState<(@Composable () -> Unit)?>
) {
    val noteViewModel = NoteViewModel()
    val freeNote = noteViewModel.getNoteById(noteID)

    // 入力データ
    var title by remember { mutableStateOf(freeNote!!.title) }
    var detail by remember { mutableStateOf(freeNote!!.detail) }

    val inputFields: List<@Composable () -> Unit> = listOf(
        // タイトル
        {
            MultiLineTextInputField(
                title = stringResource(R.string.title),
                onTextChanged = { updatedText -> title = updatedText },
                initialText = title
            )
        },
        // 詳細
        {
            MultiLineTextInputField(
                title = stringResource(R.string.noteDetail),
                defaultLines = calcMaxLines(),
                onTextChanged = { updatedText -> detail = updatedText },
                initialText = detail
            )
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // ヘッダー
            NavigationScreenHeader(
                onDismiss = onBack,
                onSave = {
                    noteViewModel.saveFreeNote(
                        noteId = noteID,
                        title = title,
                        detail = detail
                    )
                },
                onDelete = { },
                updateAppBar = { navigationIcon, _ ->
                    appBarNavigationIcon.value = navigationIcon
                    appBarRightIcon.value = null
                }
            )

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