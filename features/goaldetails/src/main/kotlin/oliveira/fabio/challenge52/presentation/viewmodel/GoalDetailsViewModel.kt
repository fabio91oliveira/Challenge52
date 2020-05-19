package oliveira.fabio.challenge52.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.coroutines.SuspendableResult
import com.github.kittinunf.result.map
import features.goaldetails.R
import kotlinx.coroutines.launch
import oliveira.fabio.challenge52.domain.usecase.ChangeItemStatusUseCase
import oliveira.fabio.challenge52.domain.usecase.MountGoalsDetailsUseCase
import oliveira.fabio.challenge52.domain.usecase.RemoveGoalUseCase
import oliveira.fabio.challenge52.domain.usecase.SetGoalAsDoneUseCase
import oliveira.fabio.challenge52.domain.usecase.VerifyAllWeekAreCompletedUseCase
import oliveira.fabio.challenge52.event.SingleLiveEvent
import oliveira.fabio.challenge52.presentation.action.GoalDetailsActions
import oliveira.fabio.challenge52.presentation.adapter.AdapterItem
import oliveira.fabio.challenge52.presentation.viewstate.Dialog
import oliveira.fabio.challenge52.presentation.viewstate.GoalDetailsViewState
import oliveira.fabio.challenge52.presentation.vo.Goal
import oliveira.fabio.challenge52.presentation.vo.ItemDetail
import oliveira.fabio.challenge52.presentation.vo.TopDetails
import timber.log.Timber

