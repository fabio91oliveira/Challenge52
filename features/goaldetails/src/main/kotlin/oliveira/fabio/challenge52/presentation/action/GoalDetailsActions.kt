package oliveira.fabio.challenge52.presentation.action

import oliveira.fabio.challenge52.domain.model.vo.Item
import oliveira.fabio.challenge52.persistence.model.entity.Week

sealed class GoalDetailsActions {
    data class ShowAddedGoalsFirstTime(val itemsList: MutableList<Item>) : GoalDetailsActions()
    data class ShowAddedGoals(val itemsList: MutableList<Item>) : GoalDetailsActions()
    data class ShowUpdatedGoal(val week: Week) : GoalDetailsActions()
    object ShowRemovedGoal : GoalDetailsActions()
    object ShowCompletedGoal : GoalDetailsActions()
    data class ShowError(val errorMessageRes: Int) : GoalDetailsActions()
}