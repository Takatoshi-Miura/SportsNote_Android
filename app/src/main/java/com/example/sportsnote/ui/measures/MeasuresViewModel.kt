package com.example.sportsnote.ui.measures

import androidx.lifecycle.ViewModel
import com.example.sportsnote.model.Measures
import com.example.sportsnote.model.RealmManager
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
     * 対策を保存する
     *
     * @param measuresId 対策ID
     * @param taskId 課題ID
     * @param title 対策タイトル
     */
    suspend fun saveMeasures(
        measuresId: String = UUID.randomUUID().toString(),
        taskId: String,
        title: String
    ): Measures {
        val measures = Measures(
            measuresId = measuresId,
            taskId = taskId,
            title = title
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