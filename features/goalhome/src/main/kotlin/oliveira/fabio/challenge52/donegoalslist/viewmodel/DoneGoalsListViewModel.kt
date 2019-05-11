package oliveira.fabio.challenge52.donegoalslist.viewmodel

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
import oliveira.fabio.challenge52.model.vo.EventVO
import kotlin.coroutines.CoroutineContext

class DoneGoalsListViewModel(
    private val goalWithWeeksRepository: GoalWithWeeksRepository,
    private val goalRepository: GoalRepository,
    private val weekRepository: WeekRepository
) : ViewModel(),
    CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    val mutableLiveDataDoneGoals by lazy { MutableLiveData<MutableList<GoalWithWeeks>?>() }
    val mutableLiveDataRemoved by lazy { MutableLiveData<EventVO<Boolean>>() }
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

            SuspendableResult.of<Int, Exception> { goalRepository.removeGoals(goalsToRemove) }.fold(
                success = {
                    SuspendableResult.of<Int, Exception> { weekRepository.removeWeeks(weeksToRemove) }
                        .fold(success = {
                            mutableLiveDataDoneGoals.value?.removeAll(doneGoalWithWeeksToRemove)
                            mutableLiveDataRemoved.postValue(EventVO(true))
                        }, failure = {
                            mutableLiveDataRemoved.postValue(EventVO(false))
                        })

                }, failure = {
                    mutableLiveDataRemoved.postValue(EventVO(false))
                })
        }
    }
}