package com.example.sportsnote.ui.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportsnote.R
import com.example.sportsnote.model.Note
import com.example.sportsnote.model.RealmManager
import com.example.sportsnote.ui.components.ItemData
import com.example.sportsnote.utils.NoteType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class NoteViewModel : ViewModel() {

    private val _items = MutableStateFlow<List<ItemData>>(emptyList())
    val items: StateFlow<List<ItemData>> = _items
    private val realmManager: RealmManager = RealmManager()

    init {
        loadNotes()
    }

    /**
     * ノート一覧データを取得
     */
    fun loadNotes() {
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
        return realmManager.selectNoteList()
    }

    /**
     * 大会ノートを保存する処理
     *
     * @param date 日付
     * @param weather 天気
     * @param temperature 気温
     * @param condition 体調
     * @param target 目標
     * @param consciousness 意識すること
     * @param result 結果
     * @param reflection 反省
     */
    suspend fun saveTournamentNote(
        date: Date,
        weather: Int,
        temperature: Int,
        condition: String,
        target: String,
        consciousness: String,
        result: String,
        reflection: String
    ) {
        val note = Note().apply {
            this.noteID = UUID.randomUUID().toString()
            this.userID = "defaultUserID" // TODO: SharedPreferenceなどから取得する想定
            this.noteType = NoteType.TOURNAMENT.value
            this.isDeleted = false
            this.created_at = Date()
            this.updated_at = Date()
            this.date = date
            this.weather = weather
            this.temperature = temperature
            this.condition = condition
            this.target = target
            this.consciousness = consciousness
            this.result = result
            this.reflection = reflection
        }
        realmManager.saveNote(note)
    }

}