package com.it6210.sportsnote.ui.components.items

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.it6210.sportsnote.R
import com.it6210.sportsnote.model.Group
import com.it6210.sportsnote.utils.Color

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
