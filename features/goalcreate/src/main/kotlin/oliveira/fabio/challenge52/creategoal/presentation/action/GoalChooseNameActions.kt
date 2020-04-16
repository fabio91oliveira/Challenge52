package oliveira.fabio.challenge52.creategoal.presentation.action

import androidx.annotation.StringRes
import oliveira.fabio.challenge52.presentation.vo.GoalToSave

internal sealed class GoalChooseNameActions {
    data class GoToFinalStepScreen(val goalToSave: GoalToSave) : GoalChooseNameActions()
    data class GoToDefinePeriodScreen(val goalToSave: GoalToSave) : GoalChooseNameActions()
    data class SetNameGoal(val goalName: String) : GoalChooseNameActions()
    data class CriticalError(
        @StringRes val titleRes: Int,
        @StringRes val descriptionRes: Int
    ) :
        GoalChooseNameActions()
}