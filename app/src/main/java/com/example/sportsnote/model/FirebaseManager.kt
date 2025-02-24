package com.example.sportsnote.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.UUID

object FirebaseManager {
    // ======= Create =======

    /**
     * Firebaseにデータを保存
     *
     * @param collectionName コレクション名
     * @param documentID ドキュメントID
     * @param data 保存するデータ
     */
    private suspend fun saveDocument(
        collectionName: String,
        documentID: String,
        data: Map<String, Any>,
    ) {
        val db = FirebaseFirestore.getInstance()
        try {
            db.collection(collectionName)
                .document(documentID)
                .set(data)
                .await()
        } catch (e: Exception) {
            println("Error writing document: ${e.message}")
        }
    }

    /**
     * FirebaseにGroupを保存
     *
     * @param group Group
     */
    suspend fun saveGroup(group: Group) {
        saveDocument(
            "Group",
            "${group.userID}_${group.groupID}",
            mapOf(
                "userID" to group.userID,
                "groupID" to group.groupID,
                "title" to group.title,
                "color" to group.color,
                "order" to group.order,
                "isDeleted" to group.isDeleted,
                "created_at" to group.created_at,
                "updated_at" to group.updated_at,
            ),
        )
    }

    /**
     * FirebaseにTaskを保存
     *
     * @param task TaskData
     */
    suspend fun saveTask(task: TaskData) {
        saveDocument(
            "Task",
            "${task.userID}_${task.taskID}",
            mapOf(
                "userID" to task.userID,
                "taskID" to task.taskID,
                "groupID" to task.groupID,
                "title" to task.title,
                "cause" to task.cause,
                "order" to task.order,
                "isComplete" to task.isComplete,
                "isDeleted" to task.isDeleted,
                "created_at" to task.created_at,
                "updated_at" to task.updated_at,
            ),
        )
    }

    /**
     * FirebaseにMeasuresを保存
     *
     * @param measures Measures
     */
    suspend fun saveMeasures(measures: Measures) {
        saveDocument(
            "Measures",
            "${measures.userID}_${measures.measuresID}",
            mapOf(
                "userID" to measures.userID,
                "measuresID" to measures.measuresID,
                "taskID" to measures.taskID,
                "title" to measures.title,
                "order" to measures.order,
                "isDeleted" to measures.isDeleted,
                "created_at" to measures.created_at,
                "updated_at" to measures.updated_at,
            ),
        )
    }

    /**
     * FirebaseにMemoを保存
     *
     * @param memo Memo
     */
    suspend fun saveMemo(memo: Memo) {
        saveDocument(
            "Memo",
            "${memo.userID}_${memo.memoID}",
            mapOf(
                "userID" to memo.userID,
                "memoID" to memo.memoID,
                "noteID" to memo.noteID,
                "measuresID" to memo.measuresID,
                "detail" to memo.detail,
                "isDeleted" to memo.isDeleted,
                "created_at" to memo.created_at,
                "updated_at" to memo.updated_at,
            ),
        )
    }

    /**
     * FirebaseにTargetを保存
     *
     * @param target Target
     */
    suspend fun saveTarget(target: Target) {
        saveDocument(
            "Target",
            "${target.userID}_${target.targetID}",
            mapOf(
                "userID" to target.userID,
                "targetID" to target.targetID,
                "title" to target.title,
                "year" to target.year,
                "month" to target.month,
                "isYearlyTarget" to target.isYearlyTarget,
                "isDeleted" to target.isDeleted,
                "created_at" to target.created_at,
                "updated_at" to target.updated_at,
            ),
        )
    }

    /**
     * FirebaseにNoteを保存
     *
     * @param note Note
     */
    suspend fun saveNote(note: Note) {
        saveDocument(
            "Note",
            "${note.userID}_${note.noteID}",
            mapOf(
                "userID" to note.userID,
                "noteID" to note.noteID,
                "noteType" to note.noteType,
                "isDeleted" to note.isDeleted,
                "created_at" to note.created_at,
                "updated_at" to note.updated_at,
                "title" to note.title,
                "date" to note.date,
                "weather" to note.weather,
                "temperature" to note.temperature,
                "condition" to note.condition,
                "reflection" to note.reflection,
                "purpose" to note.purpose,
                "detail" to note.detail,
                "target" to note.target,
                "consciousness" to note.consciousness,
                "result" to note.result,
            ),
        )
    }

    // ======= Select =======

