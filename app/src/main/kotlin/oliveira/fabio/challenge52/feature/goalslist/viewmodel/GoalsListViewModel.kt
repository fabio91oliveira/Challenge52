package oliveira.fabio.challenge52.feature.goalslist.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import oliveira.fabio.challenge52.model.repository.GoalRepository
import oliveira.fabio.challenge52.model.repository.GoalWithWeeksRepository
import oliveira.fabio.challenge52.model.repository.WeekRepository
import oliveira.fabio.challenge52.persistence.model.entity.Goal
import oliveira.fabio.challenge52.persistence.model.entity.Week
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks
import oliveira.fabio.challenge52.model.vo.Event
import kotlin.coroutines.CoroutineContext

class GoalsListViewModel(
    private val goalWithWeeksRepository: GoalWithWeeksRepository,
    private val goalRepository: GoalRepository,
    private val weekRepository: WeekRepository
) : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    val mutableLiveDataGoals by lazy { MutableLiveData<MutableList<GoalWithWeeks>?>() }
    val mutableLiveDataRemoved by lazy { MutableLiveData<Event<Boolean>>() }
    val goalWithWeeksToRemove by lazy { mutableListOf<GoalWithWeeks>() }
    var isDeleteShown = false
    var firstTime = true

    public override fun onCleared() {
        super.onCleared()
        if (job.isActive) job.cancel()
    }

    fun listGoals() {
        launch {
            SuspendableResult.of<List<GoalWithWeeks>, Exception> { goalWithWeeksRepository.getAllGoalsWithWeeks() }
                .fold(
                    success = {
                        it.forEach {
                            Log.d("kk", it.goal.valueToStart.toString())

                            it.weeks.forEach {
                                Log.d("kkkk", it.spittedValue.toString())
                            }

                            Log.d("kk", it.getTotal().toString())
                        }
                        mutableLiveDataGoals.postValue(it.toMutableList())
                    },
                    failure = {
                        mutableLiveDataGoals.postValue(null)
                    }
                )
        }
    }

    fun removeGoals() {
        launch {
            val goalsToRemove = arrayListOf<Goal>()
            val weeksToRemove = arrayListOf<Week>()

            goalWithWeeksToRemove.forEach {
                goalsToRemove.add(it.goal)
                weeksToRemove.addAll(it.weeks)
            }

            SuspendableResult.of<Int, Exception> { goalRepository.removeGoals(goalsToRemove) }.fold(
                success = {
                    SuspendableResult.of<Int, Exception> { weekRepository.removeWeeks(weeksToRemove) }
                        .fold(success = {
                            mutableLiveDataGoals.value?.removeAll(goalWithWeeksToRemove)
                            mutableLiveDataRemoved.postValue(Event(true))
                        }, failure = {
                            mutableLiveDataRemoved.postValue(Event(false))
                        })

                }, failure = {
                    mutableLiveDataRemoved.postValue(Event(false))
                })
        }
    }
}