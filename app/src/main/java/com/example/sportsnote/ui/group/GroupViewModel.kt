package com.example.sportsnote.ui.group

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportsnote.R
import com.example.sportsnote.model.Group
import com.example.sportsnote.model.RealmManager
import com.example.sportsnote.utils.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class GroupViewModel : ViewModel() {
    private val realmManager: RealmManager = RealmManager()
    private val _groups = MutableStateFlow<List<Group>>(emptyList())
    val groups: StateFlow<List<Group>> = _groups
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadData()
    }

    /**
     * 課題一覧のGroupデータをロード
     */
    fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            _groups.value = realmManager.getDataList(Group::class.java)
            _isLoading.value = false
        }
    }

    /**
     * 指定された`groupId`に基づいて、`Group`オブジェクトを取得
     *
     * @param groupId groupId
     * @return `groupId`に一致する`Group`オブジェクト。存在しない場合やエラーが発生した場合は`null`
     */
    fun getGroupById(groupId: String): Group? {
        return realmManager.getObjectById<Group>(groupId)
    }

    /**
     * Groupの件数を取得
     *
     * @return Groupの件数(削除済みデータを含まない)
     */
    fun getGroupCount(): Int {
        return realmManager.getCount(Group::class.java)
    }

    /**
     * 未分類グループを作成
     *
     * @param context Context
     */
    suspend fun createUncategorizedGroup(context: Context) {
        if (getGroupCount() > 0) return
        saveGroup(
            title = context.getString(R.string.Uncategorized),
            colorId = Color.GRAY.id,
            order = 0,
        )
    }

    /**
     * Groupを保存(orderは最後尾固定)
     *
     * @param groupId グループID
     * @param title タイトル
     * @param colorId カラーID
     * @param order 並び順
     * @param created_at 作成日付
     * @return 保存したGroupオブジェクト
     */
    suspend fun saveGroup(
        groupId: String = UUID.randomUUID().toString(),
        title: String,
        colorId: Int,
        order: Int?,
        created_at: Date = Date(),
    ): Group {
        val group =
            Group(
                groupId = groupId,
                title = title,
                colorId = colorId,
                order = order ?: realmManager.getCount(Group::class.java),
                created_at = created_at,
            )
        realmManager.saveItem(group)
        return group
    }

    /**
     * Groupを論理削除
     *
     * @param groupId groupId
     */
    suspend fun deleteGroup(groupId: String) {
        realmManager.logicalDelete<Group>(groupId)
    }
}
