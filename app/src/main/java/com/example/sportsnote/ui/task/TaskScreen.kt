package com.example.sportsnote.ui.task

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.sportsnote.R
import com.example.sportsnote.ui.components.ItemData
import com.example.sportsnote.ui.components.LazySectionedColumn
import com.example.sportsnote.ui.components.SectionData

@Composable
fun TaskScreen() {
    LazySectionedColumn(sections = getSections())
}

/**
 * 課題一覧データを取得
 *
 * @return List<SectionData>
 */
private fun getSections(): List<SectionData> {
    return listOf(
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
}

@Preview(showBackground = true)
@Composable
fun PreviewLazySectionedColumn() {
    LazySectionedColumn(sections = getSections())
}