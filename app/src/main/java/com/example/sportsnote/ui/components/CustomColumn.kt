package com.example.sportsnote.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sportsnote.R

/**
 * セクションタイトルと含有するアイテムのデータモデル
 *
 * @param title セクションのタイトル
 * @param items セルのアイテムリスト
 */
data class SectionData(
    val title: String,
    val items: List<ItemData>
)

/**
 * セクション内のアイテムのデータモデル
 *
 * @param title アイテムのタイトル
 * @param subTitle アイテムのサブタイトル
 * @param iconRes アイコン画像のリソースID
 * @param onClick タップ時のアクション
 */
data class ItemData(
    val title: String,
    val subTitle: String = "",
    val iconRes: Int,
    val onClick: () -> Unit = {}
)

/**
 * 入力項目間にスペースを付与するColumn
 *
 * @param items 入力項目リスト
 * @param spacerHeight 付与したいスペース
 */
@Composable
fun CustomSpacerColumn(
    items: List<@Composable () -> Unit>,
    spacerHeight: Dp = 4.dp
) {
    Column {
        items.forEach { item ->
            item()
            Spacer(modifier = Modifier.height(spacerHeight))
        }
    }
}

/**
 * セクション付きのColumnを作成
 *
 * @param sections セクション
 */
@Composable
fun SectionedColumn(sections: List<SectionData>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        sections.forEach { section ->
            // セクションヘッダーを描画
            SectionHeader(title = section.title)

            // セクションのアイテムを描画
            section.items.forEachIndexed { index, item ->
                SectionItem(item = item)
                if (index < section.items.lastIndex) {
                    Divider(thickness = 1.dp)
                }
            }

            // セクション間の区切り
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * セクションのヘッダーを作成
 *
 * @param title セクションのタイトル
 */
@Composable
fun SectionHeader(title: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        Divider(thickness = 2.dp)
    }
}

/**
 * セクション内のアイテムを作成
 *
 * @param item アイテムデータ
 */
@Composable
fun SectionItem(item: ItemData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { item.onClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = item.iconRes),
            contentDescription = "Item Icon",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = item.title,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = item.subTitle,
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}
