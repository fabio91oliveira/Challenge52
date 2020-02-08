package oliveira.fabio.challenge52.presentation.viewstate

import androidx.annotation.StringRes

data class GoalDetailsViewState(
    val isLoading: Boolean = true,
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
    data class DefaultDialogMoveToDone(@StringRes val stringRes: Int) : Dialog()
}