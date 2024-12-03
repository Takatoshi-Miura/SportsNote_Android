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
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
 * @param items セルのタイトルリスト
 */
data class SectionData(
    val title: String,
    val items: List<String>
)

/**
 * セクション付きのリストを作成
 *
 * @param sections セクション
 * @param onItemClick タップ時のアクション
 */
@Composable
fun SectionedColumn(
    sections: List<SectionData>,
    onItemClick: (String) -> Unit
) {
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
                SectionItem(
                    item = item,
                    onItemClick = onItemClick
                )
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
 * @param item アイテムのタイトル
 * @param onItemClick アイテムタップ時のアクション
 */
@Composable
fun SectionItem(item: String, onItemClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(item) }
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_home_black_24dp),
            contentDescription = "Item Icon",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.padding(horizontal = 8.dp))
        Text(
            text = item,
            fontSize = 16.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSectionedColumn() {
    val sections = listOf(
        SectionData("Fruits", listOf("Apple", "Banana", "Orange")),
        SectionData("Vegetables", listOf("Carrot", "Potato", "Spinach"))
    )
    val selectedItem = remember { mutableStateOf("") }

    SectionedColumn(
        sections = sections,
        onItemClick = { item ->
            selectedItem.value = item
            println("Item clicked: $item")
        }
    )
}