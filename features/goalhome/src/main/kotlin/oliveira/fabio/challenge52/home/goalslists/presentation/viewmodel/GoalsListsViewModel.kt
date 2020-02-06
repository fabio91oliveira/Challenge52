package oliveira.fabio.challenge52.home.goalslists.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.result.coroutines.SuspendableResult
import features.goalhome.R
import kotlinx.coroutines.launch
import oliveira.fabio.challenge52.home.goalslists.domain.usecase.GetAllDoneGoals
import oliveira.fabio.challenge52.home.goalslists.domain.usecase.GetAllOpenedGoals
import oliveira.fabio.challenge52.home.goalslists.domain.usecase.RemoveGoalsUseCase
import oliveira.fabio.challenge52.home.goalslists.donegoalslist.presentation.action.DoneGoalsActions
import oliveira.fabio.challenge52.home.goalslists.donegoalslist.presentation.viewstate.DoneGoalsViewState
import oliveira.fabio.challenge52.home.goalslists.openedgoalslist.presentation.action.OpenedGoalsActions
import oliveira.fabio.challenge52.home.goalslists.openedgoalslist.presentation.viewstate.OpenedGoalsViewState
import oliveira.fabio.challenge52.persistence.model.entity.Goal
import oliveira.fabio.challenge52.persistence.model.entity.Week
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks
import timber.log.Timber

