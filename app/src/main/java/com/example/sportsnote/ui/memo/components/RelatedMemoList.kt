package com.example.sportsnote.ui.memo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.sportsnote.R
import com.example.sportsnote.model.MeasuresMemo
import com.example.sportsnote.ui.LocalNavController
import com.example.sportsnote.ui.components.items.ItemLabel
import com.example.sportsnote.ui.memo.MemoViewModel
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * 対策に関連するメモを一覧表示するコンポーネント（対策画面用）
 *
 * @param measuresID 対策ID
 */
@Composable
fun RelatedMemoList(measuresID: String) {
    val memoViewModel = MemoViewModel()
    val memos = memoViewModel.getMemosByMeasuresID(measuresID)
    val navController = LocalNavController.current

    // 項目名
    ItemLabel(title = stringResource(R.string.note))
    Divider()

    // 関連しているメモリスト
    LazyColumn {
        items(memos.size) { index ->
            val memo = memos[index]
            MemoItem(
                memo = memo,
                onClick = {
                    navController.navigate("practice_note_view/${memo.noteID}")
                },
            )
            Divider()
        }
    }
}

/**
 * 対策に関連するメモを表示するアイテム
 *
 * @param memo MeasuresMemo
 * @param onClick メモをタップしたときの処理
 */
@Composable
fun MemoItem(
    memo: MeasuresMemo,
    onClick: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.surface)
                .clickable { onClick() }
                .padding(8.dp),
    ) {
        Text(
            text = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(memo.date),
            style = MaterialTheme.typography.body2,
            color = Color.Gray,
        )
        Text(
            text = memo.detail,
            style = MaterialTheme.typography.body1,
        )
    }
}
