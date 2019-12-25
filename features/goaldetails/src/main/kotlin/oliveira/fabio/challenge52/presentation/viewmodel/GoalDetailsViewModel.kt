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
import oliveira.fabio.challenge52.presentation.state.GoalDetailsAction
import oliveira.fabio.challenge52.presentation.state.GoalDetailsViewState
import java.util.*
import kotlin.coroutines.CoroutineContext

class GoalDetailsViewModel(
    private val getItemsListUseCase: GetItemsListUseCase,
    private val changeWeekDepositStatusUseCase: ChangeWeekDepositStatusUseCase,
    private val setGoalAsDoneUseCase: SetGoalAsDoneUseCase,
    private val removeGoalUseCase: RemoveGoalUseCase,
    private val checkAllWeeksAreDepositedUseCase: CheckAllWeeksAreDepositedUseCase
) : ViewModel(), CoroutineScope {

    private val _goalDetailsAction by lazy { MutableLiveData<GoalDetailsAction>() }
    private val _goalDetailsViewState by lazy { MutableLiveData<GoalDetailsViewState>() }

    val goalDetailsAction: LiveData<GoalDetailsAction>
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
        handleLoading(true)
        launch {
            SuspendableResult.of<MutableList<Item>, Exception> {
                getItemsListUseCase(goalWithWeeks)
            }
                .fold(
                    success = {
                        GoalDetailsAction.ShowAddedGoalsFirstTime(it).run()
                        handleLoading(false)
                    },
                    failure = {
                        GoalDetailsAction.ShowError(null).run()
                        handleLoading(false)
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
                        GoalDetailsAction.ShowAddedGoals(it).run()
                    },
                    failure = {
                        GoalDetailsAction.ShowError(null).run()
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
                        GoalDetailsAction.ShowUpdatedGoal(week).run()
                    },
                    failure = {
                        week.isDeposited = !week.isDeposited
                        // TODO voltar estado de depositado, corrigir na view
                        GoalDetailsAction.ShowError(R.string.goal_details_update_error_message)
                            .run()
                    }
                )
        }
    }

    fun removeGoal(goalWithWeeks: GoalWithWeeks) {
        launch {
            SuspendableResult.of<Unit, Exception> { removeGoalUseCase(goalWithWeeks) }
                .fold(success = {
                    GoalDetailsAction.ShowRemovedGoal.run()
                }, failure = {
                    GoalDetailsAction.ShowError(R.string.goal_details_remove_error_message).run()
                })
        }
    }

    fun completeGoal(goalWithWeeks: GoalWithWeeks) {
        launch {
            SuspendableResult.of<Unit, Exception> { setGoalAsDoneUseCase(goalWithWeeks) }
                .fold(success = {
                    GoalDetailsAction.ShowCompletedGoal.run()
                }, failure = {
                    GoalDetailsAction.ShowError(R.string.goal_details_make_done_error_message).run()
                })
        }
    }

    fun showConfirmationDialogDoneGoal(goalWithWeeks: GoalWithWeeks, hasUpdate: Boolean = false) {
        launch {
            SuspendableResult.of<Boolean, Exception> {
                checkAllWeeksAreDepositedUseCase(
                    goalWithWeeks
                )
            }
                .fold(success = {
                    if (it) {
                        val message =
                            if (hasUpdate) R.string.goal_details_move_to_done_first_dialog else R.string.goal_details_are_you_sure_done
                        GoalDetailsAction.ShowConfirmationDialogDoneGoal(it, message).run()
                    } else {
                        if (!hasUpdate) GoalDetailsAction.ShowCantMoveToDoneDialog.run()
                    }
                }, failure = {
                    GoalDetailsAction.ShowError(R.string.goals_generic_error).run()
                })
        }
    }

    fun showConfirmationDialogRemoveGoal() =
        GoalDetailsAction.ShowConfirmationDialogRemoveGoal.run()

    fun isDateAfterTodayWhenWeekIsNotDeposited(week: Week): Boolean {
        if (!week.isDeposited) return week.date.after(Date())
        return false
    }

    private fun handleLoading(isLoading: Boolean) {
        _goalDetailsViewState.value = GoalDetailsViewState(isLoading = isLoading)
    }

    private fun GoalDetailsAction.run() {
        _goalDetailsAction.value = this
    }
}