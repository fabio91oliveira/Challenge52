package oliveira.fabio.challenge52.presentation.viewstate

import androidx.annotation.StringRes
import oliveira.fabio.challenge52.persistence.model.entity.Week

data class GoalDetailsViewState(
    val isLoading: Boolean = false,
    val isContentVisible: Boolean = false,
    val isBarExpanded: Boolean = false,
    val dialog: Dialog = Dialog.NoDialog
) {
    companion object {
        fun init() = GoalDetailsViewState()
    }
}

sealed class Dialog {
    object NoDialog : Dialog()
    data class ConfirmationDialogDoneGoal(@StringRes val stringRes: Int) : Dialog()
    data class ConfirmationDialogRemoveGoal(@StringRes val stringRes: Int) : Dialog()
    data class ConfirmationDialogUpdateWeek(@StringRes val stringRes: Int, val week: Week) :
        Dialog()

    data class DefaultDialogMoveToDone(@StringRes val stringRes: Int) : Dialog()
}