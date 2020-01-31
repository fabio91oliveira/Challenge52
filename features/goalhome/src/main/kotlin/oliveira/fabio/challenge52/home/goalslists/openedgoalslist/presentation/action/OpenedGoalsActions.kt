package oliveira.fabio.challenge52.home.goalslists.openedgoalslist.presentation.action

import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks

sealed class OpenedGoalsActions {
    data class OpenedGoalsList(val openedGoalsList: List<GoalWithWeeks>) : OpenedGoalsActions()
    object ClearList : OpenedGoalsActions()
    data class ShowRemoveConfirmationDialog(
        @PluralsRes val pluralRes: Int,
        val doneGoalsRemovedSize: Int
    ) :
        OpenedGoalsActions()

    data class ShowMessage(@StringRes val stringRes: Int) : OpenedGoalsActions()
    object RefreshList : OpenedGoalsActions()
    data class Error(val errorMessageRes: Int) : OpenedGoalsActions()
}