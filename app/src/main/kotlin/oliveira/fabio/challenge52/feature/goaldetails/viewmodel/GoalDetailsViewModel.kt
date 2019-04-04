package oliveira.fabio.challenge52.feature.goaldetails.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import oliveira.fabio.challenge52.model.entity.Week
import oliveira.fabio.challenge52.model.repository.GoalWithWeeksRepository
import oliveira.fabio.challenge52.util.Event
import kotlin.coroutines.CoroutineContext

class GoalDetailsViewModel(private val goalWithWeeksRepository: GoalWithWeeksRepository) : ViewModel(), CoroutineScope {

    val mutableLiveDataUpdated by lazy { MutableLiveData<Event<Boolean>>() }

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    public override fun onCleared() {
        super.onCleared()
        if (job.isActive) job.cancel()
    }

    fun updateWeek(week: Week) {
        launch {
            SuspendableResult.of<Unit, Exception> { goalWithWeeksRepository.updateWeek(week) }
                .fold(
                    success = {
                        mutableLiveDataUpdated.postValue(Event(true))
                    },
                    failure = {
                        mutableLiveDataUpdated.postValue(Event(false))
                    }
                )
        }
    }
}