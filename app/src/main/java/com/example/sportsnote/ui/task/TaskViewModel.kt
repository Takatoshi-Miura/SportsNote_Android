package com.example.sportsnote.ui.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportsnote.R
import com.example.sportsnote.model.RealmManager
import com.example.sportsnote.model.TaskData
import com.example.sportsnote.ui.components.ItemData
import com.example.sportsnote.ui.components.SectionData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID


class TaskViewModel : ViewModel() {

    private val realmManager: RealmManager = RealmManager()
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

    /**
     * TaskDataを保存
     *
     * @param taskId taskID
     * @param title タイトル
     * @param cause 原因
     * @param groupId グループID
     * @param taskId 保存したTaskのID
     */
    suspend fun saveTask(
        taskId: String = UUID.randomUUID().toString(),
        title: String,
        cause: String,
        groupId: String,
    ): String {
        val task = TaskData(
            taskId = taskId,
            title = title,
            cause = cause,
            groupId = groupId
        )
        realmManager.saveItem(task)
        return taskId
    }
}