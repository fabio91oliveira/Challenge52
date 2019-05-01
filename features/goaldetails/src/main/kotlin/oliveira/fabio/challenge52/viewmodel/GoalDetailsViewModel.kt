package oliveira.fabio.challenge52.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import oliveira.fabio.challenge52.vo.HeaderItem
import oliveira.fabio.challenge52.vo.Item
import oliveira.fabio.challenge52.vo.SubItemDetails
import oliveira.fabio.challenge52.vo.SubItemWeek
import oliveira.fabio.challenge52.model.repository.GoalRepository
import oliveira.fabio.challenge52.model.repository.WeekRepository
import oliveira.fabio.challenge52.persistence.model.entity.Week
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks
import oliveira.fabio.challenge52.util.extension.getMonthName
import oliveira.fabio.challenge52.util.extension.getMonthNumber
import oliveira.fabio.challenge52.vo.Event
import java.util.*
import kotlin.coroutines.CoroutineContext

class GoalDetailsViewModel(
    private val goalRepository: GoalRepository,
    private val weekRepository: WeekRepository
) : ViewModel(), CoroutineScope {

    val mutableLiveDataUpdated by lazy { MutableLiveData<Event<Boolean>>() }
    val mutableLiveDataCompleted by lazy { MutableLiveData<Event<Boolean>>() }
    val mutableLiveDataRemoved by lazy { MutableLiveData<Event<Boolean>>() }
    val mutableLiveDataItemList by lazy { MutableLiveData<MutableList<Item>>() }
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

    fun updateWeek(week: Week) {
        week.isDeposited = !week.isDeposited
        launch {
            SuspendableResult.of<Unit, Exception> { weekRepository.updateWeek(week) }
                .fold(
                    success = {
                        Log.d("aqui", "atualizou " + week.position)
                        mutableLiveDataUpdated.postValue(Event(true))
                    },
                    failure = {
                        week.isDeposited = !week.isDeposited
                        mutableLiveDataUpdated.postValue(Event(false))
                    }
                )
        }
    }

    fun getParsedDetailsList(goalWithWeeks: GoalWithWeeks, week: Week? = null) {
        launch {
            SuspendableResult.of<MutableList<Item>, Exception> { parseToDetailsList(goalWithWeeks, week) }
                .fold(
                    success = {
                        mutableLiveDataItemList.postValue(it)
                    },
                    failure = {
                        mutableLiveDataItemList.postValue(null)
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
                            mutableLiveDataRemoved.postValue(Event(true))
                        }, failure = {
                            mutableLiveDataRemoved.postValue(Event(false))
                        })

                }, failure = {
                    mutableLiveDataRemoved.postValue(Event(false))
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
                            mutableLiveDataCompleted.postValue(Event(true))
                        }, failure = {
                            mutableLiveDataCompleted.postValue(Event(false))
                        })

                }, failure = {
                    mutableLiveDataCompleted.postValue(Event(false))
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

                add(HeaderItem(it.date.getMonthName()))
                add(SubItemWeek(it))
            } else {
                add(SubItemWeek(it))
            }
        }
    }
}