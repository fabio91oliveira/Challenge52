package oliveira.fabio.challenge52.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import oliveira.fabio.challenge52.domain.model.vo.HeaderItem
import oliveira.fabio.challenge52.domain.model.vo.Item
import oliveira.fabio.challenge52.domain.model.vo.SubItemDetails
import oliveira.fabio.challenge52.domain.model.vo.SubItemWeek
import oliveira.fabio.challenge52.extensions.getCurrentYear
import oliveira.fabio.challenge52.extensions.getMonthName
import oliveira.fabio.challenge52.extensions.getMonthNumber
import oliveira.fabio.challenge52.model.repository.GoalRepository
import oliveira.fabio.challenge52.model.repository.WeekRepository
import oliveira.fabio.challenge52.persistence.model.entity.Week
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks
import oliveira.fabio.challenge52.presentation.state.GoalDetailsState
import oliveira.fabio.challenge52.presentation.state.GoalDetailsStateLoading
import java.util.*
import kotlin.coroutines.CoroutineContext

class GoalDetailsViewModel(
    private val goalRepository: GoalRepository,
    private val weekRepository: WeekRepository
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

    fun isDateAfterTodayWhenWeekIsNotDeposited(week: Week): Boolean {
        if (!week.isDeposited) return week.date.after(Date())
        return false
    }

    fun getParsedDetailsList(goalWithWeeks: GoalWithWeeks, week: Week? = null) {
        _goalDetailsStateLoading.postValue(GoalDetailsStateLoading.ShowLoading)
        launch {
            SuspendableResult.of<MutableList<Item>, Exception> { parseToDetailsList(goalWithWeeks, week) }
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
            SuspendableResult.of<Unit, Exception> { weekRepository.updateWeek(week) }
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
            SuspendableResult.of<Int, Exception> { goalRepository.removeGoal(goalWithWeeks.goal) }.fold(
                success = {
                    SuspendableResult.of<Int, Exception> { weekRepository.removeWeeks(goalWithWeeks.weeks) }
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
            SuspendableResult.of<Unit, Exception> { weekRepository.updateWeeks(goalWithWeeks.weeks) }.fold(
                success = {
                    goalWithWeeks.goal.isDone = true
                    SuspendableResult.of<Unit, Exception> { goalRepository.updateGoal(goalWithWeeks.goal) }
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

    private fun parseToDetailsList(goalWithWeeks: GoalWithWeeks, week: Week? = null) = mutableListOf<Item>().apply {
        var lastMonth = 1

        week?.let {
            for (weekInner in goalWithWeeks.weeks) {
                if (weekInner.id == it.id) {
                    weekInner.isDeposited = it.isDeposited
                    break
                }
            }
        }

        add(
            SubItemDetails(
                goalWithWeeks.getPercentOfConclusion(),
                (goalWithWeeks.weeks.size - goalWithWeeks.getRemainingWeeksCount()),
                goalWithWeeks.weeks.size,
                goalWithWeeks.getTotalAccumulated(),
                goalWithWeeks.getTotal()
            )
        )

        goalWithWeeks.weeks.forEach {

            if (it.date.getMonthNumber() != lastMonth) {
                lastMonth = it.date.getMonthNumber()

                add(HeaderItem(formatHeaderTitle(it.date)))
                add(SubItemWeek(it))
            } else {
                add(SubItemWeek(it))
            }
        }
    }

    private fun formatHeaderTitle(date: Date) = "${date.getMonthName()}/${date.getCurrentYear()}"
}