    /**
     * Firebaseから指定したコレクションのデータを全取得し、指定したデータクラスにマッピング
     *
     * @param collection コレクション名
     * @param mapData マッピング処理を行うラムダ
     * @return List<T> マッピング後のデータリスト
     */
    private suspend fun <T> getAllDocuments(
        collection: String,
        mapData: (Map<String, Any>) -> T,
    ): List<T> {
        val db = FirebaseFirestore.getInstance()
        val userID = PreferencesManager.get(PreferencesManager.Keys.USER_ID, UUID.randomUUID().toString())
        val resultList = mutableListOf<T>()

        return try {
            val querySnapshot =
                db.collection(collection)
                    .whereEqualTo("userID", userID)
                    .get()
                    .await()
            for (document in querySnapshot.documents) {
                val data = document.data ?: continue
                val item = mapData(data)
                resultList.add(item)
            }
            resultList
        } catch (e: Exception) {
            println("Error getting documents: ${e.message}")
            emptyList()
        }
    }

    /**
     * FirebaseからGroupを全取得
     *
     * @return List<Group>
     */
    suspend fun getAllGroup(): List<Group> {
        return getAllDocuments("Group") { data ->
            Group().apply {
                userID = data["userID"] as String
                groupID = data["groupID"] as String
                title = data["title"] as String
                color = (data["color"] as Long).toInt()
                order = (data["order"] as Long).toInt()
                isDeleted = data["isDeleted"] as Boolean
                created_at = (data["created_at"] as Timestamp).toDate()
                updated_at = (data["updated_at"] as Timestamp).toDate()
            }
        }
    }

    /**
     * FirebaseからTaskを全取得
     *
     * @return List<TaskData>
     */
    suspend fun getAllTask(): List<TaskData> {
        return getAllDocuments("Task") { data ->
            TaskData().apply {
                userID = data["userID"] as String
                taskID = data["taskID"] as String
                groupID = data["groupID"] as String
                title = data["title"] as String
                cause = data["cause"] as String
                order = (data["order"] as Long).toInt()
                isComplete = data["isComplete"] as Boolean
                isDeleted = data["isDeleted"] as Boolean
                created_at = (data["created_at"] as Timestamp).toDate()
                updated_at = (data["updated_at"] as Timestamp).toDate()
            }
        }
    }

    /**
     * FirebaseからMeasuresを全取得
     *
     * @return List<Measures>
     */
    suspend fun getAllMeasures(): List<Measures> {
        return getAllDocuments("Measures") { data ->
            Measures().apply {
                userID = data["userID"] as String
                measuresID = data["measuresID"] as String
                taskID = data["taskID"] as String
                title = data["title"] as String
                order = (data["order"] as Long).toInt()
                isDeleted = data["isDeleted"] as Boolean
                created_at = (data["created_at"] as Timestamp).toDate()
                updated_at = (data["updated_at"] as Timestamp).toDate()
            }
        }
    }

    /**
     * FirebaseからMemoを全取得
     *
     * @return List<Memo>
     */
    suspend fun getAllMemo(): List<Memo> {
        return getAllDocuments("Memo") { data ->
            Memo().apply {
                userID = data["userID"] as String
                memoID = data["memoID"] as String
                noteID = data["noteID"] as String
                measuresID = data["measuresID"] as String
                detail = data["detail"] as String
                isDeleted = data["isDeleted"] as Boolean
                created_at = (data["created_at"] as Timestamp).toDate()
                updated_at = (data["updated_at"] as Timestamp).toDate()
            }
        }
    }

    /**
     * FirebaseからTargetを全取得
     *
     * @return List<Target>
     */
    suspend fun getAllTarget(): List<Target> {
        return getAllDocuments("Target") { data ->
            Target().apply {
                userID = data["userID"] as String
                targetID = data["targetID"] as String
                title = data["title"] as String
                year = (data["year"] as Long).toInt() // Firestore は数値を Long で取得する可能性があるため Int に変換
                month = (data["month"] as Long).toInt()
                isYearlyTarget = data["isYearlyTarget"] as Boolean
                isDeleted = data["isDeleted"] as Boolean
                created_at = (data["created_at"] as Timestamp).toDate()
                updated_at = (data["updated_at"] as Timestamp).toDate()
            }
        }
    }

    /**
     * FirebaseからNoteを全取得
     *
     * @return List<Note>
     */
    suspend fun getAllNote(): List<Note> {
        return getAllDocuments("Note") { data ->
            Note().apply {
                userID = data["userID"] as String
                noteID = data["noteID"] as String
                noteType = (data["noteType"] as Long).toInt()
                isDeleted = data["isDeleted"] as Boolean
                created_at = (data["created_at"] as Timestamp).toDate()
                updated_at = (data["updated_at"] as Timestamp).toDate()
                title = data["title"] as String
                date = (data["date"] as Timestamp).toDate()
                weather = (data["weather"] as Long).toInt()
                temperature = (data["temperature"] as Long).toInt()
                condition = data["condition"] as String
                reflection = data["reflection"] as String
                purpose = data["purpose"] as String
                detail = data["detail"] as String
                target = data["target"] as String
                consciousness = data["consciousness"] as String
                result = data["result"] as String
            }
        }
    }

