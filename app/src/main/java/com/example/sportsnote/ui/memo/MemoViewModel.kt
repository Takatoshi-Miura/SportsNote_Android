package com.example.sportsnote.ui.memo

import androidx.lifecycle.ViewModel
import com.example.sportsnote.model.MeasuresMemo
import com.example.sportsnote.model.Memo
import com.example.sportsnote.model.Note
import com.example.sportsnote.model.RealmManager
import java.util.Date
import java.util.UUID

class MemoViewModel : ViewModel() {
    private val realmManager: RealmManager = RealmManager.getInstance()

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
            val measuresMemo =
                MeasuresMemo(
                    memoID = memo.memoID,
                    measuresID = memo.measuresID,
                    noteID = memo.noteID,
                    detail = memo.detail,
                    date = note.date,
                )
            memoList.add(measuresMemo)
        }
        return memoList.sortedByDescending { it.date }
    }

    /**
     * メモを保存する
     *
     * @param memoID
     * @param measuresID
     * @param noteID
     * @param detail
     * @param created_at
     */
    suspend fun saveMemo(
        memoID: String? = null,
        measuresID: String,
        noteID: String,
        detail: String,
        created_at: Date = Date(),
    ): Memo {
        val finalMemoID = memoID ?: UUID.randomUUID().toString()
        val memo =
            Memo(
                memoID = finalMemoID,
                measuresID = measuresID,
                noteID = noteID,
                detail = detail,
                created_at = created_at,
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
