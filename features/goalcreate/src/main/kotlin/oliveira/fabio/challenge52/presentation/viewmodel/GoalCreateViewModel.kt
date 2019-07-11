package oliveira.fabio.challenge52.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import oliveira.fabio.challenge52.domain.interactor.GoalCreateInteractorImpl
import oliveira.fabio.challenge52.extensions.toFloatCurrency
import oliveira.fabio.challenge52.persistence.model.entity.Goal
import oliveira.fabio.challenge52.presentation.state.GoalCreateState
import kotlin.coroutines.CoroutineContext

class GoalCreateViewModel(private val goalCreateInteractor: GoalCreateInteractorImpl) :
    ViewModel(), CoroutineScope {

    private val _goalCreateState by lazy { MutableLiveData<GoalCreateState>() }

    val goalCreateState: LiveData<GoalCreateState>
        get() = _goalCreateState

    private val job by lazy { SupervisorJob() }
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    public override fun onCleared() {
        super.onCleared()
        if (job.isActive) job.cancel()
    }

    fun createGoal(goal: Goal) {
        launch {
            SuspendableResult.of<Long, Exception> { goalCreateInteractor.addGoal(goal) }.fold(
                success = {
                    goal.id = it
                    SuspendableResult.of<List<Long>, Exception> { goalCreateInteractor.addWeeks(goal, TOTAL_WEEKS) }
                        .fold(success = {
                            _goalCreateState.postValue(GoalCreateState.Success)
                        }, failure = {
                            _goalCreateState.postValue(GoalCreateState.Error)
                        })

                }, failure = {
                    _goalCreateState.postValue(GoalCreateState.Error)
                })
        }
    }

    fun isMoreOrEqualsOne(value: String): Boolean {
        if (value.isNotEmpty()) {
            return value.toFloatCurrency() >= MONEY_MIN
        }
        return false
    }

    companion object {
        private const val MONEY_MIN = 1.0
        private const val TOTAL_WEEKS = 52
    }
}