package oliveira.fabio.challenge52.creategoal.presentation.action

import androidx.annotation.StringRes
import oliveira.fabio.challenge52.creategoal.presentation.vo.MoneySuggestion

internal sealed class CreateGoalActions {
    data class ShowMoneySuggestions(val moneySuggestions: List<MoneySuggestion>) :
        CreateGoalActions()

    object GoalCreated : CreateGoalActions()

    data class CriticalError(
        @StringRes val titleRes: Int,
        @StringRes val descriptionRes: Int
    ) : CreateGoalActions()
}