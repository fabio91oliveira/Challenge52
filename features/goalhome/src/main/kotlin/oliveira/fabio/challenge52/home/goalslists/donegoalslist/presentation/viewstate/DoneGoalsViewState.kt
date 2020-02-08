package oliveira.fabio.challenge52.home.goalslists.donegoalslist.presentation.viewstate

import androidx.annotation.PluralsRes

data class DoneGoalsViewState
    (
    val isLoading: Boolean = false,
    val isDeleteButtonVisible: Boolean = false,
    val isDoneGoalsListVisible: Boolean = false,
    val isEmptyStateVisible: Boolean = false,
    val isErrorVisible: Boolean = false,
    val dialog: DoneGoalsDialog = DoneGoalsDialog.NoDialog
)

sealed class DoneGoalsDialog {
    object NoDialog : DoneGoalsDialog()
    data class RemoveConfirmationDialog(
        @PluralsRes val pluralRes: Int,
        val doneGoalsRemovedSize: Int
    ) : DoneGoalsDialog()
}