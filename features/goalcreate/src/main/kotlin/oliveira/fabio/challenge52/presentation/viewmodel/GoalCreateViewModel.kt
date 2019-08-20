package oliveira.fabio.challenge52.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import oliveira.fabio.challenge52.domain.usecase.GoalCreateUseCase
import oliveira.fabio.challenge52.extensions.toFloatCurrency
import oliveira.fabio.challenge52.persistence.model.entity.Goal
import oliveira.fabio.challenge52.presentation.state.GoalCreateState
import java.util.*
import kotlin.coroutines.CoroutineContext

class GoalCreateViewModel(private val goalCreateUseCase: GoalCreateUseCase) :
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
            SuspendableResult.of<Long, Exception> { goalCreateUseCase.addGoal(goal) }.fold(
                success = {
                    SuspendableResult.of<List<Long>, Exception> { goalCreateUseCase.addWeeks(goal, it) }
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

    fun isAllFieldsFilled(name: String, value: String) =
        name.isNotEmpty() && (isMoreOrEqualsOne(removeMoneyMask(value)))

    fun getFloatCurrencyValue(value: String) = removeMoneyMask(value).toFloatCurrency()

    private fun removeMoneyMask(value: String): String {
        val defaultCurrencySymbol = Currency.getInstance(Locale.getDefault()).symbol
        val regexPattern = "[$defaultCurrencySymbol,.]"

        return Regex(regexPattern).replace(value, "")
    }

    private fun isMoreOrEqualsOne(value: String): Boolean {
        if (value.isNotEmpty()) {
            return value.toFloatCurrency() >= MONEY_MIN
        }
        return false
    }

    companion object {
        private const val MONEY_MIN = 1.0
    }
}