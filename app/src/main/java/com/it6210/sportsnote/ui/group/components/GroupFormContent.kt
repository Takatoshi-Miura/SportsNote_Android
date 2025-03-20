package com.it6210.sportsnote.ui.group.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.it6210.sportsnote.R
import com.it6210.sportsnote.model.Group
import com.it6210.sportsnote.ui.components.items.ColorPickerField
import com.it6210.sportsnote.ui.components.items.CustomSpacerColumn
import com.it6210.sportsnote.ui.components.items.ItemLabel
import com.it6210.sportsnote.ui.components.items.SingleLineTextInputField
import com.it6210.sportsnote.ui.components.items.SortItem
import com.it6210.sportsnote.utils.Color
import com.it6210.sportsnote.viewModel.GroupViewModel
import kotlinx.coroutines.launch
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

/**
 * Groupの共通フォーム
 *
 * @param group Groupデータ（nullならデフォルト値を設定）
 * @param onTitleChange タイトル変更時の処理
 * @param onColorChange カラー変更時の処理
 * @param onGroupsChange グループ変更時の処理
 */
@Composable
fun GroupFormContent(
    group: Group? = null,
    onTitleChange: (String) -> Unit,
    onColorChange: (Int) -> Unit,
    onGroupsChange: (List<Group>) -> Unit,
) {
    val groupViewModel = GroupViewModel()
    val coroutineScope = rememberCoroutineScope()

    // 入力データの状態管理
    val groups by groupViewModel.groups.collectAsState()
    val title = remember { mutableStateOf(group?.title ?: "") }
    val color = remember { mutableIntStateOf(group?.color ?: Color.RED.id) }

    val inputFields: List<@Composable () -> Unit> =
        buildList {
            // タイトル
            add {
                SingleLineTextInputField(
                    title = stringResource(R.string.title),
                    onTextChanged = { updatedText -> title.value = updatedText },
                    initialText = title.value,
                )
            }
            // カラー
            add {
                ColorPickerField(
                    onColorSelected = { selectedColor -> color.intValue = selectedColor },
                    initialColor = color.intValue,
                )
            }
            // グループ（グループ詳細画面のみ追加）
            if (group != null) {
                add {
                    GroupListContent(
                        groupList = groups,
                        onOrderChanged = { updatedList ->
                            coroutineScope.launch {
                                // 並び順を更新
                                updatedList.forEach { group ->
                                    groupViewModel.saveGroup(
                                        groupId = group.groupID,
                                        title = group.title,
                                        colorId = group.color,
                                        order = group.order,
                                        created_at = group.created_at,
                                    )
                                }
                                groupViewModel.loadData()
                                onGroupsChange(updatedList)
                            }
                        },
                        onItemClick = { },
                    )
                }
            }
        }

    // 入力フィールド
    CustomSpacerColumn(items = inputFields)

    // 変更されたtitleとcolorを返す
    onTitleChange(title.value)
    onColorChange(color.intValue)
}

/**
 * 並び替え可能なGroupリストUI
 *
 * @param groupList Groupリスト
 * @param onOrderChanged 並び替え時の処理
 * @param onItemClick アイテムクリック時の処理
 */
@Composable
fun GroupListContent(
    groupList: List<Group>,
    onOrderChanged: (List<Group>) -> Unit,
    onItemClick: (String) -> Unit,
) {
    // リストの状態を保持
    var list by remember { mutableStateOf(groupList) }

    // 並び替えロジック
    val state =
        rememberReorderableLazyListState(onMove = { from, to ->
            list =
                list.toMutableList().apply {
                    add(to.index, removeAt(from.index))
                }.mapIndexed { index, group ->
                    Group(
                        groupId = group.groupID,
                        title = group.title,
                        colorId = group.color,
                        order = index + 1,
                        created_at = group.created_at,
                    )
                }
            onOrderChanged(list)
        })

    // 項目ラベル
    ItemLabel(stringResource(R.string.displayOrder))
    Divider()

    // グループリスト
    LazyColumn(
        state = state.listState,
        modifier =
            Modifier
                .fillMaxSize()
                .reorderable(state)
                .detectReorderAfterLongPress(state),
    ) {
        itemsIndexed(list, key = { _, item -> item.groupID }) { _, group ->
            ReorderableItem(state, key = group.groupID) { isDragging ->
                SortItem(
                    title = group.title,
                    modifier =
                        Modifier
                            .background(if (isDragging) MaterialTheme.colors.primary.copy(alpha = 0.1f) else MaterialTheme.colors.surface)
                            .fillMaxWidth()
                            .clickable {
                                onItemClick(group.groupID)
                            }
                            .padding(8.dp),
                )
                Divider()
            }
        }
    }
}
