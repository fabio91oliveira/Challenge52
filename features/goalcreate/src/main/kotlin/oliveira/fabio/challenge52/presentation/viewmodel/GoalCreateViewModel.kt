package oliveira.fabio.challenge52.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.launch
import oliveira.fabio.challenge52.domain.usecase.AddGoalUseCase
import oliveira.fabio.challenge52.domain.usecase.AddWeeksUseCase
import oliveira.fabio.challenge52.extensions.removeMoneyMask
import oliveira.fabio.challenge52.extensions.toDate
import oliveira.fabio.challenge52.extensions.toFloatCurrency
import oliveira.fabio.challenge52.persistence.model.entity.Goal
import oliveira.fabio.challenge52.presentation.action.GoalCreateActions
import oliveira.fabio.challenge52.presentation.viewstate.GoalCreateViewState
import java.text.DateFormat

class GoalCreateViewModel(
    private val addGoalUseCase: AddGoalUseCase,
    private val addWeeksUseCase: AddWeeksUseCase
) :
    ViewModel() {

    private val _goalCreateActions by lazy { MutableLiveData<GoalCreateActions>() }
    val goalCreateActions: LiveData<GoalCreateActions> = _goalCreateActions

    private val _goalCreateViewState by lazy { MutableLiveData<GoalCreateViewState>() }
    val goalCreateViewState: LiveData<GoalCreateViewState> = _goalCreateViewState

    fun createGoal(
        initialDate: String,
        name: String,
        valueToStart: String
    ) {
        viewModelScope.launch {
            Goal(
                initialDate = initialDate.toDate(DateFormat.SHORT),
                name = name,
                valueToStart = valueToStart.removeMoneyMask().toFloatCurrency()
            ).apply {
                SuspendableResult.of<Long, Exception> { addGoalUseCase(this@apply) }.fold(
                    success = {
                        SuspendableResult.of<List<Long>, Exception> {
                            addWeeksUseCase(this@apply, it)
                        }
                            .fold(success = {
                                GoalCreateActions.ShowSuccess.run()
                            }, failure = {
                                GoalCreateActions.ShowError.run()
                            })

                    }, failure = {
                        GoalCreateActions.ShowError.run()
                    })
            }
        }
    }

    fun validateFields(name: String, value: String) = GoalCreateViewState(
        isCreateButtonEnable = (name.isNotEmpty() && isMoreOrEqualsOne(value.removeMoneyMask()))
    ).newState()

    private fun isMoreOrEqualsOne(value: String): Boolean {
        if (value.isNotEmpty()) {
            return value.toFloatCurrency() >= MONEY_MIN
        }
        return false
    }

    private fun GoalCreateActions.run() {
        _goalCreateActions.value = this
    }

    private fun GoalCreateViewState.newState() {
        _goalCreateViewState.value = this
    }

    companion object {
        private const val MONEY_MIN = 1.0
    }
}