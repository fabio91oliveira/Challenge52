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

    init {
        initViewState()
    }

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
                    GoalCreateActions.Success.sendAction()
                }, failure = {
                    GoalCreateActions.Error.sendAction()
                    Timber.e(it)
                })
        }
    }

    fun validateFields(name: String, value: String) {
        setViewState {
            GoalCreateViewState(
                isCreateButtonEnable = (name.isNotEmpty() && isMoreOrEqualsOne(value.removeMoneyMask()))
            )
        }
    }

    private fun isMoreOrEqualsOne(value: String): Boolean {
        if (value.isNotEmpty()) {
            return value.toFloatCurrency() >= MONEY_MIN
        }
        return false
    }

    private fun GoalCreateActions.sendAction() {
        _goalCreateActions.value = this
    }

    private fun initViewState() {
        _goalCreateViewState.value = GoalCreateViewState.init()
    }

    private fun setViewState(state: (GoalCreateViewState) -> GoalCreateViewState) {
        _goalCreateViewState.value?.also {
            _goalCreateViewState.value = state(it)
        }
    }

    companion object {
        private const val MONEY_MIN = 1.0
    }
}