package oliveira.fabio.challenge52.home.goalslists.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.kittinunf.result.coroutines.SuspendableResult
import features.goalhome.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import oliveira.fabio.challenge52.domain.GoalWithWeeksRepository
import oliveira.fabio.challenge52.domain.repository.GoalRepository
import oliveira.fabio.challenge52.domain.repository.WeekRepository
import oliveira.fabio.challenge52.home.goalslists.donegoalslist.presentation.action.DoneGoalsActions
import oliveira.fabio.challenge52.home.goalslists.donegoalslist.presentation.viewstate.DoneGoalsViewState
import oliveira.fabio.challenge52.home.goalslists.openedgoalslist.presentation.action.OpenedGoalsActions
import oliveira.fabio.challenge52.home.goalslists.openedgoalslist.presentation.viewstate.OpenedGoalsViewState
import oliveira.fabio.challenge52.persistence.model.entity.Goal
import oliveira.fabio.challenge52.persistence.model.entity.Week
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks
import kotlin.coroutines.CoroutineContext

class GoalsListsViewModel(
    private val goalWithWeeksRepository: GoalWithWeeksRepository,
    private val goalRepository: GoalRepository,
    private val weekRepository: WeekRepository
) : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

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

    public override fun onCleared() {
        super.onCleared()
        if (job.isActive) job.cancel()
    }

    fun showAddButton() {
        OpenedGoalsViewState(isAddButtonVisible = true, isOpenedGoalsListVisible = true).newState()
    }

    fun hideAddButton() {
        OpenedGoalsViewState(isAddButtonVisible = false, isOpenedGoalsListVisible = true).newState()
    }

    fun listOpenedGoals() {
        launch {
            OpenedGoalsViewState(isLoading = true).newState()
            SuspendableResult.of<List<GoalWithWeeks>, Exception> { goalWithWeeksRepository.getAllGoalsWithWeeks() }
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
                    }
                )
        }
    }

    fun listDoneGoals() {
        launch {
            DoneGoalsViewState(isLoading = true).newState()
            SuspendableResult.of<List<GoalWithWeeks>, Exception> { goalWithWeeksRepository.getDoneAllGoalsWithWeeks() }
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
                    }
                )
        }
    }

    fun removeOpenedGoals() {
        launch {
            val goalsToRemove = arrayListOf<Goal>()
            val weeksToRemove = arrayListOf<Week>()

            openedGoalsRemoveList.forEach {
                goalsToRemove.add(it.goal)
                weeksToRemove.addAll(it.weeks)
            }

            SuspendableResult.of<Int, Exception> { goalRepository.removeGoals(goalsToRemove) }.fold(
                success = {
                    SuspendableResult.of<Int, Exception> { weekRepository.removeWeeks(weeksToRemove) }
                        .fold(success = {
                            openedGoalsRemoveList.clear()
                            OpenedGoalsActions.RefreshList.run()
                            OpenedGoalsActions.ShowMessage(R.string.goals_list_a_goal_has_been_deleted)
                                .run()
                        }, failure = {
                            OpenedGoalsActions.Error(R.string.goals_list_error_delete).run()
                            OpenedGoalsViewState(
                                isErrorVisible = true
                            ).newState()
                        })

                }, failure = {
                    OpenedGoalsActions.Error(R.string.goals_list_error_delete).run()
                    OpenedGoalsViewState(
                        isErrorVisible = true
                    ).newState()
                })
        }
    }

    fun removeDoneGoals() {
        launch {
            val goalsToRemove = arrayListOf<Goal>()
            val weeksToRemove = arrayListOf<Week>()

            doneGoalsRemoveList.forEach {
                goalsToRemove.add(it.goal)
                weeksToRemove.addAll(it.weeks)
            }

            SuspendableResult.of<Int, Exception> { goalRepository.removeGoals(goalsToRemove) }.fold(
                success = {
                    SuspendableResult.of<Int, Exception> { weekRepository.removeWeeks(weeksToRemove) }
                        .fold(success = {
                            doneGoalsRemoveList.clear()
                            DoneGoalsActions.RefreshList.run()
                            DoneGoalsActions.ShowMessage(R.string.goals_list_a_goal_has_been_deleted)
                                .run()
                        }, failure = {
                            DoneGoalsActions.Error(R.string.goals_list_error_delete).run()
                            DoneGoalsViewState(isErrorVisible = true).newState()
                        })

                }, failure = {
                    DoneGoalsActions.Error(R.string.goals_list_error_delete).run()
                    DoneGoalsViewState(isErrorVisible = true).newState()
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
}