package oliveira.fabio.challenge52.presentation.state

import oliveira.fabio.challenge52.domain.model.vo.Item
import oliveira.fabio.challenge52.persistence.model.entity.Week

sealed class GoalDetailsAction {
    data class ShowError(val error: Throwable) : GoalDetailsAction()
    data class ShowAddedGoalsFirstTime(val itemsList: MutableList<Item>?) : GoalDetailsAction()
    data class ShowAddedGoals(val itemsList: MutableList<Item>?) : GoalDetailsAction()
    data class ShowUpdatedGoal(val hasBeenUpdated: Boolean, val week: Week?) : GoalDetailsAction()
    data class ShowRemovedGoal(val hasBeenRemoved: Boolean) : GoalDetailsAction()
    data class ShowCompletedGoal(val hasBeenCompleted: Boolean) : GoalDetailsAction()
}