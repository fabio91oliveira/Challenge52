package oliveira.fabio.challenge52.goal.presentation.action

import androidx.annotation.StringRes
import oliveira.fabio.challenge52.goal.presentation.vo.GoalSuggestion
import oliveira.fabio.challenge52.presentation.vo.GoalToSave

internal sealed class GoalSuggestionsListActions {
    data class SuggestionsList(val suggestions: List<GoalSuggestion>) : GoalSuggestionsListActions()
    data class GoToGoalChooseNameScreen(val goalToSave: GoalToSave) : GoalSuggestionsListActions()
    data class Error(
        @StringRes val titleMessageRes: Int,
        @StringRes val errorMessageRes: Int
    ) :
        GoalSuggestionsListActions()
}