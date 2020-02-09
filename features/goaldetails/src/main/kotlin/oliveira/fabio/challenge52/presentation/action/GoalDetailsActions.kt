package oliveira.fabio.challenge52.presentation.action

import oliveira.fabio.challenge52.domain.model.vo.Item
import oliveira.fabio.challenge52.persistence.model.entity.Week

sealed class GoalDetailsActions {
    data class AddedGoalsFirstTime(val itemsList: MutableList<Item>) : GoalDetailsActions()
    data class AddedGoals(val itemsList: MutableList<Item>) : GoalDetailsActions()
    data class UpdatedGoal(val week: Week) : GoalDetailsActions()
    object RemovedGoal : GoalDetailsActions()
    object CompletedGoal : GoalDetailsActions()
    data class Error(val errorMessageRes: Int) : GoalDetailsActions()
}