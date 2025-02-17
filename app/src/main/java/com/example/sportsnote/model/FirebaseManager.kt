package com.example.sportsnote.model

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirebaseManager {

    // ======= Create =======

    /**
     * FirebaseにGroupを保存
     *
     * @param group Group
     */
    suspend fun saveGroup(group: Group) {
        val db = FirebaseFirestore.getInstance()
        try {
            db.collection("Group")
                .document("${group.userID}_${group.groupID}")
                .set(
                    mapOf(
                        "userID"     to group.userID,
                        "groupID"    to group.groupID,
                        "title"      to group.title,
                        "color"      to group.color,
                        "order"      to group.order,
                        "isDeleted"  to group.isDeleted,
                        "created_at" to group.created_at,
                        "updated_at" to group.updated_at
                    )
                ).await()
        } catch (e: Exception) {
            println("Error writing document: ${e.message}")
        }
    }

    /**
     * FirebaseにTaskを保存
     *
     * @param task TaskData
     */
    suspend fun saveTask(task: TaskData) {
        val db = FirebaseFirestore.getInstance()
        try {
            db.collection("Task")
                .document("${task.userID}_${task.taskID}")
                .set(
                    mapOf(
                        "userID"      to task.userID,
                        "taskID"      to task.taskID,
                        "groupID"     to task.groupID,
                        "title"       to task.title,
                        "cause"       to task.cause,
                        "order"       to task.order,
                        "isComplete"  to task.isComplete,
                        "isDeleted"   to task.isDeleted,
                        "created_at"  to task.created_at,
                        "updated_at"  to task.updated_at
                    )
                ).await()
        } catch (e: Exception) {
            println("Error writing document: ${e.message}")
        }
    }

    /**
     * FirebaseにMeasuresを保存
     *
     * @param measures Measures
     */
    suspend fun saveMeasures(measures: Measures) {
        val db = FirebaseFirestore.getInstance()
        try {
            db.collection("Measures")
                .document("${measures.userID}_${measures.measuresID}")
                .set(
                    mapOf(
                        "userID"      to measures.userID,
                        "measuresID"  to measures.measuresID,
                        "taskID"      to measures.taskID,
                        "title"       to measures.title,
                        "order"       to measures.order,
                        "isDeleted"   to measures.isDeleted,
                        "created_at"  to measures.created_at,
                        "updated_at"  to measures.updated_at
                    )
                ).await()
        } catch (e: Exception) {
            println("Error writing document: ${e.message}")
        }
    }

    /**
     * FirebaseにMemoを保存
     *
     * @param memo Memo
     */
    suspend fun saveMemo(memo: Memo) {
        val db = FirebaseFirestore.getInstance()
        try {
            db.collection("Memo")
                .document("${memo.userID}_${memo.memoID}")
                .set(
                    mapOf(
                        "userID"      to memo.userID,
                        "memoID"      to memo.memoID,
                        "noteID"      to memo.noteID,
                        "measuresID"  to memo.measuresID,
                        "detail"      to memo.detail,
                        "isDeleted"   to memo.isDeleted,
                        "created_at"  to memo.created_at,
                        "updated_at"  to memo.updated_at
                    )
                ).await()
        } catch (e: Exception) {
            println("Error writing document: ${e.message}")
        }
    }

    /**
     * FirebaseにTargetを保存
     *
     * @param target Target
     */
    suspend fun saveTarget(target: Target) {
        val db = FirebaseFirestore.getInstance()
        try {
            db.collection("Target")
                .document("${target.userID}_${target.targetID}")
                .set(
                    mapOf(
                        "userID"         to target.userID,
                        "targetID"       to target.targetID,
                        "title"          to target.title,
                        "year"           to target.year,
                        "month"          to target.month,
                        "isYearlyTarget" to target.isYearlyTarget,
                        "isDeleted"      to target.isDeleted,
                        "created_at"     to target.created_at,
                        "updated_at"     to target.updated_at
                    )
                ).await()
        } catch (e: Exception) {
            println("Error writing document: ${e.message}")
        }
    }

    /**
     * FirebaseにNoteを保存
     *
     * @param note Note
     */
    suspend fun saveNote(note: Note) {
        val db = FirebaseFirestore.getInstance()
        try {
            db.collection("Note")
                .document("${note.userID}_${note.noteID}")
                .set(
                    mapOf(
                        "userID"        to note.userID,
                        "noteID"        to note.noteID,
                        "noteType"      to note.noteType,
                        "isDeleted"     to note.isDeleted,
                        "created_at"    to note.created_at,
                        "updated_at"    to note.updated_at,
                        "title"         to note.title,
                        "date"          to note.date,
                        "weather"       to note.weather,
                        "temperature"   to note.temperature,
                        "condition"     to note.condition,
                        "reflection"    to note.reflection,
                        "purpose"       to note.purpose,
                        "detail"        to note.detail,
                        "target"        to note.target,
                        "consciousness" to note.consciousness,
                        "result"        to note.result
                    )
                ).await()
        } catch (e: Exception) {
            println("Error writing document: ${e.message}")
        }
    }

    // ======= Select =======

}