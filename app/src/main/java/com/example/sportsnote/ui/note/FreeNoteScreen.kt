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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.sportsnote.R
import com.example.sportsnote.ui.LocalNavController
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
    val coroutineScope = rememberCoroutineScope()
    val navController = LocalNavController.current

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
                defaultLines = 3,
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
                    // TODO: 保存処理
                },
                onDelete = { },
                updateAppBar = { navigationIcon, rightIcon ->
                    appBarNavigationIcon.value = navigationIcon
                    appBarRightIcon.value = null
                }
            )

            // 共通フォーム
            CustomSpacerColumn(items = inputFields)
        }
    }
}