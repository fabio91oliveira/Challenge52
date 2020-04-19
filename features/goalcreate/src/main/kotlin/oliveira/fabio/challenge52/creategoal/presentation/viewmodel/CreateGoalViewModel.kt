package oliveira.fabio.challenge52.creategoal.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.result.coroutines.SuspendableResult
import features.goalcreate.R
import kotlinx.coroutines.launch
import oliveira.fabio.challenge52.creategoal.domain.usecase.AddGoalUseCase
import oliveira.fabio.challenge52.creategoal.domain.usecase.AddItemsUseCase
import oliveira.fabio.challenge52.creategoal.domain.usecase.CalculateMoneyUseCase
import oliveira.fabio.challenge52.creategoal.domain.usecase.CreateItemsUseCase
import oliveira.fabio.challenge52.creategoal.domain.usecase.GetMoneySuggestionsUseCase
import oliveira.fabio.challenge52.creategoal.presentation.action.CreateGoalActions
import oliveira.fabio.challenge52.creategoal.presentation.viewstate.CreateGoalViewState
import oliveira.fabio.challenge52.creategoal.presentation.vo.MoneySuggestion
import oliveira.fabio.challenge52.extensions.toFloatCurrency
import oliveira.fabio.challenge52.extensions.toStringMoney
import oliveira.fabio.challenge52.presentation.vo.GoalToSave
import oliveira.fabio.challenge52.presentation.vo.PeriodEnum
import timber.log.Timber

internal class CreateGoalViewModel(
    private val state: SavedStateHandle,
    private val getMoneySuggestionsUseCase: GetMoneySuggestionsUseCase,
    private val calculateMoneyUseCase: CalculateMoneyUseCase,
    private val createItemsUseCase: CreateItemsUseCase,
    private val addGoalUseCase: AddGoalUseCase,
    private val addItemsUseCase: AddItemsUseCase
) :
    ViewModel() {

    private val _createGoalActions by lazy { MutableLiveData<CreateGoalActions>() }
    val createGoalActions: LiveData<CreateGoalActions> = _createGoalActions

    private val _createGoalViewState by lazy { MutableLiveData<CreateGoalViewState>() }
    val createGoalViewState: LiveData<CreateGoalViewState> = _createGoalViewState

    private val goal by lazy {
        state.get<GoalToSave>(GOAL) ?: initializerError() as GoalToSave
    }

    init {
        initViewState()
        setPeriodText()
        getMoneySuggestions()
    }

    fun createGoal() {
        viewModelScope.launch {
            SuspendableResult.of<Unit, Exception> {
                createItemsUseCase(goal)
            }.fold(
                success = {
                    SuspendableResult.of<Long, Exception> {
                        addGoalUseCase(goal)
                    }.fold(
                        success = {
                            SuspendableResult.of<Unit, Exception> {
                                addItemsUseCase(goal, it)
                            }.fold(
                                success = {
                                    CreateGoalActions.GoalCreated.sendAction()
                                },
                                failure = {
                                    CreateGoalActions.CriticalError(
                                        R.string.goal_choose_name_error_title,
                                        R.string.goal_choose_name_list_error_description
                                    ).sendAction()
                                    Timber.e(it)
                                }
                            )
                        },
                        failure = {
                            CreateGoalActions.CriticalError(
                                R.string.goal_choose_name_error_title,
                                R.string.goal_choose_name_list_error_description
                            ).sendAction()
                            Timber.e(it)
                        }
                    )
                },
                failure = {
                    CreateGoalActions.CriticalError(
                        R.string.goal_choose_name_error_title,
                        R.string.goal_choose_name_list_error_description
                    ).sendAction()
                    Timber.e(it)
                }
            )
        }
    }

    fun calculateTotalMoney(money: String) {
        viewModelScope.launch {
            setViewState {
                it.copy(isCalculating = true)
            }
            SuspendableResult.of<Double, Exception> {
                calculateMoneyUseCase(goal, money)
            }.fold(
                success = {
                    setViewState { state ->
                        state.copy(
                            isCalculating = false,
                            totalMoney = it.toStringMoney(useCurrency = true),
                            isCreateButtonEnable = it > 0
                        )
                    }
                },
                failure = {
                    CreateGoalActions.CriticalError(
                        R.string.goal_choose_name_error_title,
                        R.string.goal_choose_name_list_error_description
                    ).sendAction()
                    Timber.e(it)
                }
            )
        }
    }

    private fun getMoneySuggestions() {
        viewModelScope.launch {
            SuspendableResult.of<List<MoneySuggestion>, Exception> {
                getMoneySuggestionsUseCase()
            }.fold(
                success = {
                    CreateGoalActions.ShowMoneySuggestions(it).sendAction()
                },
                failure = {
                    Timber.e(it)
                }
            )
        }
    }

    private fun setPeriodText() {
        when (goal.period) {
            PeriodEnum.DAILY -> {
                setViewState {
                    it.copy(periodType = R.string.create_goal_period_day)
                }
            }
            PeriodEnum.WEEKLY -> {
                setViewState {
                    it.copy(periodType = R.string.create_goal_period_week)
                }
            }
            PeriodEnum.MONTHLY -> {
                setViewState {
                    it.copy(periodType = R.string.create_goal_period_month)
                }
            }
        }
    }

    private fun isMoreOrEqualsOne(value: String): Boolean {
        if (value.isNotEmpty()) {
            return value.toFloatCurrency() >= MONEY_MIN
        }
        return false
    }

    private fun initializerError() {
        CreateGoalActions.CriticalError(
            R.string.goal_choose_name_error_title,
            R.string.goal_choose_name_list_error_description
        ).sendAction()
        Timber.e(ExceptionInInitializerError())
    }

    private fun CreateGoalActions.sendAction() {
        _createGoalActions.value = this
    }

    private fun initViewState() {
        _createGoalViewState.value = CreateGoalViewState.init()
    }

    private fun setViewState(state: (CreateGoalViewState) -> CreateGoalViewState) {
        _createGoalViewState.value?.also {
            _createGoalViewState.value = state(it)
        }
    }

    companion object {
        private const val MONEY_MIN = 1.0
        private const val GOAL = "goal"
    }
}