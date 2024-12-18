package com.example.sportsnote.ui.components

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Calendar
import java.util.Date

/**
 * 日付入力欄
 */
@Composable
fun DatePickerField(onDateSelected: (Date) -> Unit) {
    // カレンダーのインスタンスを作成
    val calendar = Calendar.getInstance()

    // 初期状態として今日の日付を設定
    val initialDate = "${calendar.get(Calendar.YEAR)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(
        Calendar.DAY_OF_MONTH)}"
    val selectedDate = remember { mutableStateOf(initialDate) }

    // ダイアログの表示状態
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "日付",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(end = 8.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        // 選択した日付の表示 or ボタン
        Button(
            onClick = {
                DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        // 日付が選択されたらフォーマットして表示
                        selectedDate.value = "$year/${month + 1}/$dayOfMonth"

                        // 選択した日付をDate型で渡す
                        calendar.set(year, month, dayOfMonth)
                        val date = calendar.time
                        onDateSelected(date)
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            },
            modifier = Modifier.weight(1f) // 右隣まで伸ばす
        ) {
            Text(text = selectedDate.value)
        }
    }
}