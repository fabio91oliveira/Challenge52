package oliveira.fabio.challenge52.presentation.action

import oliveira.fabio.challenge52.domain.model.Week
import oliveira.fabio.challenge52.presentation.adapter.vo.AdapterItem
import oliveira.fabio.challenge52.presentation.vo.TopDetails

internal sealed class GoalDetailsActions {
    data class PopulateGoalInformation(val list: MutableList<AdapterItem<TopDetails, String, Week>>) :
        GoalDetailsActions()

    data class UpdateWeek(val week: Week) : GoalDetailsActions()
    data class UpdateTopDetails(val topDetails: TopDetails) : GoalDetailsActions()
    object RemoveGoal : GoalDetailsActions()
    object CompleteGoal : GoalDetailsActions()
    data class CriticalError(val errorMessageRes: Int) : GoalDetailsActions()
}