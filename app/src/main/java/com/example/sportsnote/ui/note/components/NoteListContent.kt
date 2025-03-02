package com.example.sportsnote.ui.note.components

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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import com.example.sportsnote.ui.components.AdMobBanner
import com.example.sportsnote.utils.NoteType

/**
 * ノート一覧のノートリストUI
 *
 * @param notes ノートリスト
 * @param onNoteClick ノート押下時の処理
 */
@Composable
fun NoteListContent(
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
                NoteItem(
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

/**
 * ノート一覧のノートセル
 *
 * @param note ノートデータ
 * @param onClick 押下時の処理
 */
@Composable
fun NoteItem(
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
