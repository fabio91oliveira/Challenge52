package oliveira.fabio.challenge52.creategoal.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import features.goalcreate.R
import oliveira.fabio.challenge52.creategoal.presentation.action.GoalChooseNameActions
import oliveira.fabio.challenge52.creategoal.presentation.viewstate.GoalChooseNameViewState
import oliveira.fabio.challenge52.presentation.vo.GoalToSave
import timber.log.Timber

internal class GoalChooseNameViewModel(private val state: SavedStateHandle) : ViewModel() {

    private val _goalChooseNameActions by lazy { MutableLiveData<GoalChooseNameActions>() }
    val goalChooseNameActions by lazy { _goalChooseNameActions }
    private val _goalChooseNameViewState by lazy { MutableLiveData<GoalChooseNameViewState>() }
    val goalChooseNameViewState by lazy { _goalChooseNameViewState }

    private val goal by lazy {
        state.get<GoalToSave>(GOAL) ?: initializerError() as GoalToSave
    }

    fun isValidName(name: String) {
        if (name.isNullOrBlank().not().and(name.length >= MIN_CHARACTER)) {
            changeGoalName(name)
            GoalChooseNameViewState(
                isContinueButtonEnabled = true
            ).setState()
        } else {
            GoalChooseNameViewState(
                isContinueButtonEnabled = false
            ).setState()
        }
    }

    fun goToNextStep() {
        goal.period?.also {
            GoalChooseNameActions.GoToCreateGoalScreen(goal).sendAction()
        } ?: run {
            GoalChooseNameActions.GoToDefinePeriodScreen(goal).sendAction()
        }
    }

    fun setNameGoal() {
        GoalChooseNameActions.SetNameGoal(goal.name).sendAction()
    }

    private fun initializerError() {
        GoalChooseNameActions.CriticalError(
            R.string.goal_choose_name_error_title,
            R.string.goal_choose_name_list_error_description
        ).sendAction()
        Timber.e(ExceptionInInitializerError())
    }

    private fun changeGoalName(name: String) {
        goal.name = name
    }

    private fun GoalChooseNameActions.sendAction() {
        _goalChooseNameActions.value = this
    }

    private fun GoalChooseNameViewState.setState() {
        _goalChooseNameViewState.value = this
    }

    companion object {
        private const val MIN_CHARACTER = 5
        private const val GOAL = "goal"
    }
}