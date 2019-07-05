package oliveira.fabio.challenge52.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import oliveira.fabio.challenge52.domain.interactor.GoalDetailsInteractorImpl
import oliveira.fabio.challenge52.domain.model.vo.Item
import oliveira.fabio.challenge52.persistence.model.entity.Week
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks
import oliveira.fabio.challenge52.presentation.state.GoalDetailsState
import oliveira.fabio.challenge52.presentation.state.GoalDetailsStateLoading
import java.util.*
import kotlin.coroutines.CoroutineContext

class GoalDetailsViewModel(
    private val goalDetailsInteractor: GoalDetailsInteractorImpl
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
        _goalDetailsStateLoading.postValue(GoalDetailsStateLoading.ShowLoading)
        launch {
            SuspendableResult.of<MutableList<Item>, Exception> {
                goalDetailsInteractor.parseToDetailsList(
                    goalWithWeeks,
                    week
                )
            }
                .fold(
                    success = {
                        _goalDetailsState.postValue(GoalDetailsState.ShowAddedGoals(it))
                        _goalDetailsStateLoading.postValue(GoalDetailsStateLoading.HideLoading)
                    },
                    failure = {
                        _goalDetailsState.postValue(GoalDetailsState.ShowAddedGoals(null))
                        _goalDetailsStateLoading.postValue(GoalDetailsStateLoading.HideLoading)
                    }
                )
        }
    }

    fun updateWeek(week: Week) {
        week.isDeposited = !week.isDeposited
        launch {
            SuspendableResult.of<Unit, Exception> { goalDetailsInteractor.updateWeek(week) }
                .fold(
                    success = {
                        _goalDetailsState.postValue(GoalDetailsState.ShowUpdatedGoal(true))
                    },
                    failure = {
                        week.isDeposited = !week.isDeposited
                        _goalDetailsState.postValue(GoalDetailsState.ShowUpdatedGoal(false))
                    }
                )
        }
    }

    fun removeGoal(goalWithWeeks: GoalWithWeeks) {
        launch {
            SuspendableResult.of<Int, Exception> { goalDetailsInteractor.removeGoal(goalWithWeeks.goal) }.fold(
                success = {
                    SuspendableResult.of<Int, Exception> { goalDetailsInteractor.removeWeeks(goalWithWeeks.weeks) }
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
            SuspendableResult.of<Unit, Exception> { goalDetailsInteractor.updateWeeks(goalWithWeeks.weeks) }.fold(
                success = {
                    goalWithWeeks.goal.isDone = true
                    SuspendableResult.of<Unit, Exception> { goalDetailsInteractor.updateGoal(goalWithWeeks.goal) }
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