class GoalsListsViewModel(
    private val getAllOpenedGoals: GetAllOpenedGoals,
    private val getAllDoneGoals: GetAllDoneGoals,
    private val removeGoalsUseCase: RemoveGoalsUseCase
) : ViewModel() {

    private val openedGoalsRemoveList by lazy { mutableListOf<GoalWithWeeks>() }
    private val _openedGoalsActions by lazy { MutableLiveData<OpenedGoalsActions>() }
    val openedGoalsActions by lazy { _openedGoalsActions }
    private val _openedGoalsViewState by lazy { MutableLiveData<OpenedGoalsViewState>() }
    val openedGoalsViewState by lazy { _openedGoalsViewState }

    private val doneGoalsRemoveList by lazy { mutableListOf<GoalWithWeeks>() }
    private val _doneGoalsActions by lazy { MutableLiveData<DoneGoalsActions>() }
    val doneGoalsActions by lazy { _doneGoalsActions }
    private val _doneGoalsViewState by lazy { MutableLiveData<DoneGoalsViewState>() }
    val doneGoalsViewState by lazy { _doneGoalsViewState }

    init {
        listOpenedGoals()
        listDoneGoals()
    }

    fun showAddButton() {
        changeOpenedGoalsViewState {
            it.copy(isAddButtonVisible = true)
        }
    }

    fun hideAddButton() {
        changeOpenedGoalsViewState {
            it.copy(isAddButtonVisible = false)
        }
    }

    fun listOpenedGoals() {
        viewModelScope.launch {
            OpenedGoalsViewState(isLoading = true).newState()
            SuspendableResult.of<List<GoalWithWeeks>, Exception> {
                getAllOpenedGoals()
            }
                .fold(
                    success = {
                        if (it.isNotEmpty()) {
                            OpenedGoalsActions.OpenedGoalsList(it).run()
                            OpenedGoalsViewState(
                                isLoading = false,
                                isOpenedGoalsListVisible = true,
                                isAddButtonVisible = true
                            ).newState()
                        } else {
                            OpenedGoalsActions.ClearList.run()
                            OpenedGoalsViewState(
                                isLoading = false,
                                isEmptyStateVisible = true,
                                isAddButtonVisible = true
                            ).newState()
                        }
                    },
                    failure = {
                        OpenedGoalsActions.Error(R.string.goals_list_error).run()
                        OpenedGoalsViewState(
                            isErrorVisible = true
                        ).newState()
                        Timber.e(it)
                    }
                )
        }
    }

    fun listDoneGoals() {
        viewModelScope.launch {
            DoneGoalsViewState(isLoading = true).newState()
            SuspendableResult.of<List<GoalWithWeeks>, Exception> {
                getAllDoneGoals()
            }
                .fold(
                    success = {
                        if (it.isNotEmpty()) {
                            DoneGoalsActions.DoneGoalsList(it).run()
                            DoneGoalsViewState(
                                isLoading = false,
                                isDoneGoalsListVisible = true
                            ).newState()
                        } else {
                            DoneGoalsActions.ClearList.run()
                            DoneGoalsViewState(
                                isLoading = false,
                                isEmptyStateVisible = true
                            ).newState()
                        }
                    },
                    failure = {
                        DoneGoalsActions.Error(R.string.goals_list_error).run()
                        DoneGoalsViewState(isErrorVisible = true).newState()
                        Timber.e(it)
                    }
                )
        }
    }

    fun removeOpenedGoals() {
        viewModelScope.launch {
            SuspendableResult.of<Unit, Exception> { removeGoalsUseCase(openedGoalsRemoveList) }
                .fold(
                    success = {
                        openedGoalsRemoveList.clear()
                        OpenedGoalsActions.RefreshList.run()
                        OpenedGoalsActions.ShowMessage(R.string.goals_list_a_goal_has_been_deleted)
                    }, failure = {
                        OpenedGoalsActions.Error(R.string.goals_list_error_delete).run()
                        OpenedGoalsViewState(
                            isErrorVisible = true
                        ).newState()
                        Timber.e(it)
                    })
        }
    }

    fun removeDoneGoals() {
        viewModelScope.launch {
            val goalsToRemove = arrayListOf<Goal>()
            val weeksToRemove = arrayListOf<Week>()

            doneGoalsRemoveList.forEach {
                goalsToRemove.add(it.goal)
                weeksToRemove.addAll(it.weeks)
            }

            SuspendableResult.of<Unit, Exception> { removeGoalsUseCase(doneGoalsRemoveList) }.fold(
                success = {
                    doneGoalsRemoveList.clear()
                    DoneGoalsActions.RefreshList.run()
                    DoneGoalsActions.ShowMessage(R.string.goals_list_a_goal_has_been_deleted)
                        .run()
                }, failure = {
                    DoneGoalsActions.Error(R.string.goals_list_error_delete).run()
                    DoneGoalsViewState(isErrorVisible = true).newState()
                    Timber.e(it)
                })
        }
    }

    fun showMessageGoalHasBeenCreated() =
        OpenedGoalsActions.ShowMessage(R.string.goals_list_a_goal_has_been_created).run()

    fun showMessageHasOneOpenedGoalDeleted() =
        OpenedGoalsActions.ShowMessage(R.string.goals_list_a_goal_has_been_deleted).run()

    fun showMessageHasOpenedGoalBeenUpdated() =
        OpenedGoalsActions.ShowMessage(R.string.goals_list_a_goal_has_been_updated).run()

    fun showMessageHasOpenedGoalBeenCompleted() =
        OpenedGoalsActions.ShowMessage(R.string.goals_list_a_goal_has_been_moved_to_done).run()

    fun showMessageHasOneDoneGoalDeleted() =
        DoneGoalsActions.ShowMessage(R.string.goals_list_a_goal_has_been_deleted).run()

    fun addOpenedGoalToListToRemove(goalWithWeeks: GoalWithWeeks) {
        with(openedGoalsRemoveList) {
            add(goalWithWeeks)
            OpenedGoalsViewState(
                isDeleteButtonVisible = true,
                isOpenedGoalsListVisible = true
            ).newState()
        }
    }

    fun removeOpenedGoalFromListToRemove(goalWithWeeks: GoalWithWeeks) {
        with(openedGoalsRemoveList) {
            remove(goalWithWeeks)
            OpenedGoalsViewState(
                isDeleteButtonVisible = isEmpty().not(),
                isAddButtonVisible = isEmpty(),
                isOpenedGoalsListVisible = true
            ).newState()
        }
    }

    fun addDoneGoalToListToRemove(goalWithWeeks: GoalWithWeeks) {
        with(doneGoalsRemoveList) {
            add(goalWithWeeks)
            DoneGoalsViewState(
                isDeleteButtonVisible = true,
                isDoneGoalsListVisible = true
            ).newState()
        }
    }

    fun removeDoneGoalFromListToRemove(goalWithWeeks: GoalWithWeeks) {
        with(doneGoalsRemoveList) {
            remove(goalWithWeeks)
            DoneGoalsViewState(
                isDeleteButtonVisible = isEmpty().not(),
                isDoneGoalsListVisible = true
            ).newState()
        }
    }

    fun showRemoveOpenedGoalsConfirmationDialog() {
        OpenedGoalsActions.ShowRemoveConfirmationDialog(
            R.plurals.goal_details_are_you_sure_removes,
            openedGoalsRemoveList.size
        ).run()
    }

    fun showRemoveDoneGoalsConfirmationDialog() {
        DoneGoalsActions.ShowRemoveConfirmationDialog(
            R.plurals.goal_details_are_you_sure_removes,
            doneGoalsRemoveList.size
        ).run()
    }

    fun showErrorWhenTryToOpenDetails() =
        DoneGoalsActions.Error(R.string.goal_details_list_error_message).run()

    private fun OpenedGoalsActions.run() {
        _openedGoalsActions.value = this
    }

    private fun DoneGoalsActions.run() {
        _doneGoalsActions.value = this
    }

    private fun OpenedGoalsViewState.newState() {
        _openedGoalsViewState.value = this
    }

    private fun DoneGoalsViewState.newState() {
        _doneGoalsViewState.value = this
    }

    private fun changeOpenedGoalsViewState(state: (OpenedGoalsViewState) -> OpenedGoalsViewState) {
        _openedGoalsViewState.value?.also {
            _openedGoalsViewState.value = state(it)
        }
    }
}