package oliveira.fabio.challenge52.home.goalslists.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.result.coroutines.SuspendableResult
import features.goalhome.R
import kotlinx.coroutines.launch
import oliveira.fabio.challenge52.home.goalslists.domain.usecase.GetAllDoneGoals
import oliveira.fabio.challenge52.home.goalslists.domain.usecase.GetAllOpenedGoals
import oliveira.fabio.challenge52.home.goalslists.donegoalslist.presentation.action.DoneGoalsActions
import oliveira.fabio.challenge52.home.goalslists.donegoalslist.presentation.action.DoneGoalsStateResources
import oliveira.fabio.challenge52.home.goalslists.donegoalslist.presentation.viewstate.DoneGoalsViewState
import oliveira.fabio.challenge52.home.goalslists.openedgoalslist.presentation.action.OpenedGoalsActions
import oliveira.fabio.challenge52.home.goalslists.openedgoalslist.presentation.action.OpenedGoalsStateResources
import oliveira.fabio.challenge52.home.goalslists.openedgoalslist.presentation.viewstate.OpenedGoalsViewState
import oliveira.fabio.challenge52.home.goalslists.presentation.viewstate.GoalsListsViewState
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks
import timber.log.Timber

class GoalsListsViewModel(
    private val getAllOpenedGoals: GetAllOpenedGoals,
    private val getAllDoneGoals: GetAllDoneGoals
) : ViewModel() {

    private val _goalsListsViewState by lazy { MutableLiveData<GoalsListsViewState>() }
    val goalsListsViewState by lazy { _goalsListsViewState }

    private val _openedGoalsActions by lazy { MutableLiveData<OpenedGoalsActions>() }
    val openedGoalsActions by lazy { _openedGoalsActions }
    private val _openedGoalsViewState by lazy { MutableLiveData<OpenedGoalsViewState>() }
    val openedGoalsViewState by lazy { _openedGoalsViewState }

    private val _doneGoalsActions by lazy { MutableLiveData<DoneGoalsActions>() }
    val doneGoalsActions by lazy { _doneGoalsActions }
    private val _doneGoalsViewState by lazy { MutableLiveData<DoneGoalsViewState>() }
    val doneGoalsViewState by lazy { _doneGoalsViewState }

    init {
        initViewStates()
        getUserName()
        listOpenedGoals()
        listDoneGoals()
    }

    fun showAddButton() {
        setOpenedGoalsViewState {
            it.copy(isAddButtonVisible = true)
        }
    }

    fun hideAddButton() {
        setOpenedGoalsViewState {
            it.copy(isAddButtonVisible = false)
        }
    }

    fun listOpenedGoals() {
        viewModelScope.launch {
            setOpenedGoalsViewState {
                OpenedGoalsViewState(isLoading = true)
            }
            SuspendableResult.of<List<GoalWithWeeks>, Exception> {
                getAllOpenedGoals()
            }
                .fold(
                    success = { list ->
                        if (list.isNotEmpty()) {
                            OpenedGoalsActions.OpenedGoalsList(list).sendAction()
                            setGoalsListsViewState {
                                it.copy(totalTasks = list.size)
                            }
                            setOpenedGoalsViewState {
                                OpenedGoalsViewState(
                                    isLoading = false,
                                    isOpenedGoalsListVisible = true,
                                    isAddButtonVisible = true
                                )
                            }
                        } else {
                            OpenedGoalsActions.Empty(
                                OpenedGoalsStateResources(
                                    R.drawable.ic_not_found,
                                    R.string.goals_lists_no_opened_goals_title,
                                    R.string.goals_lists_no_opened_goals_description
                                )
                            ).sendAction()
                            setGoalsListsViewState {
                                it.copy(totalTasks = list.size)
                            }
                            setOpenedGoalsViewState {
                                OpenedGoalsViewState(
                                    isLoading = false,
                                    isEmptyStateVisible = true,
                                    isAddButtonVisible = true
                                )
                            }
                        }
                    },
                    failure = {
                        OpenedGoalsActions.Error(
                            OpenedGoalsStateResources(
                                R.drawable.ic_error_connection,
                                R.string.goals_lists_error_title,
                                R.string.goals_lists_error_description,
                                R.string.goals_lists_error_button
                            )
                        ).sendAction()
                        setOpenedGoalsViewState {
                            OpenedGoalsViewState(
                                isErrorVisible = true,
                                isAddButtonVisible = true
                            )
                        }
                        Timber.e(it)
                    }
                )
        }
    }

    fun listDoneGoals() {
        viewModelScope.launch {
            setDoneGoalsViewState {
                DoneGoalsViewState(isLoading = true)
            }
            SuspendableResult.of<List<GoalWithWeeks>, Exception> {
                getAllDoneGoals()
            }
                .fold(
                    success = {
                        if (it.isNotEmpty()) {
                            DoneGoalsActions.DoneGoalsList(it).sendAction()
                            setDoneGoalsViewState {
                                DoneGoalsViewState(
                                    isLoading = false,
                                    isDoneGoalsListVisible = true
                                )
                            }
                        } else {
                            DoneGoalsActions.Empty(
                                DoneGoalsStateResources(
                                    R.drawable.ic_not_found,
                                    R.string.goals_lists_no_done_goals_title,
                                    R.string.goals_lists_no_done_goals_description
                                )
                            ).sendAction()
                            setDoneGoalsViewState {
                                DoneGoalsViewState(
                                    isLoading = false,
                                    isEmptyStateVisible = true
                                )
                            }
                        }
                    },
                    failure = {
                        DoneGoalsActions.Error(
                            DoneGoalsStateResources(
                                R.drawable.ic_error_connection,
                                R.string.goals_lists_error_title,
                                R.string.goals_lists_error_description,
                                R.string.goals_lists_error_button
                            )
                        ).sendAction()
                        setDoneGoalsViewState {
                            DoneGoalsViewState(
                                isErrorVisible = true
                            )
                        }
                        Timber.e(it)
                    }
                )
        }
    }

    private fun getUserName() {
        setGoalsListsViewState {
            it.copy(userName = "Fulaninho")
        }
    }

    fun showMessageGoalHasBeenCreated() =
        OpenedGoalsActions.ShowMessage(R.string.goals_list_a_goal_has_been_created).sendAction()

    fun showMessageHasOneOpenedGoalDeleted() =
        OpenedGoalsActions.ShowMessage(R.string.goals_list_a_goal_has_been_deleted).sendAction()

    fun showMessageHasOpenedGoalBeenUpdated() =
        OpenedGoalsActions.ShowMessage(R.string.goals_list_a_goal_has_been_updated).sendAction()

    fun showMessageHasOpenedGoalBeenCompleted() =
        OpenedGoalsActions.ShowMessage(R.string.goals_list_a_goal_has_been_moved_to_done).sendAction()

    fun showMessageHasOneDoneGoalDeleted() =
        DoneGoalsActions.ShowMessage(R.string.goals_list_a_goal_has_been_deleted).sendAction()

    private fun initViewStates() {
        _openedGoalsViewState.value = OpenedGoalsViewState.init()
        _doneGoalsViewState.value = DoneGoalsViewState.init()
        _goalsListsViewState.value = GoalsListsViewState.init()
    }

    private fun OpenedGoalsActions.sendAction() {
        _openedGoalsActions.value = this
    }

    private fun DoneGoalsActions.sendAction() {
        _doneGoalsActions.value = this
    }

    private fun setGoalsListsViewState(state: (GoalsListsViewState) -> GoalsListsViewState) {
        _goalsListsViewState.value?.also {
            _goalsListsViewState.value = state(it)
        }
    }

    private fun setOpenedGoalsViewState(state: (OpenedGoalsViewState) -> OpenedGoalsViewState) {
        _openedGoalsViewState.value?.also {
            _openedGoalsViewState.value = state(it)
        }
    }

    private fun setDoneGoalsViewState(state: (DoneGoalsViewState) -> DoneGoalsViewState) {
        _doneGoalsViewState.value?.also {
            _doneGoalsViewState.value = state(it)
        }
    }
}