package oliveira.fabio.challenge52.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.result.coroutines.SuspendableResult
import features.goaldetails.R
import kotlinx.coroutines.launch
import oliveira.fabio.challenge52.domain.model.Goal
import oliveira.fabio.challenge52.domain.model.Week
import oliveira.fabio.challenge52.domain.usecase.ChangeWeekStatusUseCase
import oliveira.fabio.challenge52.domain.usecase.CreateTopDetailsUseCase
import oliveira.fabio.challenge52.domain.usecase.MountGoalsDetailsUseCase
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
    private val state: SavedStateHandle,
    private val mountGoalsDetailsUseCase: MountGoalsDetailsUseCase,
    private val changeWeekStatusUseCase: ChangeWeekStatusUseCase,
    private val createTopDetailsUseCase: CreateTopDetailsUseCase,
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

    private val goal by lazy { state.get<Goal>(GOAL_TAG) ?: initializerError() as Goal }
    val goalName = goal.name

    init {
        initState()
        mountDetails()
    }

    fun changeWeekStatus(
        week: Week
    ) {
        setViewState {
            it.copy(
                isWeekBeingUpdated = true,
                dialog = Dialog.NoDialog
            )
        }
        viewModelScope.launch {
            SuspendableResult.of<Unit, Exception> {
                changeWeekStatusUseCase(week)
            }.fold(
                success = {
                    GoalDetailsActions.UpdateWeek(
                        week,
                        R.string.goal_details_week_updated
                    ).sendAction()
                    updateTopDetails()
                    setViewState {
                        it.copy(isWeekBeingUpdated = false)
                    }
                },
                failure = {
                    setViewState { state ->
                        state.copy(
                            isWeekBeingUpdated = false,
                            dialog = Dialog.RegularErrorDialog(R.string.goal_details_update_error_message)
                        )
                    }
                    Timber.e(it)
                }
            )
        }
    }

    fun removeGoal() {
        viewModelScope.launch {
            SuspendableResult.of<Unit, Exception> { removeGoalUseCase(goal.id) }
                .fold(success = {
                    GoalDetailsActions.RemoveGoal.sendAction()
                }, failure = {
                    setViewState { state ->
                        state.copy(dialog = Dialog.RegularErrorDialog(R.string.goal_details_update_error_message))
                    }
                    Timber.e(it)
                })
        }
    }

    fun completeGoal() {
        viewModelScope.launch {
            SuspendableResult.of<Unit, Exception> { setGoalAsDoneUseCase(goal.id) }
                .fold(success = {
                    GoalDetailsActions.CompleteGoal.sendAction()
                }, failure = {
                    setViewState { state ->
                        state.copy(dialog = Dialog.RegularErrorDialog(R.string.goal_details_update_error_message))
                    }
                    Timber.e(it)
                })
        }
    }

    fun showConfirmationDialogDoneGoalWhenUpdated() {
        viewModelScope.launch {
            SuspendableResult.of<Boolean, Exception> {
                verifyAllWeekAreCompletedUseCase(
                    goal.weeks
                )
            }.fold(success = { allWeeksAreCompleted ->
                if (allWeeksAreCompleted)
                    setViewState {
                        it.copy(dialog = Dialog.ConfirmationDialogDoneGoal(R.string.goal_details_move_to_done_first_dialog))
                    }
            }, failure = {
                setViewState { state ->
                    state.copy(dialog = Dialog.RegularErrorDialog(R.string.goal_details_update_error_message))
                }
                Timber.e(it)
            })
        }
    }

    fun showConfirmationDialogDoneGoal() {
        viewModelScope.launch {
            SuspendableResult.of<Boolean, Exception> {
                verifyAllWeekAreCompletedUseCase(
                    goal.weeks
                )
            }.fold(success = { allWeeksAreChecked ->
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
                setViewState { state ->
                    state.copy(dialog = Dialog.RegularErrorDialog(R.string.goal_details_update_error_message))
                }
                Timber.e(it)
            })
        }
    }

    fun showConfirmationDialogRemoveGoal() =
        setViewState {
            it.copy(dialog = Dialog.ConfirmationDialogRemoveGoal(R.string.goal_details_are_you_sure_remove))
        }

    fun showConfirmationDialogUpdateWeek(week: Week) {
        setViewState {
            it.copy(
                dialog = Dialog.ConfirmationDialogUpdateWeek(
                    R.string.goal_details_date_after_today,
                    week
                )
            )
        }
    }

    private fun isDialogVisible(): Boolean {
        _goalDetailsViewState.value?.also {
            return it.dialog is Dialog.ConfirmationDialogUpdateWeek
        }

        return false
    }

    fun hideDialogs() =
        setViewState {
            it.copy(dialog = Dialog.NoDialog)
        }

    fun isDateAfterTodayWhenWeekIsNotChecked(week: Week): Boolean {
        if (week.isChecked.not()) return week.date.after(Date())
        return false
    }

    private fun mountDetails() {
        setViewState {
            GoalDetailsViewState(isLoading = true)
        }
        viewModelScope.launch {
            SuspendableResult.of<MutableList<AdapterItem<TopDetails, String, Week>>, Exception> {
                mountGoalsDetailsUseCase(goal.weeks)
            }.fold(
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
                    GoalDetailsActions.CriticalError(R.string.goal_details_list_error_message)
                        .sendAction()
                    setViewState {
                        GoalDetailsViewState(isLoading = false)
                    }
                    Timber.e(it)
                }
            )
        }
    }

    private fun updateTopDetails() {
        viewModelScope.launch {
            SuspendableResult.of<TopDetails, Exception> {
                createTopDetailsUseCase(goal)
            }.fold(success = {
                GoalDetailsActions.UpdateTopDetails(it).sendAction()
            }, failure = {
                setViewState { state ->
                    state.copy(dialog = Dialog.RegularErrorDialog(R.string.goal_details_update_error_message))
                }
                Timber.e(it)
            })
        }
    }

    private fun initializerError() {
        GoalDetailsActions.CriticalError(R.string.goal_details_list_error_message)
            .sendAction()
        setViewState {
            GoalDetailsViewState(isLoading = false)
        }
        Timber.e(ExceptionInInitializerError())
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

    companion object {
        private const val GOAL_TAG = "GOAL"
    }
}