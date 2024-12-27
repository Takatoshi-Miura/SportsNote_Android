package com.example.sportsnote.ui.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sportsnote.model.Group
import com.example.sportsnote.model.RealmManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GroupViewModel : ViewModel() {

    private val realmManager: RealmManager = RealmManager()
    private val _groups = MutableStateFlow<List<Group>>(emptyList())
    val groups: StateFlow<List<Group>> = _groups

    init {
        loadData()
    }

    /**
     * 課題一覧のGroupデータをロード
     */
    fun loadData() {
        viewModelScope.launch {
            _groups.value = realmManager.getDataList(Group::class.java)
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
     * Groupを保存(orderは最後尾固定)
     *
     * @param title タイトル
     * @param colorId カラーID
     */
    suspend fun saveGroup(
        title: String,
        colorId: Int
    ) {
        val count = realmManager.getCount(Group::class.java)
        val group = Group(
            title = title,
            colorId = colorId,
            order = count
        )
        realmManager.saveItem(group)
    }

}