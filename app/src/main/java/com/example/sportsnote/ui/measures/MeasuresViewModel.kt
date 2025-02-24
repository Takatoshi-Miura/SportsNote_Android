package com.example.sportsnote.ui.measures

import androidx.lifecycle.ViewModel
import com.example.sportsnote.model.Measures
import com.example.sportsnote.model.RealmManager
import java.util.Date
import java.util.UUID

class MeasuresViewModel : ViewModel() {
    private val realmManager: RealmManager = RealmManager()

    /**
     * 対策を取得（measuresID指定）
     *
     * @param measuresID measuresID
     * @return `Measures`オブジェクト。存在しない場合やエラーが発生した場合は`null`
     */
    fun getMeasuresById(measuresID: String): Measures? {
        return realmManager.getObjectById<Measures>(measuresID)
    }

    /**
     * 対策を取得（taskID指定）
     *
     * @param taskID taskID
     * @return List<Measures>
     */
    fun getMeasuresByTaskID(taskID: String): List<Measures> {
        return realmManager.getMeasuresByTaskID(taskID)
    }

    /**
     * 対策を保存する
     *
     * @param measuresId 対策ID
     * @param taskId 課題ID
     * @param title 対策タイトル
     * @param order 並び順
     * @param created_at 作成日付
     */
    suspend fun saveMeasures(
        measuresId: String = UUID.randomUUID().toString(),
        taskId: String,
        title: String,
        order: Int = getMeasuresByTaskID(taskId).size,
        created_at: Date = Date(),
    ): Measures {
        val measures =
            Measures(
                measuresId = measuresId,
                taskId = taskId,
                title = title,
                order = order,
                created_at = created_at,
            )
        realmManager.saveItem(measures)
        return measures
    }

    /**
     * 対策を論理削除
     *
     * @param measuresID measuresID
     */
    suspend fun deleteMeasures(measuresID: String) {
        realmManager.logicalDelete<Measures>(measuresID)
    }
}