    // ======= Update =======

    /**
     * Firebaseから指定したコレクションのデータを更新
     *
     * @param collection コレクション名
     * @param documentID ドキュメントID
     * @param data 更新するデータ
     */
    private fun updateDocument(
        collection: String,
        documentID: String,
        data: Map<String, Any>,
    ) {
        val db = FirebaseFirestore.getInstance()
        val documentRef = db.collection(collection).document(documentID)

        documentRef.update(data)
            .addOnSuccessListener {
                println("$collection document successfully updated")
            }
            .addOnFailureListener { e ->
                println("Error updating $collection document: ${e.message}")
            }
    }

    /**
     * グループを更新
     *
     * @param group グループデータ
     */
    fun updateGroup(group: Group) {
        val userID = PreferencesManager.get(PreferencesManager.Keys.USER_ID, UUID.randomUUID().toString())
        val documentID = "${userID}_${group.groupID}"
        val data =
            mapOf(
                "title" to group.title,
                "color" to group.color,
                "order" to group.order,
                "isDeleted" to group.isDeleted,
                "updated_at" to group.updated_at,
            )
        updateDocument("Group", documentID, data)
    }

    /**
     * 課題を更新
     *
     * @param task 課題データ
     */
    fun updateTask(task: TaskData) {
        val userID = PreferencesManager.get(PreferencesManager.Keys.USER_ID, UUID.randomUUID().toString())
        val documentID = "${userID}_${task.taskID}"
        val data =
            mapOf(
                "groupID" to task.groupID,
                "title" to task.title,
                "cause" to task.cause,
                "order" to task.order,
                "isComplete" to task.isComplete,
                "isDeleted" to task.isDeleted,
                "updated_at" to task.updated_at,
            )
        updateDocument("Task", documentID, data)
    }

    /**
     * 対策を更新
     *
     * @param measures 対策データ
     */
    fun updateMeasures(measures: Measures) {
        val userID = PreferencesManager.get(PreferencesManager.Keys.USER_ID, UUID.randomUUID().toString())
        val documentID = "${userID}_${measures.measuresID}"
        val data =
            mapOf(
                "title" to measures.title,
                "order" to measures.order,
                "isDeleted" to measures.isDeleted,
                "updated_at" to measures.updated_at,
            )
        updateDocument("Measures", documentID, data)
    }

    /**
     * メモを更新
     *
     * @param memo メモデータ
     */
    fun updateMemo(memo: Memo) {
        val userID = PreferencesManager.get(PreferencesManager.Keys.USER_ID, UUID.randomUUID().toString())
        val documentID = "${userID}_${memo.memoID}"
        val data =
            mapOf(
                "detail" to memo.detail,
                "isDeleted" to memo.isDeleted,
                "updated_at" to memo.updated_at,
            )
        updateDocument("Memo", documentID, data)
    }

    /**
     * 目標を更新
     *
     * @param target 目標データ
     */
    fun updateTarget(target: Target) {
        val userID = PreferencesManager.get(PreferencesManager.Keys.USER_ID, UUID.randomUUID().toString())
        val documentID = "${userID}_${target.targetID}"
        val data =
            mapOf(
                "title" to target.title,
                "year" to target.year,
                "month" to target.month,
                "isYearlyTarget" to target.isYearlyTarget,
                "isDeleted" to target.isDeleted,
                "updated_at" to target.updated_at,
            )
        updateDocument("Target", documentID, data)
    }

    /**
     * ノート(フリー、練習、大会)を更新
     *
     * @param note ノートデータ
     */
    fun updateNote(note: Note) {
        val userID = PreferencesManager.get(PreferencesManager.Keys.USER_ID, UUID.randomUUID().toString())
        val documentID = "${userID}_${note.noteID}"
        val data =
            mapOf(
                "isDeleted" to note.isDeleted,
                "updated_at" to note.updated_at,
                "title" to note.title,
                "date" to note.date,
                "weather" to note.weather,
                "temperature" to note.temperature,
                "condition" to note.condition,
                "reflection" to note.reflection,
                "purpose" to note.purpose,
                "detail" to note.detail,
                "target" to note.target,
                "consciousness" to note.consciousness,
                "result" to note.result,
            )
        updateDocument("Note", documentID, data)
    }
}
