package com.example.sportsnote.ui.memo

import androidx.lifecycle.ViewModel
import com.example.sportsnote.model.Memo
import com.example.sportsnote.model.RealmManager
import java.util.UUID

class MemoViewModel : ViewModel() {

    private val realmManager: RealmManager = RealmManager()

    /**
     * メモを保存する
     *
     * @param memoID
     * @param measuresID
     * @param noteID
     * @param detail
     */
    suspend fun saveMemo(
        memoID: String = UUID.randomUUID().toString(),
        measuresID: String,
        noteID: String,
        detail: String
    ): Memo {
        val memo = Memo(
            memoID = memoID,
            measuresID = measuresID,
            noteID = noteID,
            detail = detail
        )
        realmManager.saveItem(memo)
        return memo
    }
}