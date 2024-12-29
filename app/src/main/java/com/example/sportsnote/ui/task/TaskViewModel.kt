package com.example.sportsnote.ui.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportsnote.model.RealmManager
import com.example.sportsnote.model.TaskData
import com.example.sportsnote.model.TaskListData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID


class TaskViewModel : ViewModel() {

    private val realmManager: RealmManager = RealmManager()
    private val _tasks = MutableStateFlow<List<TaskData>>(emptyList())
    val tasks: StateFlow<List<TaskData>> = _tasks
    private val _taskLists = MutableStateFlow<List<TaskListData>>(emptyList())
    val taskLists: StateFlow<List<TaskListData>> = _taskLists

    init {
        loadData()
    }

    /**
     * 課題一覧データをロード
     */
    fun loadData() {
        viewModelScope.launch {
            _tasks.value = realmManager.getDataList(TaskData::class.java)

            // 課題一覧用データを取得
            val allMeasuresList = mutableListOf<TaskListData>()
            _tasks.value.forEach { task ->
                val measuresList = realmManager.getMeasuresByTaskID(task.taskID)
                val taskListData = TaskListData(
                    taskID = task.taskID,
                    groupID = task.groupID,
                    title = task.title,
                    measures = measuresList.first().title,
                    order = task.order
                )
                allMeasuresList.add(taskListData)
            }
            _taskLists.value = allMeasuresList
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