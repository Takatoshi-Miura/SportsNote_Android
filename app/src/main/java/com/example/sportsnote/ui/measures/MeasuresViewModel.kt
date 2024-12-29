package com.example.sportsnote.ui.measures

import androidx.lifecycle.ViewModel
import com.example.sportsnote.model.Measures
import com.example.sportsnote.model.RealmManager
import java.util.UUID

class MeasuresViewModel : ViewModel() {

    private val realmManager: RealmManager = RealmManager()


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
    ) {
        val measures = Measures(
            measuresId = measuresId,
            taskId = taskId,
            title = title
        )
        realmManager.saveItem(measures)
    }
}