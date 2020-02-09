package oliveira.fabio.challenge52.home.goalslists.donegoalslist.presentation.action

import androidx.annotation.StringRes
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks

sealed class DoneGoalsActions {
    data class DoneGoalsList(val doneGoalsList: List<GoalWithWeeks>) : DoneGoalsActions()
    object ClearList : DoneGoalsActions()
    object RefreshList : DoneGoalsActions()
    data class ShowMessage(@StringRes val stringRes: Int) : DoneGoalsActions()
    data class Error(val errorMessageRes: Int) : DoneGoalsActions()
}