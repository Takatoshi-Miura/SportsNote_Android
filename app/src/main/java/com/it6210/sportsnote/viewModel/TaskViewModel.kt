package com.it6210.sportsnote.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.it6210.sportsnote.model.Group
import com.it6210.sportsnote.model.TaskData
import com.it6210.sportsnote.model.TaskDetailData
import com.it6210.sportsnote.model.TaskListData
import com.it6210.sportsnote.model.manager.FirebaseManager
import com.it6210.sportsnote.model.manager.PreferencesManager
import com.it6210.sportsnote.model.manager.RealmManager
import com.it6210.sportsnote.utils.Color
import com.it6210.sportsnote.utils.Network
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class TaskViewModel : ViewModel() {
    private val realmManager: RealmManager = RealmManager.getInstance()
    private val _tasks = MutableStateFlow<List<TaskData>>(emptyList())
    val tasks: StateFlow<List<TaskData>> = _tasks
    private val _taskLists = MutableStateFlow<List<TaskListData>>(emptyList())
    val taskLists: StateFlow<List<TaskListData>> = _taskLists
    private val _taskListsForNote = MutableStateFlow<List<TaskListData>>(emptyList())
    val taskListsForNote: StateFlow<List<TaskListData>> = _taskListsForNote
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

            // 課題一覧用データ,練習ノート用データを取得
            val taskListDatas = mutableListOf<TaskListData>()
            val taskListForPracticeNote = mutableListOf<TaskListData>()
            _tasks.value.forEach { task ->
                if (task.isComplete) return@forEach
                val taskListData = convertTaskDataToTaskListData(task)
                taskListDatas.add(taskListData)

                // 練習ノートには対策が一つ1以上存在する課題のみ連携
                if (taskListData.measures.isBlank()) return@forEach
                taskListForPracticeNote.add(taskListData)
            }
            _taskLists.value = taskListDatas
            _taskListsForNote.value = taskListForPracticeNote
            _isLoading.value = false
        }
    }

    /**
     * TaskDataをTaskListDataに変換
     *
     * @param task TaskData
     * @return TaskListData
     */
    fun convertTaskDataToTaskListData(task: TaskData): TaskListData {
        // 最優先の対策を取得
        val measuresList = realmManager.getMeasuresByTaskID(task.taskID)
        val (measuresTitle, measuresID) =
            if (measuresList.isNotEmpty()) {
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
            memoID = null,
            order = task.order,
        )
    }

    /**
     * groupIDに合致する完了した課題を取得
     *
     * @param groupID groupID
     * @return List<TaskListData>
     */
    fun getCompletedTasksByGroupId(groupID: String): List<TaskListData> {
        val tasks = realmManager.getCompletedTasksByGroupId(groupID)
        return tasks.map { task -> convertTaskDataToTaskListData(task) }
    }

    /**
     * taskIDに合致する課題を取得
     *
     * @param taskID taskID
     * @return TaskDetailData
     */
    fun getTaskByTaskId(taskID: String): TaskDetailData {
        val taskData = realmManager.getObjectById<TaskData>(taskID)
        val measuresList = realmManager.getMeasuresByTaskID(taskID)
        return TaskDetailData(
            task = taskData!!,
            measuresList = measuresList,
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
     * @param isComplete 完了状態
     */
    suspend fun saveTask(
        taskId: String? = null,
        title: String,
        cause: String,
        groupId: String,
        isComplete: Boolean = false,
        created_at: Date = Date(),
    ): String {
        val finalTaskId = taskId ?: UUID.randomUUID().toString()
        val isUpdate = taskId != null
        val task =
            TaskData(
                taskId = finalTaskId,
                title = title,
                cause = cause,
                groupId = groupId,
                isComplete = isComplete,
                created_at = created_at,
            )
        realmManager.saveItem(task)

        // Firebaseに反映
        if (!Network.isOnline()) return finalTaskId
        if (!PreferencesManager.get(PreferencesManager.Keys.IS_LOGIN, false)) {
            return finalTaskId
        }
        if (isUpdate) {
            FirebaseManager.updateTask(task)
        } else {
            FirebaseManager.saveTask(task)
        }
        return finalTaskId
    }

    /**
     * TaskDataを論理削除
     *
     * @param taskID taskID
     */
    suspend fun deleteTaskData(taskID: String) {
        realmManager.logicalDelete<TaskData>(taskID)

        // Firebaseに反映
        if (!Network.isOnline()) return
        if (!PreferencesManager.get(PreferencesManager.Keys.IS_LOGIN, false)) {
            return
        }
        val deletedTask = realmManager.getObjectById<TaskData>(taskID) ?: return
        FirebaseManager.updateTask(deletedTask)
    }
}
