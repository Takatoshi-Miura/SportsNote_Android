package com.example.sportsnote.ui.target

import androidx.lifecycle.ViewModel
import com.example.sportsnote.model.RealmManager
import com.example.sportsnote.model.Target

class TargetViewModel : ViewModel() {

    private val realmManager: RealmManager = RealmManager()

    /**
     * Targetを保存
     *
     * @param title タイトル
     * @param year 年
     * @param month 月
     * @param isYearlyTarget 年間目標フラグ
     * @return targetID
     */
    suspend fun saveTarget(
        title: String,
        year: Int,
        month: Int,
        isYearlyTarget: Boolean
    ): String {
        // TODO: 重複する目標を取得して、そのtargetIDをセットして上書き
        val target = Target()
        target.title = title
        target.year = year
        target.month = month
        target.isYearlyTarget = isYearlyTarget
        realmManager.saveItem(target)
        return target.targetID
    }
}