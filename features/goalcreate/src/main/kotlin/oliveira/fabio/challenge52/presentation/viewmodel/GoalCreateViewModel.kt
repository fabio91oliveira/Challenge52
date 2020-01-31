package oliveira.fabio.challenge52.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.launch
import oliveira.fabio.challenge52.domain.usecase.AddGoalUseCase
import oliveira.fabio.challenge52.extensions.removeMoneyMask
import oliveira.fabio.challenge52.extensions.toFloatCurrency
import oliveira.fabio.challenge52.presentation.action.GoalCreateActions
import oliveira.fabio.challenge52.presentation.viewstate.GoalCreateViewState
import timber.log.Timber

class GoalCreateViewModel(
    private val addGoalUseCase: AddGoalUseCase
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
            SuspendableResult.of<Unit, Exception> {
                addGoalUseCase(
                    initialDate,
                    name,
                    valueToStart
                )
            }.fold(
                success = {
                    GoalCreateActions.ShowSuccess.run()
                }, failure = {
                    GoalCreateActions.ShowError.run()
                    Timber.e(it)
                })
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