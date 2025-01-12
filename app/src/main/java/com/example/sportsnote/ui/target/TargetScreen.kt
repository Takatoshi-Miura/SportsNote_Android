package com.example.sportsnote.ui.target

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.Text
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.sportsnote.R
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun TargetScreen() {
    val systemGray6 = Color(0xFFF2F2F7)

    Box(
        modifier = Modifier
            .background(systemGray6)
            .fillMaxSize()
    ) {
        val coroutineScope = rememberCoroutineScope()
        val BOTTOM_NAVIGATION_HEIGHT = 56.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // 年間目標と月間目標
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(8.dp)
            ) {
                Column {
                    TargetLabel(stringResource(R.string.targetYear, "年間目標が入ります"))
                    TargetLabel(stringResource(R.string.targetMonth, "月間目標が入ります"))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // カレンダー
            CalendarDisplay(
                modifier = Modifier
                    .background(Color.White)
            )
        }

        // +ボタン
        FloatingActionButton(
            onClick = {
                coroutineScope.launch {  }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = 16.dp,
                    bottom = 16.dp + BOTTOM_NAVIGATION_HEIGHT
                )
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add Target")
        }
    }
}

/**
 * 目標表示ラベル
 *
 * @param text 目標文字列
 */
@Composable
fun TargetLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.body1,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    )
}

/**
 * カレンダーを表示するコンポーネント
 *
 * @param modifier Modifier
 */
@Composable
fun CalendarDisplay(modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    // 現在の年月
    val currentMonth = remember { YearMonth.now() }
    // 現在より前の年月
    val startMonth = remember { currentMonth.minusMonths(100) }
    // 現在より後の年月
    val endMonth = remember { currentMonth.plusMonths(100) }
    // 曜日
    val daysOfWeek = remember { daysOfWeek() }
    // カレンダーの状態を持つ
    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first(),
        outDateStyle = OutDateStyle.EndOfGrid
    )

    Column(modifier = modifier) {
        val visibleMonth = state.firstVisibleMonth.yearMonth
        val formattedMonth = "${visibleMonth.year} / ${"%02d".format(visibleMonth.monthValue)}"

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // 左矢印
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        state.scrollToMonth(state.firstVisibleMonth.yearMonth.minusMonths(1))
                    }
                }
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Previous Month")
            }

            // 現在表示中の月を表示
            Text(
                text = formattedMonth,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            // 右矢印
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        state.scrollToMonth(state.firstVisibleMonth.yearMonth.plusMonths(1))
                    }
                }
            ) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Next Month")
            }
        }

        // 横スクロールのカレンダーを作成
        HorizontalCalendar(
            state = state,
            dayContent = { Day(it) },
            monthHeader = { DaysOfWeekTitle(daysOfWeek = daysOfWeek) }
        )
    }
}

/**
 * 曜日ラベルを表示するコンポーネント
 *
 * @param daysOfWeek 曜日
 */
@Composable
fun DaysOfWeekTitle(daysOfWeek: List<DayOfWeek>) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                color = getDayOfWeekTextColor(dayOfWeek),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * 曜日ラベルの文字色を返却
 *
 * @param dayOfWeek 曜日
 * @return 文字色
 */
@Composable
fun getDayOfWeekTextColor(dayOfWeek: DayOfWeek): Color {
    return when (dayOfWeek) {
        DayOfWeek.SATURDAY -> Color.Blue
        DayOfWeek.SUNDAY -> Color.Red
        else -> Color.Black
    }
}

/**
 * カレンダーの日付を表示するコンポーネント
 *
 * @param day カレンダー日付
 */
@Composable
fun Day(day: CalendarDay) {
    val today = remember { java.time.LocalDate.now() }
    val isToday = day.date.isEqual(today)

    Box(
        modifier = Modifier
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        // 背景色を付与
        if (isToday) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colors.primary, shape = CircleShape)
            )
        }

        // 数字
        Text(
            text = day.date.dayOfMonth.toString(),
            color = getDayTextColor(day),
            style = MaterialTheme.typography.body2
        )
    }
}

/**
 * 日付の数字の色を返却
 *
 * @param day 日付
 * @return 日付の数字の色
 */
@Composable
fun getDayTextColor(day: CalendarDay): Color {
    val today = remember { java.time.LocalDate.now() }
    val isToday = day.date.isEqual(today)

    return when {
        isToday -> Color.White
        day.date.dayOfWeek == DayOfWeek.SUNDAY -> Color.Red
        day.date.dayOfWeek == DayOfWeek.SATURDAY -> Color.Blue
        day.position == DayPosition.MonthDate -> Color.Black
        else -> Color.Gray
    }
}
