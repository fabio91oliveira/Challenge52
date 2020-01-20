package oliveira.fabio.challenge52.presentation.state

import oliveira.fabio.challenge52.domain.model.vo.Item
import oliveira.fabio.challenge52.persistence.model.entity.Week

sealed class GoalDetailsAction {
    data class ShowAddedGoalsFirstTime(val itemsList: MutableList<Item>) : GoalDetailsAction()
    data class ShowAddedGoals(val itemsList: MutableList<Item>) : GoalDetailsAction()
    data class ShowUpdatedGoal(val week: Week) : GoalDetailsAction()
    object ShowRemovedGoal : GoalDetailsAction()
    object ShowCompletedGoal : GoalDetailsAction()
    data class ShowError(val errorMessageRes: Int) : GoalDetailsAction()

    data class ShowConfirmationDialogDoneGoal(val messageRes: Int) :
        GoalDetailsAction()

    data class ShowCantMoveToDoneDialog(val messageRes: Int) : GoalDetailsAction()
    object ShowConfirmationDialogRemoveGoal : GoalDetailsAction()
    data class ShowConfirmationDialogWeekIsPosterior(val isPosterior: Boolean) : GoalDetailsAction()
}