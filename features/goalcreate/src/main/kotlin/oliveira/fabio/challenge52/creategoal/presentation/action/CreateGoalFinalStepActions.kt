package oliveira.fabio.challenge52.creategoal.presentation.action

import androidx.annotation.StringRes
import oliveira.fabio.challenge52.creategoal.presentation.vo.MoneySuggestion

internal sealed class CreateGoalFinalStepActions {
    data class ShowMoneySuggestions(val moneySuggestions: List<MoneySuggestion>) :
        CreateGoalFinalStepActions()

    object GoalCreated : CreateGoalFinalStepActions()

    data class CriticalError(
        @StringRes val titleRes: Int,
        @StringRes val descriptionRes: Int
    ) : CreateGoalFinalStepActions()
}