package oliveira.fabio.challenge52.feature.goalcreate.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import oliveira.fabio.challenge52.model.repository.GoalWithWeeksRepository
import oliveira.fabio.challenge52.persistence.model.entity.Goal
import oliveira.fabio.challenge52.persistence.model.entity.Week
import oliveira.fabio.challenge52.util.extension.toCurrentFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

class GoalCreateViewModel(private val goalWithWeeksRepository: GoalWithWeeksRepository) :
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
            SuspendableResult.of<Long, Exception> { goalWithWeeksRepository.addGoal(goal) }.fold(
                success = {
                    goal.id = it
                    SuspendableResult.of<List<Long>, Exception> { goalWithWeeksRepository.addWeeks(createWeeks(goal)) }
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

    fun isZero(value: String): Boolean {
        return value == PATTERN_ZERO
    }

    private fun createWeeks(goal: Goal): MutableList<Week> {
        val weeks = mutableListOf<Week>()

        val spittedValue = goal.totalValue / TOTAL_WEEKS
        val calendar = Calendar.getInstance()
        calendar.time = goal.initialDate

        for (i in 1..TOTAL_WEEKS) {
            Week(i, spittedValue, calendar.time, false, goal.id).apply {
                this.position = i
                this.spittedValue = spittedValue
                this.date = calendar.time
                this.idGoal = goal.id
                this.isDeposited = false
                Log.d("DATA: ", calendar.toCurrentFormat("dd/MM/yyyy"))
                weeks.add(this)
            }
            calendar.add(Calendar.DAY_OF_YEAR, 7)
        }

        return weeks
    }

    companion object {
        private const val PATTERN_ZERO = "000"
        private const val TOTAL_WEEKS = 52
    }
}