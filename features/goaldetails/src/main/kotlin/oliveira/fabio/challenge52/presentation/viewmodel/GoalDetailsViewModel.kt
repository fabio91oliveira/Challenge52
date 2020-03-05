package oliveira.fabio.challenge52.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.result.coroutines.SuspendableResult
import features.goaldetails.R
import kotlinx.coroutines.launch
import oliveira.fabio.challenge52.domain.model.Week
import oliveira.fabio.challenge52.domain.usecase.ChangeWeekStatusUseCase
import oliveira.fabio.challenge52.domain.usecase.MountWeeksListUseCase
import oliveira.fabio.challenge52.domain.usecase.RemoveGoalUseCase
import oliveira.fabio.challenge52.domain.usecase.SetGoalAsDoneUseCase
import oliveira.fabio.challenge52.domain.usecase.VerifyAllWeekAreCompletedUseCase
import oliveira.fabio.challenge52.presentation.action.GoalDetailsActions
import oliveira.fabio.challenge52.presentation.adapter.vo.AdapterItem
import oliveira.fabio.challenge52.presentation.viewstate.Dialog
import oliveira.fabio.challenge52.presentation.viewstate.GoalDetailsViewState
import oliveira.fabio.challenge52.presentation.vo.TopDetails
import timber.log.Timber
import java.util.*

internal class GoalDetailsViewModel(
    private val mountWeeksListUseCase: MountWeeksListUseCase,
    private val changeWeekStatusUseCase: ChangeWeekStatusUseCase,
    private val setGoalAsDoneUseCase: SetGoalAsDoneUseCase,
    private val removeGoalUseCase: RemoveGoalUseCase,
    private val verifyAllWeekAreCompletedUseCase: VerifyAllWeekAreCompletedUseCase
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

    fun mountWeeksList(weeks: ArrayList<Week>) {
        setViewState {
            GoalDetailsViewState(isLoading = true)
        }
        viewModelScope.launch {
            SuspendableResult.of<MutableList<AdapterItem<TopDetails, String, Week>>, Exception> {
                mountWeeksListUseCase(weeks)
            }
                .fold(
                    success = {
                        GoalDetailsActions.PopulateGoalInformation(it).sendAction()
                        setViewState {
                            GoalDetailsViewState(
                                isLoading = false,
                                isContentVisible = true
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

    fun changeWeekStatus(
        week: Week
    ) {
        viewModelScope.launch {
            SuspendableResult.of<Unit, Exception> {
                changeWeekStatusUseCase(week)
            }
                .fold(
                    success = {
                        GoalDetailsActions.UpdateWeek(week).sendAction()
                    },
                    failure = {
                        GoalDetailsActions.Error(R.string.goal_details_update_error_message)
                            .sendAction()
                        Timber.e(it)
                    }
                )
        }
    }

    fun removeGoal(idGoal: Long) {
        viewModelScope.launch {
            SuspendableResult.of<Unit, Exception> { removeGoalUseCase(idGoal) }
                .fold(success = {
                    GoalDetailsActions.RemoveGoal.sendAction()
                }, failure = {
                    GoalDetailsActions.Error(R.string.goal_details_remove_error_message)
                        .sendAction()
                    Timber.e(it)
                })
        }
    }

    fun completeGoal(idGoal: Long) {
        viewModelScope.launch {
            SuspendableResult.of<Unit, Exception> { setGoalAsDoneUseCase(idGoal) }
                .fold(success = {
                    GoalDetailsActions.CompleteGoal.sendAction()
                }, failure = {
                    GoalDetailsActions.Error(R.string.goal_details_make_done_error_message)
                        .sendAction()
                    Timber.e(it)
                })
        }
    }

    fun showConfirmationDialogDoneGoalWhenUpdated(weeks: ArrayList<Week>) {
        viewModelScope.launch {
            SuspendableResult.of<Boolean, Exception> {
                verifyAllWeekAreCompletedUseCase(
                    weeks
                )
            }
                .fold(success = { allWeeksAreCompleted ->
                    if (allWeeksAreCompleted)
                        setViewState {
                            it.copy(dialog = Dialog.ConfirmationDialogDoneGoal(R.string.goal_details_move_to_done_first_dialog))
                        }
                }, failure = {
                    GoalDetailsActions.Error(R.string.goals_generic_error).sendAction()
                    Timber.e(it)
                })
        }
    }

    fun showConfirmationDialogDoneGoal(weeks: ArrayList<Week>) {
        viewModelScope.launch {
            SuspendableResult.of<Boolean, Exception> {
                verifyAllWeekAreCompletedUseCase(
                    weeks
                )
            }
                .fold(success = { allWeeksAreChecked ->
                    if (allWeeksAreChecked) {
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

    fun isDateAfterTodayWhenWeekIsNotChecked(week: Week): Boolean {
        if (week.isChecked.not()) return week.date.after(Date())
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