package oliveira.fabio.challenge52.goal.presentation.action

import androidx.annotation.StringRes

internal sealed class CreateGoalActions {
    object GoalCreated : CreateGoalActions()

    data class CriticalError(
        @StringRes val titleRes: Int,
        @StringRes val descriptionRes: Int
    ) : CreateGoalActions()
}