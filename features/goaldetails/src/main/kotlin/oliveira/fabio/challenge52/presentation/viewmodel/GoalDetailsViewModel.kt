package oliveira.fabio.challenge52.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import oliveira.fabio.challenge52.domain.model.vo.Item
import oliveira.fabio.challenge52.domain.usecase.GetItemsListUseCase
import oliveira.fabio.challenge52.domain.usecase.GoalDetailsUseCase
import oliveira.fabio.challenge52.persistence.model.entity.Week
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks
import oliveira.fabio.challenge52.presentation.state.GoalDetailsAction
import oliveira.fabio.challenge52.presentation.state.GoalDetailsViewState
import java.util.*
import kotlin.coroutines.CoroutineContext

class GoalDetailsViewModel(
    // old use case
    private val goalDetailsUseCase: GoalDetailsUseCase,

    private val getItemsListUseCase: GetItemsListUseCase
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

    fun getDetailsList(goalWithWeeks: GoalWithWeeks) {
        handleLoading(true)
        launch {
            SuspendableResult.of<MutableList<Item>, Exception> {
                getItemsListUseCase.getItemList(
                    goalWithWeeks
                )
            }
                .fold(
                    success = {
                        _goalDetailsAction.postValue(GoalDetailsAction.ShowAddedGoalsFirstTime(it))
                        handleLoading(false)
                    },
                    failure = {
                        _goalDetailsAction.postValue(GoalDetailsAction.ShowAddedGoalsFirstTime(null))
                        handleLoading(false)
                    }
                )
        }
    }

    fun getDetailsList(goalWithWeeks: GoalWithWeeks, week: Week?) {
        launch {
            SuspendableResult.of<MutableList<Item>, Exception> {
                getItemsListUseCase.getItemList(
                    goalWithWeeks,
                    week
                )
            }
                .fold(
                    success = {
                        _goalDetailsAction.postValue(GoalDetailsAction.ShowAddedGoals(it))
                    },
                    failure = {
                        _goalDetailsAction.postValue(GoalDetailsAction.ShowAddedGoals(null))
                    }
                )
        }
    }

    fun updateWeek(week: Week) {
        launch {
            SuspendableResult.of<Unit, Exception> {
                goalDetailsUseCase.changeWeekDepositStatus(week)
                goalDetailsUseCase.updateWeek(week)
            }
                .fold(
                    success = {
                        _goalDetailsAction.postValue(GoalDetailsAction.ShowUpdatedGoal(true, week))
                    },
                    failure = {
                        goalDetailsUseCase.changeWeekDepositStatus(week)
                        _goalDetailsAction.postValue(GoalDetailsAction.ShowUpdatedGoal(false, null))
                    }
                )
        }
    }

    fun removeGoal(goalWithWeeks: GoalWithWeeks) {
        launch {
            SuspendableResult.of<Int, Exception> { goalDetailsUseCase.removeGoal(goalWithWeeks.goal) }
                .fold(
                    success = {
                        SuspendableResult.of<Int, Exception> {
                            goalDetailsUseCase.removeWeeks(
                                goalWithWeeks.weeks
                            )
                        }
                            .fold(success = {
                                _goalDetailsAction.postValue(GoalDetailsAction.ShowRemovedGoal(true))
                            }, failure = {
                                _goalDetailsAction.postValue(GoalDetailsAction.ShowRemovedGoal(false))
                            })

                    }, failure = {
                        _goalDetailsAction.postValue(GoalDetailsAction.ShowRemovedGoal(false))
                    })
        }
    }

    fun completeGoal(goalWithWeeks: GoalWithWeeks) {
        launch {
            SuspendableResult.of<Unit, Exception> { goalDetailsUseCase.updateWeeks(goalWithWeeks.weeks) }
                .fold(
                    success = {
                        goalDetailsUseCase.setGoalAsDone(goalWithWeeks)
                        SuspendableResult.of<Unit, Exception> {
                            goalDetailsUseCase.updateGoal(
                                goalWithWeeks.goal
                            )
                        }
                            .fold(success = {
                                _goalDetailsAction.postValue(GoalDetailsAction.ShowCompletedGoal(true))
                            }, failure = {
                                _goalDetailsAction.postValue(GoalDetailsAction.ShowCompletedGoal(false))
                            })

                    }, failure = {
                        _goalDetailsAction.postValue(GoalDetailsAction.ShowCompletedGoal(false))
                    })
        }
    }

    fun isAllWeeksDeposited(goalWithWeeks: GoalWithWeeks): Boolean {
        goalWithWeeks.weeks.forEach {
            if (!it.isDeposited) {
                return false
            }
        }
        return true
    }

    fun isDateAfterTodayWhenWeekIsNotDeposited(week: Week): Boolean {
        if (!week.isDeposited) return week.date.after(Date())
        return false
    }

    //

    private fun handleLoading(isLoading: Boolean) {
        _goalDetailsViewState.value = GoalDetailsViewState(isLoading = isLoading)
    }

    private fun GoalDetailsAction.run() {
        _goalDetailsAction.value = this
    }
}