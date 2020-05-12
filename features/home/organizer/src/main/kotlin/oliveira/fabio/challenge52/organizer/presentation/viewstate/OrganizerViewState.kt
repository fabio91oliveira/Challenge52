package oliveira.fabio.challenge52.organizer.presentation.viewstate

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

internal data class OrganizerViewState
    (
    val isLoading: Boolean = false,
    val isLoadingRemove: Boolean = false,
    val isHideLoading: Boolean = false,
    val isHide: Boolean = false,
    val isTransactionsVisible: Boolean = false,
    val isEmptyStateVisible: Boolean = false,
    val isEmptyStateFilterTransactionVisible: Boolean = false,
    val isAddButtonVisible: Boolean = true,
    val isChipsEnabled: Boolean = false,
    val currentMonthYear: String,
    val dialog: Dialog = Dialog.NoDialog
) {
    companion object {
        fun init(currentMonthYear: String) = OrganizerViewState(currentMonthYear = currentMonthYear)
    }
}

sealed class Dialog {
    object NoDialog : Dialog()
    data class ConfirmationDialogRemoveTransaction(
        @DrawableRes val imageRes: Int,
        @StringRes val titleRes: Int,
        @StringRes val descriptionRes: Int,
        val positionTransaction: Int
    ) :
        Dialog()
}