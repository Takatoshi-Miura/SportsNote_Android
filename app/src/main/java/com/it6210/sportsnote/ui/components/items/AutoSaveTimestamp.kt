package com.it6210.sportsnote.ui.components.items

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.it6210.sportsnote.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 自動保存日時を表示するコンポーネント
 *
 * @param date 日時
 * @param modifier Modifier
 */
@Composable
fun AutoSaveTimestamp(
    date: Date,
    modifier: Modifier = Modifier,
) {
    val dateFormat = remember { SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()) }
    val displayTime by remember(date) { derivedStateOf { dateFormat.format(date) } }

    Text(
        text = stringResource(R.string.autoSave, displayTime),
        style = MaterialTheme.typography.subtitle2,
        color = Color.Gray,
        modifier = modifier.padding(horizontal = 16.dp),
    )
}
