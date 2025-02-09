package com.example.sportsnote.ui.memo

import androidx.lifecycle.ViewModel
import com.example.sportsnote.model.MeasuresMemo
import com.example.sportsnote.model.Memo
import com.example.sportsnote.model.Note
import com.example.sportsnote.model.RealmManager
import java.util.UUID

class MemoViewModel : ViewModel() {

    private val realmManager: RealmManager = RealmManager()

    /**
     * measuresIDに合致するメモを取得
     *
     * @param measuresID 対策ID
     * @return List<Memo>
     */
    fun getMemosByMeasuresID(measuresID: String): List<MeasuresMemo> {
        val measuresMemos = realmManager.getMemosByMeasuresID(measuresID)
        val memoList = mutableListOf<MeasuresMemo>()
        measuresMemos.forEach { memo ->
            val note = realmManager.getObjectById<Note>(memo.noteID) ?: return@forEach
            val measuresMemo = MeasuresMemo(
                memoID = memo.memoID,
                measuresID = memo.measuresID,
                noteID = memo.noteID,
                detail = memo.detail,
                date = note.date
            )
            memoList.add(measuresMemo)
        }
        return memoList
    }

    /**
     * メモを保存する
     *
     * @param memoID
     * @param measuresID
     * @param noteID
     * @param detail
     */
    suspend fun saveMemo(
        memoID: String? = null,
        measuresID: String,
        noteID: String,
        detail: String
    ): Memo {
        val finalMemoID = memoID ?: UUID.randomUUID().toString()
        val memo = Memo(
            memoID = finalMemoID,
            measuresID = measuresID,
            noteID = noteID,
            detail = detail
        )
        realmManager.saveItem(memo)
        return memo
    }

    /**
     * Memoを論理削除
     *
     * @param memoID memoID
     */
    suspend fun deleteMemo(memoID: String) {
        realmManager.logicalDelete<Memo>(memoID)
    }
}