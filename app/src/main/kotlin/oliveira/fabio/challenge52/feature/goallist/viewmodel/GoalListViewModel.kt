package oliveira.fabio.challenge52.feature.goallist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import oliveira.fabio.challenge52.model.repository.GoalWithWeeksRepository
import oliveira.fabio.challenge52.model.vo.GoalWithWeeks
import oliveira.fabio.challenge52.util.Event
import kotlin.coroutines.CoroutineContext

class GoalListViewModel(private val goalWithWeeksRepository: GoalWithWeeksRepository) : ViewModel(), CoroutineScope {
    private val job = SupervisorJob()

    val mutableLiveDataGoals by lazy { MutableLiveData<Event<List<GoalWithWeeks>>>() }

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
}