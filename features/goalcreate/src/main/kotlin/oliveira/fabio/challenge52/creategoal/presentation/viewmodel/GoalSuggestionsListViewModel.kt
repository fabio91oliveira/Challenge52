package oliveira.fabio.challenge52.creategoal.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.result.coroutines.SuspendableResult
import features.goalcreate.R
import kotlinx.coroutines.launch
import oliveira.fabio.challenge52.creategoal.domain.usecase.CreateGoalToSaveObjectUseCase
import oliveira.fabio.challenge52.creategoal.domain.usecase.GetGoalSuggestionsUseCase
import oliveira.fabio.challenge52.creategoal.presentation.vo.GoalSuggestion
import oliveira.fabio.challenge52.creategoal.presentation.action.GoalSuggestionsListActions
import oliveira.fabio.challenge52.creategoal.presentation.viewstate.GoalSuggestionsListViewState
import oliveira.fabio.challenge52.presentation.vo.GoalToSave
import oliveira.fabio.challenge52.presentation.vo.Challenge
import timber.log.Timber

internal class GoalSuggestionsListViewModel(
    private val getGoalSuggestionsUseCase: GetGoalSuggestionsUseCase,
    private val createGoalToSaveObjectUseCase: CreateGoalToSaveObjectUseCase
) : ViewModel() {

    private val _goalSuggestionsListActions by lazy { MutableLiveData<GoalSuggestionsListActions>() }
    val goalSuggestionsListActions by lazy { _goalSuggestionsListActions }

    private val _goalSuggestionsListViewState by lazy { MutableLiveData<GoalSuggestionsListViewState>() }
    val goalSuggestionsListViewState by lazy { _goalSuggestionsListViewState }

    init {
        getSuggestionsList()
    }

    private fun getSuggestionsList() {
        viewModelScope.launch {
            GoalSuggestionsListViewState(
                isLoading = true
            ).setState()
            SuspendableResult.of<List<GoalSuggestion>, Exception> {
                getGoalSuggestionsUseCase()
            }.fold(
                success = {
                    GoalSuggestionsListActions.SuggestionsList(it).sendAction()
                    GoalSuggestionsListViewState(
                        isLoading = false,
                        isSuggestionsVisible = true
                    ).setState()
                },
                failure = {
                    GoalSuggestionsListActions.Error(
                        R.string.goal_suggestions_list_error_title,
                        R.string.goal_suggestions_list_error_description
                    ).sendAction()
                    Timber.e(it)
                }
            )
        }
    }

    fun goToNameSetupScreen(
        goalSuggestion: GoalSuggestion,
        challenge: Challenge?
    ) {
        viewModelScope.launch {
            SuspendableResult.of<GoalToSave, Exception> {
                createGoalToSaveObjectUseCase(goalSuggestion, challenge)
            }.fold(
                success = {
                    GoalSuggestionsListActions.GoToGoalChooseNameScreen(it).sendAction()
                },
                failure = {
                    GoalSuggestionsListActions.Error(
                        R.string.goal_suggestions_list_error_title,
                        R.string.goal_suggestions_list_error_description
                    ).sendAction()
                    Timber.e(it)
                }
            )
        }
    }

    private fun GoalSuggestionsListActions.sendAction() {
        _goalSuggestionsListActions.value = this
    }

    private fun GoalSuggestionsListViewState.setState() {
        _goalSuggestionsListViewState.value = this
    }
}