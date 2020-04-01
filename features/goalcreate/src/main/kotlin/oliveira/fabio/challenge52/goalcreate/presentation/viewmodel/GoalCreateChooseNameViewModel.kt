package oliveira.fabio.challenge52.goalcreate.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import oliveira.fabio.challenge52.goalcreate.presentation.viewstate.GoalCreateChooseNameViewState

internal class GoalCreateChooseNameViewModel : ViewModel() {

    private val _goalCreateChooseNameViewState by lazy { MutableLiveData<GoalCreateChooseNameViewState>() }
    val goalCreateChooseNameViewState by lazy { _goalCreateChooseNameViewState }

    fun isValidName(name: String) {
        GoalCreateChooseNameViewState(
            isContinueButtonEnabled = (name.isNullOrBlank().not().and(name.length >= MIN_CHARACTER))
        ).setState()
    }

    private fun GoalCreateChooseNameViewState.setState() {
        _goalCreateChooseNameViewState.value = this
    }

    companion object {
        private const val MIN_CHARACTER = 5
    }
}