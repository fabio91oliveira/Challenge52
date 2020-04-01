package oliveira.fabio.challenge52.goalcreate.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.result.coroutines.SuspendableResult
import features.goalcreate.R
import kotlinx.coroutines.launch
import oliveira.fabio.challenge52.goalcreate.domain.usecase.GetGoalSuggestionsUseCase
import oliveira.fabio.challenge52.goalcreate.domain.vo.GoalSuggestion
import oliveira.fabio.challenge52.goalcreate.presentation.action.GoalCreateNameSuggestionsActions
import oliveira.fabio.challenge52.goalcreate.presentation.viewstate.GoalCreateNameSuggestionsViewState
import timber.log.Timber

internal class GoalCreateNameSuggestionsViewModel(
    private val getGoalSuggestionsUseCase: GetGoalSuggestionsUseCase
) : ViewModel() {

    private val _goalCreateNameSuggestionsAction by lazy { MutableLiveData<GoalCreateNameSuggestionsActions>() }
    val goalCreateNameSuggestionsAction by lazy { _goalCreateNameSuggestionsAction }

    private val _goalCreateNameSuggestionsViewState by lazy { MutableLiveData<GoalCreateNameSuggestionsViewState>() }
    val goalCreateNameSuggestionsViewState by lazy { _goalCreateNameSuggestionsViewState }

    init {
        getSuggestionsList()
    }

    private fun getSuggestionsList() {
        viewModelScope.launch {
            GoalCreateNameSuggestionsViewState(
                isLoading = true
            ).setState()
            SuspendableResult.of<List<GoalSuggestion>, Exception> {
                getGoalSuggestionsUseCase()
            }.fold(
                success = {
                    GoalCreateNameSuggestionsActions.Suggestions(it).sendAction()
                    GoalCreateNameSuggestionsViewState(
                        isLoading = false,
                        isSuggestionsVisible = true
                    ).setState()
                },
                failure = {
                    GoalCreateNameSuggestionsActions.Error(
                        R.string.goal_create_name_suggestions_error_title,
                        R.string.goal_create_name_suggestions_error_description
                    ).sendAction()
                    Timber.e(it)
                }
            )
        }
    }

    private fun GoalCreateNameSuggestionsActions.sendAction() {
        _goalCreateNameSuggestionsAction.value = this
    }

    private fun GoalCreateNameSuggestionsViewState.setState() {
        _goalCreateNameSuggestionsViewState.value = this
    }
}