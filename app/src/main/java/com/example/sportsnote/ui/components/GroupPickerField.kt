package com.example.sportsnote.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.sportsnote.R
import com.example.sportsnote.model.Group
import com.example.sportsnote.utils.Color

/**
 * Group選択欄
 *
 * @param groups Groupリスト
 * @param onGroupSelected Group選択時の処理
 * @param initialGroup 初期値
 */
@Composable
fun GroupPickerField(
    groups: List<Group>,
    onGroupSelected: (Group) -> Unit,
    initialGroup: Group,
) {
    DropdownPickerField(
        fieldName = stringResource(R.string.group),
        items = groups,
        initialSelected = initialGroup,
        onItemSelected = onGroupSelected,
        displayText = { it.title },
        buttonColor = { Color.fromInt(it.color).toComposeColor() },
    )
}
