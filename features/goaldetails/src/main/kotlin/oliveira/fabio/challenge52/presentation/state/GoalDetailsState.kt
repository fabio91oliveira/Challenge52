package oliveira.fabio.challenge52.presentation.state

import oliveira.fabio.challenge52.domain.model.vo.Item
import oliveira.fabio.challenge52.persistence.model.entity.Week

sealed class GoalDetailsState {
    data class ShowError(val error: Throwable) : GoalDetailsState()
    data class ShowAddedGoals(val itemsList: MutableList<Item>?) : GoalDetailsState()
    data class ShowUpdatedGoal(val hasBeenUpdated: Boolean, val week: Week?) : GoalDetailsState()
    data class ShowRemovedGoal(val hasBeenRemoved: Boolean) : GoalDetailsState()
    data class ShowCompletedGoal(val hasBeenCompleted: Boolean) : GoalDetailsState()
}