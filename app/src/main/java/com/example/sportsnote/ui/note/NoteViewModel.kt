package com.example.sportsnote.ui.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportsnote.R
import com.example.sportsnote.ui.components.ItemData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NoteViewModel : ViewModel() {

    private val _items = MutableStateFlow<List<ItemData>>(emptyList())
    val items: StateFlow<List<ItemData>> = _items

    init {
        loadNotes()
    }

    /**
     * ノート一覧のダミーデータを取得
     */
    private fun loadNotes() {
        viewModelScope.launch {
            // ダミーデータをロード
            _items.value = listOf(
                ItemData("Apple", R.drawable.ic_home_black_24dp) { println("Apple clicked") },
                ItemData("Banana", R.drawable.ic_home_black_24dp) { println("Banana clicked") },
                ItemData("Orange", R.drawable.ic_home_black_24dp) { println("Orange clicked") }
            )
        }
    }

}