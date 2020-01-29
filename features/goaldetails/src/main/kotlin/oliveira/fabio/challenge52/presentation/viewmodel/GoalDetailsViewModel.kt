package oliveira.fabio.challenge52.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.kittinunf.result.coroutines.SuspendableResult
import features.goaldetails.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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
import oliveira.fabio.challenge52.presentation.viewstate.GoalDetailsViewState
import java.util.*
import kotlin.coroutines.CoroutineContext

class GoalDetailsViewModel(
    private val getItemsListUseCase: GetItemsListUseCase,
    private val changeWeekDepositStatusUseCase: ChangeWeekDepositStatusUseCase,
    private val setGoalAsDoneUseCase: SetGoalAsDoneUseCase,
    private val removeGoalUseCase: RemoveGoalUseCase,
    private val checkAllWeeksAreDepositedUseCase: CheckAllWeeksAreDepositedUseCase
) : ViewModel(), CoroutineScope {

    private val _goalDetailsAction by lazy { MutableLiveData<GoalDetailsActions>() }
    private val _goalDetailsViewState by lazy { MutableLiveData<GoalDetailsViewState>() }

    val goalDetailsActions: LiveData<GoalDetailsActions>
        get() = _goalDetailsAction

    val goalDetailsViewState: LiveData<GoalDetailsViewState>
        get() = _goalDetailsViewState

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    public override fun onCleared() {
        super.onCleared()
        if (job.isActive) job.cancel()
    }

    fun getWeeksList(goalWithWeeks: GoalWithWeeks) {
        GoalDetailsViewState(isLoading = true).newState()
        launch {
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
                    }
                )
        }
    }

    fun getWeeksList(goalWithWeeks: GoalWithWeeks, week: Week?) {
        launch {
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
                    }
                )
        }
    }

    fun changeWeekDepositStatus(week: Week) {
        launch {
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
                    }
                )
        }
    }

    fun removeGoal(goalWithWeeks: GoalWithWeeks) {
        launch {
            SuspendableResult.of<Unit, Exception> { removeGoalUseCase(goalWithWeeks) }
                .fold(success = {
                    GoalDetailsActions.ShowRemovedGoal.run()
                }, failure = {
                    GoalDetailsActions.ShowError(R.string.goal_details_remove_error_message).run()
                })
        }
    }

    fun completeGoal(goalWithWeeks: GoalWithWeeks) {
        launch {
            SuspendableResult.of<Unit, Exception> { setGoalAsDoneUseCase(goalWithWeeks) }
                .fold(success = {
                    GoalDetailsActions.ShowCompletedGoal.run()
                }, failure = {
                    GoalDetailsActions.ShowError(R.string.goal_details_make_done_error_message)
                        .run()
                })
        }
    }

    fun showConfirmationDialogDoneGoalWhenUpdated(goalWithWeeks: GoalWithWeeks) {
        launch {
            SuspendableResult.of<Boolean, Exception> {
                checkAllWeeksAreDepositedUseCase(
                    goalWithWeeks
                )
            }
                .fold(success = { areAllWeeksDeposited ->
                    if (areAllWeeksDeposited)
                        GoalDetailsActions.ShowConfirmationDialogDoneGoal(R.string.goal_details_move_to_done_first_dialog).run()
                }, failure = {
                    GoalDetailsActions.ShowError(R.string.goals_generic_error).run()
                })
        }
    }

    fun showConfirmationDialogDoneGoal(goalWithWeeks: GoalWithWeeks) {
        launch {
            SuspendableResult.of<Boolean, Exception> {
                checkAllWeeksAreDepositedUseCase(
                    goalWithWeeks
                )
            }
                .fold(success = { areAllWeeksDeposited ->
                    if (areAllWeeksDeposited) {
                        GoalDetailsActions.ShowConfirmationDialogDoneGoal(R.string.goal_details_are_you_sure_done)
                            .run()
                    } else {
                        GoalDetailsActions.ShowCantMoveToDoneDialog(R.string.goal_details_cannot_move_to_done)
                            .run()
                    }
                }, failure = {
                    GoalDetailsActions.ShowError(R.string.goals_generic_error).run()
                })
        }
    }

    fun showConfirmationDialogRemoveGoal() =
        GoalDetailsActions.ShowConfirmationDialogRemoveGoal.run()

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
}