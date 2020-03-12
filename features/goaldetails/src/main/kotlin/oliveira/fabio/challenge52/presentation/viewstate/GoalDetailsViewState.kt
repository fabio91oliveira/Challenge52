package oliveira.fabio.challenge52.presentation.viewstate

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import oliveira.fabio.challenge52.domain.model.Week

internal data class GoalDetailsViewState(
    val isLoading: Boolean = false,
    val isWeekBeingUpdated: Boolean = false,
    val isContentVisible: Boolean = false,
    val dialog: Dialog = Dialog.NoDialog
) {
    companion object {
        fun init() = GoalDetailsViewState()
    }
}

sealed class Dialog {
    object NoDialog : Dialog()
    data class ConfirmationDialogDoneGoal(
        @DrawableRes val imageRes: Int,
        @StringRes val stringRes: Int
    ) :
        Dialog()

    data class ConfirmationDialogRemoveGoal(
        @DrawableRes val imageRes: Int,
        @StringRes val stringRes: Int
    ) :
        Dialog()

    data class ConfirmationDialogUpdateWeek(
        @DrawableRes val imageRes: Int,
        @StringRes val stringRes: Int, val week: Week
    ) :
        Dialog()

    data class DefaultDialogMoveToDone(
        @DrawableRes val imageRes: Int,
        @StringRes val stringRes: Int
    ) : Dialog()

    data class RegularErrorDialog(
        @DrawableRes val imageRes: Int,
        @StringRes val stringRes: Int
    ) : Dialog()
}