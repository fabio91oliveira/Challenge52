package oliveira.fabio.challenge52.goalcreate.presentation.action

import androidx.annotation.StringRes
import oliveira.fabio.challenge52.goalcreate.domain.vo.GoalSuggestion

internal sealed class GoalCreateNameSuggestionsActions {
    data class Suggestions(val suggestions: List<GoalSuggestion>) : GoalCreateNameSuggestionsActions()
    data class Error(
        @StringRes val titleMessageRes: Int,
        @StringRes val errorMessageRes: Int
    ) :
        GoalCreateNameSuggestionsActions()
}