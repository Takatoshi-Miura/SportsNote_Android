package com.example.sportsnote.ui.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportsnote.R
import com.example.sportsnote.model.Note
import com.example.sportsnote.ui.components.ItemData
import com.example.sportsnote.utils.NoteType
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
            _items.value = getNoteList().map { note ->
                val displayText = when (NoteType.fromInt(note.noteType)) {
                    NoteType.FREE -> note.title
                    NoteType.PRACTICE -> note.detail
                    NoteType.TOURNAMENT -> note.result
                }
                ItemData(
                    title = displayText,
                    iconRes = R.drawable.ic_home_black_24dp,
                    onClick = { println("$displayText clicked") }
                )
            }
        }
    }

    /**
     * ノートリストを取得
     *
     * @return List<Note>
     */
    private fun getNoteList(): List<Note> {
        // TODO: Realmから取得するように修正
        return listOf(
            Note(title = "フリーノート"),
            Note(purpose = "練習の目的", detail = "練習内容"),
            Note(target = "目標", consciousness = "考慮すること", result = "結果")
        )
    }

}