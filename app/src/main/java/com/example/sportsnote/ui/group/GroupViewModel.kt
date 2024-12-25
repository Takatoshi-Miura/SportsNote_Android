package com.example.sportsnote.ui.group

import androidx.lifecycle.ViewModel
import com.example.sportsnote.model.Group
import com.example.sportsnote.model.RealmManager

class GroupViewModel : ViewModel() {

    private val realmManager: RealmManager = RealmManager()

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