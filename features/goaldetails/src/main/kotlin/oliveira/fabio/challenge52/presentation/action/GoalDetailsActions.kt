package oliveira.fabio.challenge52.presentation.action

import oliveira.fabio.challenge52.domain.model.Week
import oliveira.fabio.challenge52.presentation.adapter.vo.AdapterItem

internal sealed class GoalDetailsActions {
    data class AddedGoals(val list: MutableList<AdapterItem<String, Week>>) :
        GoalDetailsActions()

    data class UpdatedGoal(val week: Week) : GoalDetailsActions()
    object RemovedGoal : GoalDetailsActions()
    object CompletedGoal : GoalDetailsActions()
    data class Error(val errorMessageRes: Int) : GoalDetailsActions()
}