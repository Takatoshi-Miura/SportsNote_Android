package com.it6210.sportsnote.ui.note.components

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.it6210.sportsnote.R
import com.it6210.sportsnote.ui.components.items.ItemLabel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

/**
 * 日付入力欄
 *
 * @param initialDate 初期値
 * @param onDateSelected 日付選択時の処理
 */
@Composable
fun DatePickerField(
    initialDate: Date? = null,
    onDateSelected: (Date) -> Unit,
) {
    // カレンダーのインスタンスを作成
    val calendar = Calendar.getInstance()

    // 初期日付を設定
    val initialCalendar =
        initialDate?.let {
            calendar.time = it
            calendar
        } ?: calendar

    // 初期状態として渡された初期日付、もしくは今日の日付を設定
    val selectedDate =
        remember {
            mutableStateOf(SimpleDateFormat("yyyy/MM/dd").format(initialCalendar.time))
        }

    // ダイアログの表示状態
    val context = LocalContext.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // タイトルラベル
        ItemLabel(title = stringResource(R.string.date))
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
                    calendar.get(Calendar.DAY_OF_MONTH),
                ).show()
            },
            modifier = Modifier.weight(1f),
        ) {
            Text(text = selectedDate.value)
        }
    }
}
