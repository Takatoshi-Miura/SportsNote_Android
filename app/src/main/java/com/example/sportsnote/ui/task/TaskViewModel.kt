package com.example.sportsnote.ui.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportsnote.R
import com.example.sportsnote.ui.components.ItemData
import com.example.sportsnote.ui.components.SectionData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class TaskViewModel : ViewModel() {

    private val _sections = MutableStateFlow<List<SectionData>>(emptyList())
    val sections: StateFlow<List<SectionData>> = _sections

    init {
        loadSections()
    }

    /**
     * 課題一覧のダミーデータをロード
     */
    private fun loadSections() {
        viewModelScope.launch {
            _sections.value = listOf(
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
    }
}