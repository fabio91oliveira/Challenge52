package oliveira.fabio.challenge52.feature.goaldetails.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import oliveira.fabio.challenge52.feature.goaldetails.vo.HeaderItem
import oliveira.fabio.challenge52.feature.goaldetails.vo.Item
import oliveira.fabio.challenge52.feature.goaldetails.vo.SubItemDetails
import oliveira.fabio.challenge52.feature.goaldetails.vo.SubItemWeek
import oliveira.fabio.challenge52.model.entity.Week
import oliveira.fabio.challenge52.model.repository.GoalWithWeeksRepository
import oliveira.fabio.challenge52.model.vo.GoalWithWeeks
import oliveira.fabio.challenge52.util.Event
import oliveira.fabio.challenge52.util.extension.getMonthName
import oliveira.fabio.challenge52.util.extension.getMonthNumber
import kotlin.coroutines.CoroutineContext

class GoalDetailsViewModel(private val goalWithWeeksRepository: GoalWithWeeksRepository) : ViewModel(), CoroutineScope {

    val mutableLiveDataUpdated by lazy { MutableLiveData<Event<Boolean>>() }
    val mutableLiveDataItemList by lazy { MutableLiveData<MutableList<Item>>() }

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    public override fun onCleared() {
        super.onCleared()
        if (job.isActive) job.cancel()
    }

    fun updateWeek(week: Week) {
        week.isDeposited = !week.isDeposited
        launch {
            SuspendableResult.of<Unit, Exception> { goalWithWeeksRepository.updateWeek(week) }
                .fold(
                    success = {
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
                goalWithWeeks.goal.totalValue
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