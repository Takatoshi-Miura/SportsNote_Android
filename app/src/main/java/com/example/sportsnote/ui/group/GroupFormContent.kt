package com.example.sportsnote.ui.group

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.sportsnote.R
import com.example.sportsnote.model.Group
import com.example.sportsnote.ui.components.ColorPickerField
import com.example.sportsnote.ui.components.CustomSpacerColumn
import com.example.sportsnote.ui.components.MultiLineTextInputField
import com.example.sportsnote.utils.Color
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
 * @param onOrderChanged 並び替え時の処理
 */
@Composable
fun GroupFormContent(
    group: Group? = null,
    onTitleChange: (String) -> Unit,
    onColorChange: (Int) -> Unit,
) {
    val groupViewModel = GroupViewModel()
    val groups by groupViewModel.groups.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val title = remember { mutableStateOf(group?.title ?: "") }
    val color = remember { mutableStateOf(group?.color ?: Color.RED.id) }

    val inputFields: List<@Composable () -> Unit> =
        buildList {
            // タイトル
            add {
                MultiLineTextInputField(
                    title = stringResource(R.string.title),
                    onTextChanged = { updatedText -> title.value = updatedText },
                    initialText = title.value,
                )
            }
            // カラー
            add {
                ColorPickerField(
                    onColorSelected = { selectedColor -> color.value = selectedColor },
                    initialColor = color.value,
                )
            }
            // グループ（グループ詳細画面のみ追加）
            if (group != null) {
                add {
                    Text(
                        text = stringResource(R.string.displayOrder),
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(8.dp),
                    )
                    Divider()
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
    onColorChange(color.value)
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
                GroupItem(
                    group = group,
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

@Composable
fun GroupItem(
    group: Group,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .background(MaterialTheme.colors.surface)
                .height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // タイトル
        Text(
            text = group.title,
            style = MaterialTheme.typography.body1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
        // 並び替えアイコン
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Drag Handle",
            modifier = Modifier.padding(end = 8.dp),
        )
    }
}
