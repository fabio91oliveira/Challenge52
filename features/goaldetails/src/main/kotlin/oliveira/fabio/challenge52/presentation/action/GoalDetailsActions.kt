package oliveira.fabio.challenge52.presentation.action

import androidx.annotation.StringRes

internal sealed class GoalDetailsActions {
    object UpdateDetails : GoalDetailsActions()
    data class UpdateDetailsWithPosition(val position: Int) : GoalDetailsActions()
    data class ShowConfirmationMessage(@StringRes val stringRes: Int) : GoalDetailsActions()
    object RemoveGoal : GoalDetailsActions()
    object CompleteGoal : GoalDetailsActions()
    data class CriticalError(
        @StringRes val titleRes: Int,
        @StringRes val descriptionRes: Int
    ) : GoalDetailsActions()
}