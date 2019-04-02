package oliveira.fabio.challenge52.feature.goallist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import oliveira.fabio.challenge52.model.entity.Goal
import oliveira.fabio.challenge52.model.entity.Week
import oliveira.fabio.challenge52.model.repository.GoalWithWeeksRepository
import oliveira.fabio.challenge52.model.vo.GoalWithWeeks
import oliveira.fabio.challenge52.util.Event
import kotlin.coroutines.CoroutineContext

class GoalListViewModel(private val goalWithWeeksRepository: GoalWithWeeksRepository) : ViewModel(), CoroutineScope {
    private val job = SupervisorJob()

    val mutableLiveDataGoals by lazy { MutableLiveData<Event<List<GoalWithWeeks>>>() }
    val mutableLiveDataRemoved by lazy { MutableLiveData<Event<Boolean>>() }
    val goalWithWeeksToRemove by lazy { mutableListOf<GoalWithWeeks>() }

    var isDeleteShown = false

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    public override fun onCleared() {
        super.onCleared()
        if (job.isActive) job.cancel()
    }

    fun listGoals() {
        launch {
            SuspendableResult.of<List<GoalWithWeeks>, Exception> { goalWithWeeksRepository.getAllGoalsWithWeeks() }
                .fold(
                    success = {
                        mutableLiveDataGoals.postValue(Event(it))
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

            SuspendableResult.of<Int, Exception> { goalWithWeeksRepository.removeGoals(goalsToRemove) }.fold(
                success = {
                    SuspendableResult.of<Int, Exception> { goalWithWeeksRepository.removeWeeks(weeksToRemove) }
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
}