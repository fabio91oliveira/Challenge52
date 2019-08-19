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
import oliveira.fabio.challenge52.domain.usecase.GoalDetailsUseCase
import oliveira.fabio.challenge52.persistence.model.entity.Week
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks
import oliveira.fabio.challenge52.presentation.state.GoalDetailsState
import oliveira.fabio.challenge52.presentation.state.GoalDetailsStateLoading
import java.util.*
import kotlin.coroutines.CoroutineContext

class GoalDetailsViewModel(
    private val goalDetailsUseCase: GoalDetailsUseCase
) : ViewModel(), CoroutineScope {

    private val _goalDetailsState by lazy { MutableLiveData<GoalDetailsState>() }
    private val _goalDetailsStateLoading by lazy { MutableLiveData<GoalDetailsStateLoading>() }

    val goalDetailsState: LiveData<GoalDetailsState>
        get() = _goalDetailsState

    val goalDetailsStateLoading: LiveData<GoalDetailsStateLoading>
        get() = _goalDetailsStateLoading

    var firstTime = true

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    public override fun onCleared() {
        super.onCleared()
        if (job.isActive) job.cancel()
    }

    fun getParsedDetailsList(goalWithWeeks: GoalWithWeeks, week: Week? = null) {
        if (firstTime) _goalDetailsStateLoading.postValue(GoalDetailsStateLoading.ShowLoading)
        launch {
            SuspendableResult.of<MutableList<Item>, Exception> {
                goalDetailsUseCase.getItemList(
                    goalWithWeeks,
                    week
                )
            }
                .fold(
                    success = {
                        _goalDetailsState.postValue(GoalDetailsState.ShowAddedGoals(it))
                        if (firstTime) _goalDetailsStateLoading.postValue(GoalDetailsStateLoading.HideLoading)
                    },
                    failure = {
                        _goalDetailsState.postValue(GoalDetailsState.ShowAddedGoals(null))
                        if (firstTime) _goalDetailsStateLoading.postValue(GoalDetailsStateLoading.HideLoading)
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
                        _goalDetailsState.postValue(GoalDetailsState.ShowUpdatedGoal(true, week))
                    },
                    failure = {
                        goalDetailsUseCase.changeWeekDepositStatus(week)
                        _goalDetailsState.postValue(GoalDetailsState.ShowUpdatedGoal(false, null))
                    }
                )
        }
    }

    fun removeGoal(goalWithWeeks: GoalWithWeeks) {
        launch {
            SuspendableResult.of<Int, Exception> { goalDetailsUseCase.removeGoal(goalWithWeeks.goal) }.fold(
                success = {
                    SuspendableResult.of<Int, Exception> { goalDetailsUseCase.removeWeeks(goalWithWeeks.weeks) }
                        .fold(success = {
                            _goalDetailsState.postValue(GoalDetailsState.ShowRemovedGoal(true))
                        }, failure = {
                            _goalDetailsState.postValue(GoalDetailsState.ShowRemovedGoal(false))
                        })

                }, failure = {
                    _goalDetailsState.postValue(GoalDetailsState.ShowRemovedGoal(false))
                })
        }
    }

    fun completeGoal(goalWithWeeks: GoalWithWeeks) {
        launch {
            SuspendableResult.of<Unit, Exception> { goalDetailsUseCase.updateWeeks(goalWithWeeks.weeks) }.fold(
                success = {
                    goalDetailsUseCase.setGoalAsDone(goalWithWeeks)
                    SuspendableResult.of<Unit, Exception> { goalDetailsUseCase.updateGoal(goalWithWeeks.goal) }
                        .fold(success = {
                            _goalDetailsState.postValue(GoalDetailsState.ShowCompletedGoal(true))
                        }, failure = {
                            _goalDetailsState.postValue(GoalDetailsState.ShowCompletedGoal(false))
                        })

                }, failure = {
                    _goalDetailsState.postValue(GoalDetailsState.ShowCompletedGoal(false))
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
}