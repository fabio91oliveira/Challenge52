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

    init {
        initState()
    }

    fun getWeeksList(goalWithWeeks: GoalWithWeeks) {
        setViewState {
            GoalDetailsViewState(isLoading = true)
        }
        viewModelScope.launch {
            SuspendableResult.of<MutableList<Item>, Exception> {
                getItemsListUseCase(goalWithWeeks)
            }
                .fold(
                    success = {
                        GoalDetailsActions.AddedGoalsFirstTime(it).sendAction()
                        setViewState {
                            GoalDetailsViewState(
                                isLoading = false,
                                isContentVisible = true,
                                isBarExpanded = true
                            )
                        }
                    },
                    failure = {
                        GoalDetailsActions.Error(R.string.goal_details_list_error_message)
                            .sendAction()
                        setViewState {
                            GoalDetailsViewState(isLoading = false)
                        }
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
                        GoalDetailsActions.AddedGoals(it).sendAction()
                        setViewState {
                            GoalDetailsViewState(
                                isLoading = false,
                                isContentVisible = true,
                                isBarExpanded = false
                            )
                        }
                    },
                    failure = {
                        GoalDetailsActions.Error(R.string.goal_details_list_error_message)
                            .sendAction()
                        setViewState {
                            GoalDetailsViewState(isLoading = false)
                        }
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
                        GoalDetailsActions.UpdatedGoal(week).sendAction()
                    },
                    failure = {
                        GoalDetailsActions.Error(R.string.goal_details_update_error_message)
                            .sendAction()
                        Timber.e(it)
                    }
                )
        }
    }

    fun removeGoal(goalWithWeeks: GoalWithWeeks) {
        viewModelScope.launch {
            SuspendableResult.of<Unit, Exception> { removeGoalUseCase(goalWithWeeks) }
                .fold(success = {
                    GoalDetailsActions.RemovedGoal.sendAction()
                }, failure = {
                    GoalDetailsActions.Error(R.string.goal_details_remove_error_message)
                        .sendAction()
                    Timber.e(it)
                })
        }
    }

    fun completeGoal(goalWithWeeks: GoalWithWeeks) {
        viewModelScope.launch {
            SuspendableResult.of<Unit, Exception> { setGoalAsDoneUseCase(goalWithWeeks) }
                .fold(success = {
                    GoalDetailsActions.CompletedGoal.sendAction()
                }, failure = {
                    GoalDetailsActions.Error(R.string.goal_details_make_done_error_message)
                        .sendAction()
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
                        setViewState {
                            it.copy(dialog = Dialog.ConfirmationDialogDoneGoal(R.string.goal_details_move_to_done_first_dialog))
                        }
                }, failure = {
                    GoalDetailsActions.Error(R.string.goals_generic_error).sendAction()
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
                        setViewState {
                            it.copy(dialog = Dialog.ConfirmationDialogDoneGoal(R.string.goal_details_are_you_sure_done))
                        }
                    } else {
                        setViewState {
                            it.copy(dialog = Dialog.DefaultDialogMoveToDone(R.string.goal_details_cannot_move_to_done))
                        }
                    }
                }, failure = {
                    GoalDetailsActions.Error(R.string.goals_generic_error).sendAction()
                    Timber.e(it)
                })
        }
    }

    fun showConfirmationDialogRemoveGoal() =
        setViewState {
            it.copy(dialog = Dialog.ConfirmationDialogRemoveGoal(R.string.goal_details_are_you_sure_remove))
        }

    fun showConfirmationDialogUpdateWeek(week: Week) =
        setViewState {
            it.copy(
                dialog = Dialog.ConfirmationDialogUpdateWeek(
                    R.string.goal_details_date_after_today,
                    week
                )
            )
        }

    fun hideDialogs() =
        setViewState {
            it.copy(dialog = Dialog.NoDialog)
        }

    fun isDateAfterTodayWhenWeekIsNotDeposited(week: Week): Boolean {
        if (!week.isDeposited) return week.date.after(Date())
        return false
    }

    private fun GoalDetailsActions.sendAction() {
        _goalDetailsAction.value = this
    }

    private fun initState() {
        _goalDetailsViewState.value = GoalDetailsViewState.init()
    }

    private fun setViewState(state: (GoalDetailsViewState) -> GoalDetailsViewState) {
        _goalDetailsViewState.value?.also {
            _goalDetailsViewState.value = state(it)
        }
    }
}