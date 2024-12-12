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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
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
 * @param iconRes アイコン画像のリソースID
 * @param onClick タップ時のアクション
 */
data class ItemData(
    val title: String,
    val iconRes: Int,
    val onClick: () -> Unit = {}
)

/**
 * セクションなしのLazyColumnを作成
 *
 * @param items リスト項目
 */
@Composable
fun LazyNonSectionedColumn(items: List<ItemData>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(items) { item ->
            TextListItem(title = item.title, onClick = item.onClick)
            Divider(thickness = 1.dp)
        }
    }
}

/**
 * セクション付きのLazyColumnを作成
 *
 * @param sections セクション
 */
@Composable
fun LazySectionedColumn(sections: List<SectionData>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        sections.forEach { section ->
            // セクションヘッダーを描画
            item {
                SectionHeader(title = section.title)
            }
            // セクション内のアイテムを描画
            items(section.items) { item ->
                SectionItem(item = item)
                Divider(thickness = 1.dp)
            }
            // セクション間の区切り
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
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
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = item.iconRes),
            contentDescription = "Item Icon",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.padding(horizontal = 8.dp))
        Text(
            text = item.title,
            fontSize = 16.sp
        )
    }
}

/**
 * テキストのみのリストアイテム
 *
 * @param title タイトル
 * @param onClick 押下時の処理
 */
@Composable
fun TextListItem(title: String, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Text(
            text = title,
            fontSize = 16.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLazyNonSectionedColumn() {
    val items = listOf(
        ItemData("Apple", R.drawable.ic_home_black_24dp) { println("Apple clicked") },
        ItemData("Banana", R.drawable.ic_home_black_24dp) { println("Banana clicked") },
        ItemData("Orange", R.drawable.ic_home_black_24dp) { println("Orange clicked") }
    )
    LazyNonSectionedColumn(items = items)
}

@Preview(showBackground = true)
@Composable
fun PreviewSectionedColumn() {
    val sections = listOf(
        SectionData(
            title = "Fruits",
            items = listOf(
                ItemData("Apple", R.drawable.ic_home_black_24dp) { println("Apple clicked") },
                ItemData("Banana", R.drawable.ic_home_black_24dp) { println("Banana clicked") },
                ItemData("Orange", R.drawable.ic_home_black_24dp) { println("Orange clicked") }
            )
        ),
        SectionData(
            title = "Vegetables",
            items = listOf(
                ItemData("Carrot", R.drawable.ic_home_black_24dp) { println("Carrot clicked") },
                ItemData("Potato", R.drawable.ic_home_black_24dp) { println("Potato clicked") },
                ItemData("Spinach", R.drawable.ic_home_black_24dp) { println("Spinach clicked") }
            )
        )
    )
    SectionedColumn(sections = sections)
}