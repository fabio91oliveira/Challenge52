package oliveira.fabio.challenge52.home.goalslists.openedgoalslist.presentation.viewstate

import androidx.annotation.PluralsRes

data class OpenedGoalsViewState
    (
    val isLoading: Boolean = false,
    val isDeleteButtonVisible: Boolean = false,
    val isAddButtonVisible: Boolean = false,
    val isOpenedGoalsListVisible: Boolean = false,
    val isEmptyStateVisible: Boolean = false,
    val isErrorVisible: Boolean = false,
    val dialog: OpenedGoalsDialog = OpenedGoalsDialog.NoDialog
) {
    companion object {
        fun init() = OpenedGoalsViewState()
    }
}

sealed class OpenedGoalsDialog {
    object NoDialog : OpenedGoalsDialog()
    data class RemoveConfirmationDialog(
        @PluralsRes val pluralRes: Int,
        val doneGoalsRemovedSize: Int
    ) : OpenedGoalsDialog()
}