internal class GoalDetailsViewModel(
    private val state: SavedStateHandle,
    private val mountGoalsDetailsUseCase: MountGoalsDetailsUseCase,
    private val changeItemStatusUseCase: ChangeItemStatusUseCase,
    private val setGoalAsDoneUseCase: SetGoalAsDoneUseCase,
    private val removeGoalUseCase: RemoveGoalUseCase,
    private val verifyAllWeekAreCompletedUseCase: VerifyAllWeekAreCompletedUseCase
) : ViewModel() {
    private val _goalDetailsAction by lazy { SingleLiveEvent<GoalDetailsActions>() }
    private val _goalDetailsViewState by lazy { MutableLiveData<GoalDetailsViewState>() }

    val goalDetailsActions: LiveData<GoalDetailsActions>
        get() = _goalDetailsAction

    val goalDetailsViewState: LiveData<GoalDetailsViewState>
        get() = _goalDetailsViewState

    private val goal by lazy { state.get<Goal>(GOAL_TAG) ?: initializerError() as Goal }
    private val isFromDoneGoals by lazy { state.get<Boolean>(IS_FROM_DONE_GOALS) ?: false }

    init {
        initState()
        mountDetails(isFirstTime = true)
    }

    fun changeWeekStatus(
        itemDetail: ItemDetail,
        position: Int
    ) {
        setViewState {
            it.copy(
                isItemsBeingUpdated = true,
                dialog = Dialog.NoDialog
            )
        }
        viewModelScope.launch {
            Result.of(changeItemStatusUseCase(itemDetail, goal))
                .map {
                    mountGoalsDetailsUseCase(goal)
                }
                .fold(
                    success = { pair ->
                        setViewState {
                            it.copy(
                                isItemsBeingUpdated = false,
                                topDetails = pair.first,
                                adapterList = pair.second
                            )
                        }
                        GoalDetailsActions.UpdateDetailsWithPosition(position).sendAction()
                        GoalDetailsActions.ShowConfirmationMessage(
                            R.string.goal_details_item_updated
                        ).sendAction()
                    },
                    failure = {
                        setViewState { state ->
                            state.copy(
                                isItemsBeingUpdated = false,
                                dialog = Dialog.RegularErrorDialog(
                                    R.drawable.ic_confirm,
                                    R.string.goal_details_error_title,
                                    R.string.goal_details_update_error_message
                                )
                            )
                        }
                        Timber.e(it)
                    }
                )
        }
    }

    fun removeGoal() {
        viewModelScope.launch {
            SuspendableResult.of<Unit, Exception> { removeGoalUseCase(goal.id) }
                .fold(success = {
                    GoalDetailsActions.RemoveGoal.sendAction()
                }, failure = {
                    setViewState { state ->
                        state.copy(
                            dialog = Dialog.RegularErrorDialog(
                                R.drawable.ic_confirm,
                                R.string.goal_details_error_title,
                                R.string.goal_details_update_error_message
                            )
                        )
                    }
                    Timber.e(it)
                })
        }
    }

    fun completeGoal() {
        viewModelScope.launch {
            SuspendableResult.of<Unit, Exception> { setGoalAsDoneUseCase(goal.id) }
                .fold(success = {
                    GoalDetailsActions.CompleteGoal.sendAction()
                }, failure = {
                    setViewState { state ->
                        state.copy(
                            dialog = Dialog.RegularErrorDialog(
                                R.drawable.ic_confirm,
                                R.string.goal_details_error_title,
                                R.string.goal_details_update_error_message
                            )
                        )
                    }
                    Timber.e(it)
                })
        }
    }

    fun showConfirmationDialogDoneGoalWhenUpdated() {
        viewModelScope.launch {
            if (isFromDoneGoals.not()) {
                Result.of(
                    verifyAllWeekAreCompletedUseCase(
                        goal.items
                    )
                ).fold(success = { allWeeksAreCompleted ->
                    if (allWeeksAreCompleted)
                        setViewState {
                            it.copy(
                                dialog = Dialog.ConfirmationDialogDoneGoal(
                                    R.drawable.ic_confirm,
                                    R.string.goal_details_warning_title,
                                    R.string.goal_details_move_to_done_first_dialog
                                )
                            )
                        }
                }, failure = {
                    setViewState { state ->
                        state.copy(
                            dialog = Dialog.RegularErrorDialog(
                                R.drawable.ic_confirm,
                                R.string.goal_details_error_title,
                                R.string.goal_details_update_error_message
                            )
                        )
                    }
                    Timber.e(it)
                })
            }
        }
    }

    fun showConfirmationDialogDoneGoal() {
        viewModelScope.launch {
            SuspendableResult.of<Boolean, Exception> {
                verifyAllWeekAreCompletedUseCase(
                    goal.items
                )
            }.fold(success = { allWeeksAreChecked ->
                if (allWeeksAreChecked) {
                    setViewState {
                        it.copy(
                            dialog = Dialog.ConfirmationDialogDoneGoal(
                                R.drawable.ic_confirm,
                                R.string.goal_details_warning_title,
                                R.string.goal_details_are_you_sure_done
                            )
                        )
                    }
                } else {
                    setViewState {
                        it.copy(
                            dialog = Dialog.DefaultDialogMoveToDone(
                                R.drawable.ic_confirm,
                                R.string.goal_details_warning_title,
                                R.string.goal_details_cannot_move_to_done
                            )
                        )
                    }
                }
            }, failure = {
                setViewState { state ->
                    state.copy(
                        dialog = Dialog.RegularErrorDialog(
                            R.drawable.ic_confirm,
                            R.string.goal_details_error_title,
                            R.string.goal_details_update_error_message
                        )
                    )
                }
                Timber.e(it)
            })
        }
    }

    fun showConfirmationDialogRemoveGoal() =
        setViewState {
            it.copy(
                dialog = Dialog.ConfirmationDialogRemoveGoal(
                    R.drawable.ic_confirm,
                    R.string.goal_details_warning_title,
                    R.string.goal_details_are_you_sure_remove
                )
            )
        }

    fun hideDialogs() =
        setViewState {
            it.copy(dialog = Dialog.NoDialog)
        }

    fun mountDetails(isFirstTime: Boolean = false) {
        setViewState {
            GoalDetailsViewState(isLoading = true)
        }
        viewModelScope.launch {
            SuspendableResult.of<Pair<TopDetails, MutableList<AdapterItem<String, ItemDetail>>>, Exception> {
                mountGoalsDetailsUseCase(goal)
            }.fold(
                success = { pair ->
                    setViewState {
                        GoalDetailsViewState(
                            isLoading = false,
                            isContentVisible = true,
                            topDetails = pair.first,
                            adapterList = pair.second
                        )
                    }
                    GoalDetailsActions.UpdateDetails.sendAction()
                    if (isFirstTime) showConfirmationDialogDoneGoalWhenUpdated()
                },
                failure = {
                    GoalDetailsActions.CriticalError(
                        R.string.goal_details_error_title,
                        R.string.goal_details_list_error_message
                    )
                        .sendAction()
                    setViewState {
                        GoalDetailsViewState(isLoading = false)
                    }
                    Timber.e(it)
                }
            )
        }
    }

    private fun initializerError() {
        GoalDetailsActions.CriticalError(
            R.string.goal_details_error_title,
            R.string.goal_details_list_error_message
        )
            .sendAction()
        setViewState {
            GoalDetailsViewState(isLoading = false)
        }
        Timber.e(ExceptionInInitializerError())
    }

    private fun GoalDetailsActions.sendAction() {
        _goalDetailsAction.value = this
    }

    private fun initState() {
        _goalDetailsViewState.value = GoalDetailsViewState.init()
    }

    private fun setViewState(state: (GoalDetailsViewState) -> GoalDetailsViewState) {
        _goalDetailsViewState.value?.also {
            _goalDetailsViewState.value = state(it)
        }
    }

    companion object {
        private const val GOAL_TAG = "GOAL"
        private const val IS_FROM_DONE_GOALS = "IS_FROM_DONE_GOALS"
    }
}