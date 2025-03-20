package com.it6210.sportsnote.ui.task.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.it6210.sportsnote.R
import com.it6210.sportsnote.model.AddTaskData
import com.it6210.sportsnote.model.Group
import com.it6210.sportsnote.ui.components.items.CustomSpacerColumn
import com.it6210.sportsnote.ui.components.items.GroupPickerField
import com.it6210.sportsnote.ui.components.items.MultiLineTextInputField
import com.it6210.sportsnote.ui.components.items.SingleLineTextInputField
import com.it6210.sportsnote.viewModel.GroupViewModel

/**
 * 課題追加画面のフォーム
 *
 * @param onTitleChange タイトル変更時の処理
 * @param onCauseChange 原因変更時の処理
 * @param onMeasuresChange 対策変更時の処理
 * @param onGroupChange グループ変更時の処理
 */
@Composable
fun AddTaskFormContent(
    onTitleChange: (String) -> Unit,
    onCauseChange: (String) -> Unit,
    onMeasuresChange: (String) -> Unit,
    onGroupChange: (Group) -> Unit,
) {
    // グループリストを取得
    val groupViewModel = GroupViewModel()
    val groups by groupViewModel.groups.collectAsState()
    val addTaskData = AddTaskData(groupList = groups)

    // 入力データの状態を管理
    val title = remember { mutableStateOf(addTaskData.title) }
    val cause = remember { mutableStateOf(addTaskData.cause) }
    val measures = remember { mutableStateOf(addTaskData.measuresTitle) }
    val group = remember { mutableStateOf(addTaskData.groupList.first()) }

    val inputFields: List<@Composable () -> Unit> =
        listOf(
            // タイトル
            {
                SingleLineTextInputField(
                    title = stringResource(R.string.title),
                    onTextChanged = { updatedText -> title.value = updatedText },
                    initialText = title.value,
                )
            },
            // 原因
            {
                MultiLineTextInputField(
                    title = stringResource(R.string.cause),
                    defaultLines = 3,
                    onTextChanged = { updatedText -> cause.value = updatedText },
                    initialText = cause.value,
                )
            },
            // 対策
            {
                SingleLineTextInputField(
                    title = stringResource(R.string.measures),
                    onTextChanged = { updatedText -> measures.value = updatedText },
                    initialText = measures.value,
                )
            },
            // グループ
            {
                GroupPickerField(
                    groups = addTaskData.groupList,
                    onGroupSelected = { selectedGroup -> group.value = selectedGroup },
                    initialGroup = group.value,
                )
            },
        )

    // 入力欄のレイアウト
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
    ) {
        CustomSpacerColumn(items = inputFields)

        // 変更された入力値を返す
        onTitleChange(title.value)
        onCauseChange(cause.value)
        onMeasuresChange(measures.value)
        onGroupChange(group.value)
    }
}
