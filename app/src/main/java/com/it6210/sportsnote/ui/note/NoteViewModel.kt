package com.it6210.sportsnote.ui.note

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.it6210.sportsnote.R
import com.it6210.sportsnote.model.FirebaseManager
import com.it6210.sportsnote.model.Note
import com.it6210.sportsnote.model.NoteListItem
import com.it6210.sportsnote.model.PracticeNote
import com.it6210.sportsnote.model.PreferencesManager
import com.it6210.sportsnote.model.RealmManager
import com.it6210.sportsnote.model.TaskData
import com.it6210.sportsnote.model.TaskListData
import com.it6210.sportsnote.ui.measures.MeasuresViewModel
import com.it6210.sportsnote.ui.memo.MemoViewModel
import com.it6210.sportsnote.ui.task.TaskViewModel
import com.it6210.sportsnote.utils.Network
import com.it6210.sportsnote.utils.NoteType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale
import java.util.UUID

class NoteViewModel : ViewModel() {
    private val realmManager: RealmManager = RealmManager.getInstance()
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes
    private val _targetNotes = MutableStateFlow<List<Note>>(emptyList())
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val noteListItems: StateFlow<List<NoteListItem>> =
        _notes.map { notes ->
            notes.map { note ->
                NoteListItem(
                    noteID = note.noteID,
                    noteType = note.noteType,
                    date = note.date,
                    backGroundColor = getBackgroundColor(note),
                    title = getNoteTitle(note),
                    subTitle = getNoteSubTitle(note),
                )
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val targetNotes: StateFlow<List<NoteListItem>> =
        _targetNotes.map { notes ->
            notes.map { note ->
                NoteListItem(
                    noteID = note.noteID,
                    noteType = note.noteType,
                    date = note.date,
                    backGroundColor = getBackgroundColor(note),
                    title = getNoteTitle(note),
                    subTitle = getNoteSubTitle(note),
                )
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        searchNotesByQuery("")
    }

    /**
     * ノートリストを取得（検索）
     *
     * @param query 検索文字列
     */
    fun searchNotesByQuery(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result =
                if (query.isBlank()) {
                    // クエリが空の場合は全件取得
                    realmManager.getDataList(Note::class.java)
                } else {
                    realmManager.searchNotesByQuery(query)
                }
            _notes.value =
                result.sortedWith(
                    compareByDescending<Note> { it.noteType == NoteType.FREE.value }
                        .thenByDescending { it.date },
                )
            _isLoading.value = false
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
     * ノート一覧に表示するタイトルを取得
     *
     * @param note Note
     * @return タイトル
     */
    private fun getNoteTitle(note: Note): String {
        val title =
            when (NoteType.fromInt(note.noteType)) {
                NoteType.FREE -> note.title
                NoteType.PRACTICE -> note.detail
                NoteType.TOURNAMENT -> note.result
            }
        return title
    }

    /**
     * ノート一覧に表示するサブタイトルを取得
     *
     * @param note Note
     * @return サブタイトル
     */
    private fun getNoteSubTitle(note: Note): String {
        val subTitle =
            when (NoteType.fromInt(note.noteType)) {
                NoteType.FREE -> note.detail
                NoteType.PRACTICE -> formatDate(note.date)
                NoteType.TOURNAMENT -> formatDate(note.date)
            }
        return subTitle
    }

    /**
     * 日付をyyyy/MM/dd (曜日)形式でフォーマットする
     */
    private fun formatDate(date: Date): String {
        val format = SimpleDateFormat("yyyy/MM/dd (E)", Locale.getDefault())
        return format.format(date)
    }

    /**
     * ノート一覧に表示する背景色を取得
     *
     * @param note Note
     * @return 背景色
     */
    private fun getBackgroundColor(note: Note): Color {
        val backgroundColor =
            when (NoteType.fromInt(note.noteType)) {
                NoteType.FREE -> Color.White
                NoteType.PRACTICE -> realmManager.getNoteBackgroundColor(note.noteID)
                NoteType.TOURNAMENT -> Color.White
            }
        return backgroundColor
    }

    /**
     * 練習ノート詳細データを取得
     *
     * @param noteId ノートID
     * @return PracticeNote
     */
    fun getPracticeNote(noteId: String): PracticeNote {
        val note: Note? = getNoteById(noteId)

        // 取り組んだ課題セルの内容を取得
        val taskReflections = mutableMapOf<TaskListData, String>()
        val memos = realmManager.getMemosByNoteID(noteID = noteId)
        memos.forEach { memo ->
            // 対策データを取得
            val measuresViewModel = MeasuresViewModel()
            val measures = measuresViewModel.getMeasuresById(measuresID = memo.measuresID)
            // 課題データを取得
            val taskViewModel = TaskViewModel()
            val taskData = realmManager.getObjectById<TaskData>(measures!!.taskID)
            val taskListData = taskViewModel.convertTaskDataToTaskListData(task = taskData!!)
            taskListData.memoID = memo.memoID
            // 取り組んだ課題セルの内容を整理
            taskReflections[taskListData] = memo.detail
        }

        return PracticeNote(
            noteID = note!!.noteID,
            date = note.date,
            weather = note.weather,
            temperature = note.temperature,
            condition = note.condition,
            purpose = note.purpose,
            detail = note.detail,
            reflection = note.reflection,
            taskReflections = taskReflections,
            created_at = note.created_at,
        )
    }

    /**
     * フリーノートを作成(初回起動時)
     *
     * @param context Context
     */
    suspend fun createFreeNote(context: Context) {
        val freeNote = getFreeNote()
        if (freeNote != null) return
        saveFreeNote(
            title = context.getString(R.string.freeNote),
            detail = context.getString(R.string.defaltFreeNoteDetail),
        )
    }

    /**
     * フリーノートを保存
     *
     * @param noteId NoteID
     * @param title タイトル
     * @param detail ノート内容
     */
    suspend fun saveFreeNote(
        noteId: String? = null,
        title: String,
        detail: String,
        created_at: Date = Date(),
    ) {
        val finalNoteId = noteId ?: UUID.randomUUID().toString()
        val isUpdate = noteId != null
        val note =
            Note().apply {
                this.noteID = finalNoteId
                this.userID = PreferencesManager.get(PreferencesManager.Keys.USER_ID, UUID.randomUUID().toString())
                this.noteType = NoteType.FREE.value
                this.isDeleted = false
                this.created_at = created_at
                this.updated_at = Date()
                this.title = title
                this.detail = detail
            }
        realmManager.saveItem(note)

        // Firebaseに反映
        if (!Network.isOnline()) return
        if (!PreferencesManager.get(PreferencesManager.Keys.IS_LOGIN, false)) {
            return
        }
        if (isUpdate) {
            FirebaseManager.updateNote(note)
        } else {
            FirebaseManager.saveNote(note)
        }
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
        noteId: String? = null,
        date: Date,
        weather: Int,
        temperature: Int,
        condition: String,
        target: String,
        consciousness: String,
        result: String,
        reflection: String,
        created_at: Date = Date(),
    ) {
        val finalNoteId = noteId ?: UUID.randomUUID().toString()
        val isUpdate = noteId != null
        val note =
            Note().apply {
                this.noteID = finalNoteId
                this.userID = PreferencesManager.get<String>(PreferencesManager.Keys.USER_ID, UUID.randomUUID().toString())
                this.noteType = NoteType.TOURNAMENT.value
                this.isDeleted = false
                this.created_at = created_at
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

        // Firebaseに反映
        if (!Network.isOnline()) return
        if (!PreferencesManager.get(PreferencesManager.Keys.IS_LOGIN, false)) {
            return
        }
        if (isUpdate) {
            FirebaseManager.updateNote(note)
        } else {
            FirebaseManager.saveNote(note)
        }
    }

    /**
     * 練習ノートを保存する処理
     *
     * @param noteId ノートID
     * @param date 日付
     * @param weather 天気
     * @param temperature 気温
     * @param condition 体調
     * @param purpose 練習の目的
     * @param detail 練習内容
     * @param reflection 反省
     * @param taskReflections 取り組んだ課題のメモ
     */
    suspend fun savePracticeNote(
        noteId: String? = null,
        date: Date,
        weather: Int,
        temperature: Int,
        condition: String,
        purpose: String,
        detail: String,
        reflection: String,
        taskReflections: Map<TaskListData, String>,
        created_at: Date = Date(),
    ) {
        val finalNoteId = noteId ?: UUID.randomUUID().toString()
        val isUpdate = noteId != null
        // 練習ノートを保存
        val note =
            Note().apply {
                this.noteID = finalNoteId
                this.userID = PreferencesManager.get<String>(PreferencesManager.Keys.USER_ID, UUID.randomUUID().toString())
                this.noteType = NoteType.PRACTICE.value
                this.isDeleted = false
                this.created_at = created_at
                this.updated_at = Date()
                this.date = date
                this.weather = weather
                this.temperature = temperature
                this.condition = condition
                this.purpose = purpose
                this.detail = detail
                this.reflection = reflection
            }
        realmManager.saveItem(note)

        // 取り組んだ課題のメモを保存
        val memoViewModel = MemoViewModel()
        taskReflections.forEach { (taskListData, reflectionText) ->
            if (reflectionText.isBlank()) return@forEach
            memoViewModel.saveMemo(
                memoID = taskListData.memoID,
                measuresID = taskListData.measuresID,
                noteID = finalNoteId,
                detail = reflectionText,
            )
        }

        // Firebaseに反映
        if (!Network.isOnline()) return
        if (!PreferencesManager.get(PreferencesManager.Keys.IS_LOGIN, false)) {
            return
        }
        if (isUpdate) {
            FirebaseManager.updateNote(note)
        } else {
            FirebaseManager.saveNote(note)
        }
    }

    /**
     * ノートを論理削除
     *
     * @param noteId ノートID
     */
    suspend fun deleteNote(noteId: String) {
        realmManager.logicalDelete<Note>(noteId)

        // Firebaseに反映
        if (!Network.isOnline()) return
        if (!PreferencesManager.get(PreferencesManager.Keys.IS_LOGIN, false)) {
            return
        }
        val deletedNote = getNoteById(noteId) ?: return
        FirebaseManager.updateNote(deletedNote)
    }
}
