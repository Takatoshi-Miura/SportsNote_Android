package com.example.sportsnote.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.sportsnote.R

/**
 * 画面の設定情報
 *
 * @param topBarTitle TopBarのタイトル
 * @param showBottomBar BottomBarを表示するかどうか
 */
data class ScreenConfig(
    val topBarTitle: String,
    val showBottomBar: Boolean
)

sealed class Screen(val route: String) {
    object Task : Screen("task")
    object GroupView : Screen("group_view_screen/{groupId}")
    object TaskDetail : Screen("detail_task/{taskId}")
    object Measures : Screen("measures/{measuresId}")
    object CompletedTask : Screen("completed_task/{groupId}")
    object Note : Screen("note")
    object TournamentNoteView : Screen("tournament_note_view/{noteId}")
    object FreeNoteView : Screen("free_note_view/{noteId}")
    object Target : Screen("target")

    // 各画面に対応する設定を定義
    @Composable
    fun getConfig(): ScreenConfig {
        return when (this) {
            is Task -> ScreenConfig(stringResource(R.string.task), showBottomBar = true)
            is GroupView -> ScreenConfig(stringResource(R.string.groupView), showBottomBar = false)
            is TaskDetail -> ScreenConfig(stringResource(R.string.taskDetail), showBottomBar = false)
            is Measures -> ScreenConfig(stringResource(R.string.measuresDetail), showBottomBar = false)
            is CompletedTask -> ScreenConfig(stringResource(R.string.completedTask), showBottomBar = true)
            is Note -> ScreenConfig(stringResource(R.string.note), showBottomBar = true)
            is TournamentNoteView -> ScreenConfig(stringResource(R.string.noteDetail), showBottomBar = false)
            is FreeNoteView -> ScreenConfig(stringResource(R.string.freeNote), showBottomBar = false)
            is Target -> ScreenConfig(stringResource(R.string.target), showBottomBar = true)
        }
    }

    companion object {
        // currentRouteから対応するScreenを取得するメソッド
        fun fromRoute(route: String): Screen {
            return when {
                route.startsWith(Task.route) -> Task
                route.startsWith(GroupView.route) -> GroupView
                route.startsWith(TaskDetail.route) -> TaskDetail
                route.startsWith(Measures.route) -> Measures
                route.startsWith(CompletedTask.route) -> CompletedTask
                route.startsWith(Note.route) -> Note
                route.startsWith(TournamentNoteView.route) -> TournamentNoteView
                route.startsWith(FreeNoteView.route) -> FreeNoteView
                route.startsWith(Target.route) -> Target
                else -> Task // デフォルトはTaskに戻る
            }
        }
    }
}

