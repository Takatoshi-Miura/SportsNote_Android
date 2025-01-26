package com.example.sportsnote.ui.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportsnote.model.Note
import com.example.sportsnote.model.PreferencesManager
import com.example.sportsnote.model.RealmManager
import com.example.sportsnote.utils.NoteType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Date
import java.util.UUID

class NoteViewModel : ViewModel() {

    private val realmManager: RealmManager = RealmManager()
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes
    private val _targetNotes = MutableStateFlow<List<Note>>(emptyList())
    val targetNotes: StateFlow<List<Note>> = _targetNotes

    init {
        loadNotes()
    }

    /**
     * ノート一覧データを取得
     */
    fun loadNotes() {
        viewModelScope.launch {
            _notes.value = getNoteList()
        }
    }

    /**
     * ノートリストを取得
     *
     * @return List<Note>
     */
    private fun getNoteList(): List<Note> {
        return realmManager.getDataList(Note::class.java)
            .sortedWith(
                compareByDescending<Note> { it.noteType == NoteType.FREE.value }
                    .thenByDescending { it.date }
            )
    }

    /**
     * ノートリストを取得（検索）
     *
     * @param query 検索文字列
     */
    fun searchNotesByQuery(query: String) {
        // クエリが空の場合は全件取得
        if (query.isBlank()) {
            loadNotes()
            return
        }

        _notes.update {
            realmManager.searchNotesByQuery(query)
                .sortedWith(
                    compareByDescending<Note> { it.noteType == NoteType.FREE.value }
                        .thenByDescending { it.date }
                )
        }
    }

    /**
     * 指定された`groupId`に基づいて、`Note`オブジェクトを取得
     *
     * @param noteId noteId
     * @return `noteId`に一致する`Note`オブジェクト。存在しない場合やエラーが発生した場合は`null`
     */
    fun getNoteById(noteId: String): Note? {
        return realmManager.getObjectById<Note>(noteId)
    }

    /**
     * 指定した日付に合致するノートを取得
     *
     * @param selectedDate 日付
     */
    fun getNoteListByDate(selectedDate: LocalDate) {
        _targetNotes.value = realmManager.getNotesByDate(selectedDate)
    }

    /**
     * フリーノートを取得
     */
    fun getFreeNote(): Note? {
        return realmManager.getFreeNote()
    }

    /**
     * フリーノートを保存
     *
     * @param noteId NoteID
     * @param title タイトル
     * @param detail ノート内容
     */
    suspend fun saveFreeNote(
        noteId: String = UUID.randomUUID().toString(),
        title: String,
        detail: String
    ) {
        val note = Note().apply {
            this.noteID = noteId
            this.userID = PreferencesManager.get(PreferencesManager.Keys.USER_ID, UUID.randomUUID().toString())
            this.noteType = NoteType.FREE.value
            this.isDeleted = false
            this.created_at = Date()
            this.updated_at = Date()
            this.title = title
            this.detail = detail
        }
        realmManager.saveItem(note)
    }

    /**
     * 大会ノートを保存する処理
     *
     * @param noteId ノートID
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
        noteId: String = UUID.randomUUID().toString(),
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
            this.noteID = noteId
            this.userID = PreferencesManager.get<String>(PreferencesManager.Keys.USER_ID, UUID.randomUUID().toString())
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
        realmManager.saveItem(note)
    }

    /**
     * ノートを論理削除
     *
     * @param noteId ノートID
     */
    suspend fun deleteNote(noteId: String) {
        realmManager.logicalDelete<Note>(noteId)
    }
}