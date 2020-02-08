package oliveira.fabio.challenge52.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.result.coroutines.SuspendableResult
import features.goaldetails.R
import kotlinx.coroutines.launch
import oliveira.fabio.challenge52.domain.model.vo.Item
import oliveira.fabio.challenge52.domain.usecase.ChangeWeekDepositStatusUseCase
import oliveira.fabio.challenge52.domain.usecase.CheckAllWeeksAreDepositedUseCase
import oliveira.fabio.challenge52.domain.usecase.GetItemsListUseCase
import oliveira.fabio.challenge52.domain.usecase.RemoveGoalUseCase
import oliveira.fabio.challenge52.domain.usecase.SetGoalAsDoneUseCase
import oliveira.fabio.challenge52.persistence.model.entity.Week
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks
import oliveira.fabio.challenge52.presentation.action.GoalDetailsActions
import oliveira.fabio.challenge52.presentation.viewstate.Dialog
import oliveira.fabio.challenge52.presentation.viewstate.GoalDetailsViewState
import timber.log.Timber
import java.util.*

class GoalDetailsViewModel(
    private val getItemsListUseCase: GetItemsListUseCase,
    private val changeWeekDepositStatusUseCase: ChangeWeekDepositStatusUseCase,
    private val setGoalAsDoneUseCase: SetGoalAsDoneUseCase,
    private val removeGoalUseCase: RemoveGoalUseCase,
    private val checkAllWeeksAreDepositedUseCase: CheckAllWeeksAreDepositedUseCase
) : ViewModel() {

    private val _goalDetailsAction by lazy { MutableLiveData<GoalDetailsActions>() }
    private val _goalDetailsViewState by lazy { MutableLiveData<GoalDetailsViewState>() }

    val goalDetailsActions: LiveData<GoalDetailsActions>
        get() = _goalDetailsAction

    val goalDetailsViewState: LiveData<GoalDetailsViewState>
        get() = _goalDetailsViewState

    fun getWeeksList(goalWithWeeks: GoalWithWeeks) {
        GoalDetailsViewState(isLoading = true).newState()
        viewModelScope.launch {
            SuspendableResult.of<MutableList<Item>, Exception> {
                getItemsListUseCase(goalWithWeeks)
            }
                .fold(
                    success = {
                        GoalDetailsActions.ShowAddedGoalsFirstTime(it).run()
                        GoalDetailsViewState(isLoading = false).newState()
                    },
                    failure = {
                        GoalDetailsActions.ShowError(R.string.goal_details_list_error_message).run()
                        GoalDetailsViewState(isLoading = false).newState()
                        Timber.e(it)
                    }
                )
        }
    }

    fun getWeeksList(goalWithWeeks: GoalWithWeeks, week: Week?) {
        viewModelScope.launch {
            SuspendableResult.of<MutableList<Item>, Exception> {
                getItemsListUseCase(
                    goalWithWeeks,
                    week
                )
            }
                .fold(
                    success = {
                        GoalDetailsActions.ShowAddedGoals(it).run()
                    },
                    failure = {
                        GoalDetailsActions.ShowError(R.string.goal_details_list_error_message).run()
                        Timber.e(it)
                    }
                )
        }
    }

    fun changeWeekDepositStatus(week: Week) {
        viewModelScope.launch {
            SuspendableResult.of<Unit, Exception> {
                changeWeekDepositStatusUseCase(week)
            }
                .fold(
                    success = {
                        GoalDetailsActions.ShowUpdatedGoal(week).run()
                    },
                    failure = {
                        GoalDetailsActions.ShowError(R.string.goal_details_update_error_message)
                            .run()
                        Timber.e(it)
                    }
                )
        }
    }

    fun removeGoal(goalWithWeeks: GoalWithWeeks) {
        viewModelScope.launch {
            SuspendableResult.of<Unit, Exception> { removeGoalUseCase(goalWithWeeks) }
                .fold(success = {
                    GoalDetailsActions.ShowRemovedGoal.run()
                }, failure = {
                    GoalDetailsActions.ShowError(R.string.goal_details_remove_error_message).run()
                    Timber.e(it)
                })
        }
    }

    fun completeGoal(goalWithWeeks: GoalWithWeeks) {
        viewModelScope.launch {
            SuspendableResult.of<Unit, Exception> { setGoalAsDoneUseCase(goalWithWeeks) }
                .fold(success = {
                    GoalDetailsActions.ShowCompletedGoal.run()
                }, failure = {
                    GoalDetailsActions.ShowError(R.string.goal_details_make_done_error_message)
                        .run()
                    Timber.e(it)
                })
        }
    }

    fun showConfirmationDialogDoneGoalWhenUpdated(goalWithWeeks: GoalWithWeeks) {
        viewModelScope.launch {
            SuspendableResult.of<Boolean, Exception> {
                checkAllWeeksAreDepositedUseCase(
                    goalWithWeeks
                )
            }
                .fold(success = { areAllWeeksDeposited ->
                    if (areAllWeeksDeposited)
                        changeGoalDetailsViewState {
                            it.copy(dialog = Dialog.ConfirmationDialogDoneGoal(R.string.goal_details_move_to_done_first_dialog))
                        }
                }, failure = {
                    GoalDetailsActions.ShowError(R.string.goals_generic_error).run()
                    Timber.e(it)
                })
        }
    }

    fun showConfirmationDialogDoneGoal(goalWithWeeks: GoalWithWeeks) {
        viewModelScope.launch {
            SuspendableResult.of<Boolean, Exception> {
                checkAllWeeksAreDepositedUseCase(
                    goalWithWeeks
                )
            }
                .fold(success = { areAllWeeksDeposited ->
                    if (areAllWeeksDeposited) {
                        changeGoalDetailsViewState {
                            it.copy(dialog = Dialog.ConfirmationDialogDoneGoal(R.string.goal_details_are_you_sure_done))
                        }
                    } else {
                        changeGoalDetailsViewState {
                            it.copy(dialog = Dialog.DefaultDialogMoveToDone(R.string.goal_details_cannot_move_to_done))
                        }
                    }
                }, failure = {
                    GoalDetailsActions.ShowError(R.string.goals_generic_error).run()
                    Timber.e(it)
                })
        }
    }

    fun showConfirmationDialogRemoveGoal() =
        changeGoalDetailsViewState {
            it.copy(dialog = Dialog.ConfirmationDialogRemoveGoal(R.string.goal_details_are_you_sure_remove))
        }


    fun isDateAfterTodayWhenWeekIsNotDeposited(week: Week): Boolean {
        if (!week.isDeposited) return week.date.after(Date())
        return false
    }

    private fun GoalDetailsActions.run() {
        _goalDetailsAction.value = this
    }

    private fun GoalDetailsViewState.newState() {
        _goalDetailsViewState.value = this
    }

    private fun changeGoalDetailsViewState(state: (GoalDetailsViewState) -> GoalDetailsViewState) {
        _goalDetailsViewState.value?.also {
            _goalDetailsViewState.value = state(it)
        }
    }
}