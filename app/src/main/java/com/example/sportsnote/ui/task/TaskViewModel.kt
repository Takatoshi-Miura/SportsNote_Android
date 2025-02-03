package com.example.sportsnote.ui.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportsnote.model.Group
import com.example.sportsnote.model.RealmManager
import com.example.sportsnote.model.TaskData
import com.example.sportsnote.model.TaskDetailData
import com.example.sportsnote.model.TaskListData
import com.example.sportsnote.utils.Color
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
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadData()
    }

    /**
     * 課題一覧データをロード
     */
    fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            _tasks.value = realmManager.getDataList(TaskData::class.java)

            // 課題一覧用データを取得
            val taskListDatas = mutableListOf<TaskListData>()
            _tasks.value.forEach { task ->
                val taskListData = convertTaskDataToTaskListData(task)
                taskListDatas.add(taskListData)
            }
            _taskLists.value = taskListDatas
            _isLoading.value = false
        }
    }

    /**
     * TaskDataをTaskListDataに変換
     *
     * @param task TaskData
     * @return TaskListData
     */
    private fun convertTaskDataToTaskListData(task: TaskData): TaskListData {
        // 最優先の対策を取得
        val measuresList = realmManager.getMeasuresByTaskID(task.taskID)
        val (measuresTitle, measuresID) = if (measuresList.isNotEmpty()) {
            measuresList.first().let { it.title to it.measuresID }
        } else {
            "" to "" // 空の値を設定
        }

        // グループカラーを取得
        val group = realmManager.getObjectById<Group>(task.groupID) ?: Group()

        return TaskListData(
            taskID = task.taskID,
            groupID = task.groupID,
            groupColor = Color.fromInt(group.color).toComposeColor(),
            title = task.title,
            measuresID = measuresID,
            measures = measuresTitle,
            order = task.order
        )
    }

    /**
     * groupIDに合致する完了した課題を取得
     *
     * @param groupID groupID
     * @return List<TaskListData>
     */
    fun getCompletedTasksByGroupId(
        groupID: String
    ): List<TaskListData> {
        val taskListDatas = mutableListOf<TaskListData>()
        val tasks = realmManager.getCompletedTasksByGroupId(groupID)
        tasks.forEach { task ->
            val taskListData = convertTaskDataToTaskListData(task)
            taskListDatas.add(taskListData)
        }
        return taskListDatas
    }

    /**
     * taskIDに合致する課題を取得
     *
     * @param taskID taskID
     * @return TaskDetailData
     */
    fun getTaskByTaskId(
        taskID: String
    ): TaskDetailData {
        val taskData = realmManager.getObjectById<TaskData>(taskID)
        val measuresList = realmManager.getMeasuresByTaskID(taskID)
        return TaskDetailData(
            task = taskData!!,
            measuresList = measuresList
        )
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

    /**
     * TaskDataを論理削除
     *
     * @param taskID taskID
     */
    suspend fun deleteTaskData(taskID: String) {
        realmManager.logicalDelete<TaskData>(taskID)
    }
}