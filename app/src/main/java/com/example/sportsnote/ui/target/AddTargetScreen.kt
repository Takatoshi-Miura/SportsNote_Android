package com.example.sportsnote.ui.target

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.sportsnote.R
import com.example.sportsnote.ui.components.CustomSpacerColumn
import com.example.sportsnote.ui.components.MultiLineTextInputField
import com.example.sportsnote.ui.components.header.AddScreenHeader
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * Target登録画面
 *
 * @param isYearlyTarget 年間目標か？
 * @param onDismiss 閉じる際の処理
 */
@Composable
fun AddTargetScreen(
    isYearlyTarget: Boolean,
    onDismiss: () -> Unit
) {
    val targetViewModel = TargetViewModel()
    val coroutineScope = rememberCoroutineScope()
    var title by remember { mutableStateOf("") }
    var year by remember { mutableStateOf(2025) }
    var month by remember { mutableStateOf(1) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        val context = LocalContext.current

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.surface)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // ヘッダー
                AddScreenHeader(
                    title = stringResource(R.string.AddTarget),
                    onCancel = onDismiss,
                    onSave = {
                        // タイトル未入力の場合
                        if (title.isBlank()) {
                            coroutineScope.launch {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.ErrorTitleEmpty),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            return@AddScreenHeader
                        }
                        // 保存処理
                        targetViewModel.saveTarget(
                            title = title,
                            year = year,
                            month = month,
                            isYearlyTarget = isYearlyTarget
                        )
                        onDismiss()
                    },
                    coroutineScope = coroutineScope
                )

                // Target共通フォーム
                TargetFormContent(
                    isYearlyTarget = isYearlyTarget,
                    onTitleChange = { title = it },
                    onYearChange = { year = it },
                    onMonthChange = { month = it }
                )
            }
        }
    }
}

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
    onMonthChange: (Int) -> Unit
) {
    val currentDate = LocalDate.now()
    val title = remember { mutableStateOf("") }
    val year = remember { mutableStateOf(currentDate.year) }
    val month = remember { mutableStateOf(currentDate.monthValue) }

    val inputFields: List<@Composable () -> Unit> = mutableListOf<@Composable () -> Unit>().apply {
        // タイトル
        add {
            MultiLineTextInputField(
                title = stringResource(R.string.title),
                onTextChanged = { updatedText -> title.value = updatedText },
                initialText = title.value
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // 入力フィールド
        CustomSpacerColumn(items = inputFields)

        // 変更された値を返す
        onTitleChange(title.value)
        onYearChange(year.value)
        onMonthChange(month.value)
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
        label = stringResource(R.string.year)
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
        label = stringResource(R.string.month)
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
    label: String
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            value = "%02d".format(selectedValue.value),
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
            },
            modifier = Modifier.fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        selectedValue.value = item
                        expanded = false
                    }
                ) {
                    Text(text = "%02d".format(item))
                }
            }
        }
    }
}
