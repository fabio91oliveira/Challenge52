package oliveira.fabio.challenge52.feature.donegoalslist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import oliveira.fabio.challenge52.persistence.model.entity.Goal
import oliveira.fabio.challenge52.persistence.model.entity.Week
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks
import oliveira.fabio.challenge52.repository.GoalWithWeeksRepository
import oliveira.fabio.challenge52.util.Event
import kotlin.coroutines.CoroutineContext

class DoneGoalsListViewModel(private val goalWithWeeksRepository: GoalWithWeeksRepository) : ViewModel(),
    CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    val mutableLiveDataDoneGoals by lazy { MutableLiveData<MutableList<GoalWithWeeks>?>() }
    val mutableLiveDataRemoved by lazy { MutableLiveData<Event<Boolean>>() }
    val doneGoalWithWeeksToRemove by lazy { mutableListOf<GoalWithWeeks>() }
    var isDeleteShown = false
    var firstTime = true

    public override fun onCleared() {
        super.onCleared()
        if (job.isActive) job.cancel()
    }

    fun listDoneGoals() {
        launch {
            SuspendableResult.of<List<GoalWithWeeks>, Exception> { goalWithWeeksRepository.getDoneAllGoalsWithWeeks() }
                .fold(
                    success = {
                        mutableLiveDataDoneGoals.postValue(it.toMutableList())
                    },
                    failure = {
                        mutableLiveDataDoneGoals.postValue(null)
                    }
                )
        }
    }

    fun removeDoneGoals() {
        launch {
            val goalsToRemove = arrayListOf<Goal>()
            val weeksToRemove = arrayListOf<Week>()

            doneGoalWithWeeksToRemove.forEach {
                goalsToRemove.add(it.goal)
                weeksToRemove.addAll(it.weeks)
            }

            SuspendableResult.of<Int, Exception> { goalWithWeeksRepository.removeGoals(goalsToRemove) }.fold(
                success = {
                    SuspendableResult.of<Int, Exception> { goalWithWeeksRepository.removeWeeks(weeksToRemove) }
                        .fold(success = {
                            mutableLiveDataDoneGoals.value?.removeAll(doneGoalWithWeeksToRemove)
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