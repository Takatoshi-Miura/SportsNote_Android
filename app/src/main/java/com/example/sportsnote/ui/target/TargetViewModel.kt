package com.example.sportsnote.ui.target

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import com.example.sportsnote.model.RealmManager
import com.example.sportsnote.model.Target
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Date

class TargetViewModel : ViewModel() {

    private val realmManager: RealmManager = RealmManager()

    // カレンダーに表示している年月の目標を保持
    private val _targets = mutableStateOf<List<Target>>(emptyList())
    val targets: State<List<Target>> = _targets

    // 1つの年間目標を保持するステートフロー
    private val _yearlyTarget = MutableStateFlow<Target?>(null)
    val yearlyTarget: StateFlow<Target?> get() = _yearlyTarget

    // 1つの月間目標を保持するステートフロー
    private val _monthlyTarget = MutableStateFlow<Target?>(null)
    val monthlyTarget: StateFlow<Target?> get() = _monthlyTarget

    /**
     * 表示している年月に対応するTargetを取得
     *
     * @param year 表示中の年
     * @param month 表示中の月
     */
    fun getTargetByYearMonth(year: Int, month: Int) {
        val fetchedTargets = realmManager.fetchTargetsByYearMonth(year, month)
        _targets.value = fetchedTargets

        // 年間目標をフィルタして取得
        val yearlyTarget = fetchedTargets.firstOrNull { it.isYearlyTarget }
        _yearlyTarget.value = yearlyTarget

        // 月間目標をフィルタして取得
        val monthlyTarget = fetchedTargets.firstOrNull { !it.isYearlyTarget }
        _monthlyTarget.value = monthlyTarget
    }

    /**
     * Targetを保存
     *
     * @param title タイトル
     * @param year 年
     * @param month 月
     * @param isYearlyTarget 年間目標フラグ
     * @param created_at 作成日付
     * @return targetID
     */
    suspend fun saveTarget(
        title: String,
        year: Int,
        month: Int,
        isYearlyTarget: Boolean,
        created_at: Date = Date()
    ): String {
        // 重複する目標を削除
        val fetchedTargets = realmManager.fetchTargetsByYearMonth(year, month)
        if (isYearlyTarget) {
            val yearlyTarget = fetchedTargets.firstOrNull { it.isYearlyTarget }
            if (yearlyTarget != null) {
                realmManager.logicalDelete<Target>(yearlyTarget.targetID)
            }
        } else {
            val monthlyTarget = fetchedTargets.firstOrNull { !it.isYearlyTarget }
            if (monthlyTarget != null) {
                realmManager.logicalDelete<Target>(monthlyTarget.targetID)
            }
        }

        // 保存
        val target = Target()
        target.title = title
        target.year = year
        target.month = month
        target.isYearlyTarget = isYearlyTarget
        target.created_at = created_at
        realmManager.saveItem(target)
        return target.targetID
    }
}