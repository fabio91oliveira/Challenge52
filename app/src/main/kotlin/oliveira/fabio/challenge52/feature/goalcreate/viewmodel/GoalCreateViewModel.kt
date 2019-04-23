package oliveira.fabio.challenge52.feature.goalcreate.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import oliveira.fabio.challenge52.model.repository.GoalRepository
import oliveira.fabio.challenge52.model.repository.WeekRepository
import oliveira.fabio.challenge52.persistence.model.entity.Goal
import oliveira.fabio.challenge52.persistence.model.entity.Week
import oliveira.fabio.challenge52.util.extension.toFloatCurrency
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.coroutines.CoroutineContext

class GoalCreateViewModel(private val goalRepository: GoalRepository, private val weekRepository: WeekRepository) :
    ViewModel(), CoroutineScope {

    private val job by lazy { SupervisorJob() }
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    val mutableLiveData by lazy { MutableLiveData<Boolean>() }

    public override fun onCleared() {
        super.onCleared()
        if (job.isActive) job.cancel()
    }

    fun createGoal(goal: Goal) {
        launch {
            SuspendableResult.of<Long, Exception> { goalRepository.addGoal(goal) }.fold(
                success = {
                    goal.id = it
                    SuspendableResult.of<List<Long>, Exception> { weekRepository.addWeeks(createWeeks(goal)) }
                        .fold(success = {
                            mutableLiveData.postValue(true)
                        }, failure = {
                            mutableLiveData.postValue(false)
                        })

                }, failure = {
                    mutableLiveData.postValue(false)
                })
        }
    }

    fun isMoreOrEqualsOne(value: String): Boolean {
        if (value.isNotEmpty()) {
            return value.toFloatCurrency() >= MONEY_MIN
        }
        return false
    }

    private fun createWeeks(goal: Goal): MutableList<Week> {
        val weeks = mutableListOf<Week>()

        val calendar = Calendar.getInstance()
        calendar.time = goal.initialDate
        var total = goal.valueToStart

        for (i in 1..TOTAL_WEEKS) {
            Week(i, round(total.toDouble(), 2), calendar.time, false, goal.id).apply {
                weeks.add(this)
            }
            total += goal.valueToStart
            calendar.add(Calendar.DAY_OF_YEAR, 7)
        }

        return weeks
    }

    private fun round(value: Double, div: Int): Float {
        return BigDecimal(value).setScale(div, RoundingMode.HALF_EVEN).toFloat()
    }

    companion object {
        private const val MONEY_MIN = 1.0
        private const val TOTAL_WEEKS = 52
    }
}