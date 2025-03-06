package com.it6210.sportsnote.ui.target.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.it6210.sportsnote.R
import com.it6210.sportsnote.ui.components.CustomSpacerColumn
import com.it6210.sportsnote.ui.components.SingleLineTextInputField
import java.time.LocalDate

/**
 * Targetの共通フォーム
 *
 * @param isYearlyTarget 年間目標か？
 * @param onTitleChange タイトル変更時の処理
 * @param onYearChange 年変更時の処理
 * @param onMonthChange 月変更時の処理
 */
@Composable
fun TargetFormContent(
    isYearlyTarget: Boolean,
    onTitleChange: (String) -> Unit,
    onYearChange: (Int) -> Unit,
    onMonthChange: (Int) -> Unit,
) {
    val currentDate = LocalDate.now()
    val title = remember { mutableStateOf("") }
    val year = remember { mutableIntStateOf(currentDate.year) }
    val month = remember { mutableIntStateOf(currentDate.monthValue) }

    val inputFields: List<@Composable () -> Unit> =
        mutableListOf<@Composable () -> Unit>().apply {
            // タイトル
            add {
                SingleLineTextInputField(
                    title = stringResource(R.string.title),
                    onTextChanged = { updatedText -> title.value = updatedText },
                    initialText = title.value,
                )
            }
            // 年の選択
            add {
                YearSelection(year = year)
            }
            // 月の選択
            if (!isYearlyTarget) {
                add {
                    MonthSelection(month = month)
                }
            }
        }

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
    ) {
        // 入力フィールド
        CustomSpacerColumn(items = inputFields)

        // 変更された値を返す
        onTitleChange(title.value)
        onYearChange(year.intValue)
        onMonthChange(month.intValue)
    }
}

/**
 * 年を選択するコンポーネント
 *
 * @param year 年
 */
@Composable
fun YearSelection(year: MutableState<Int>) {
    val currentDate = LocalDate.now()
    val years = (1950..currentDate.year + 10).toList()

    SelectableDropdown(
        items = years,
        selectedValue = year,
        label = stringResource(R.string.year),
    )
}

/**
 * 月を選択するコンポーネント
 *
 * @param month 月
 */
@Composable
fun MonthSelection(month: MutableState<Int>) {
    val months = (1..12).toList()

    SelectableDropdown(
        items = months,
        selectedValue = month,
        label = stringResource(R.string.month),
    )
}

/**
 * Dropdownメニューリストを表示するコンポーネント
 *
 * @param items 選択肢のリスト
 * @param selectedValue 選択された値
 * @param label ラベル文字列
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectableDropdown(
    items: List<Int>,
    selectedValue: MutableState<Int>,
    label: String,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
    ) {
        TextField(
            value = "%02d".format(selectedValue.value),
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
            },
            modifier = Modifier.fillMaxWidth(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        selectedValue.value = item
                        expanded = false
                    },
                ) {
                    Text(text = "%02d".format(item))
                }
            }
        }
    }
}
