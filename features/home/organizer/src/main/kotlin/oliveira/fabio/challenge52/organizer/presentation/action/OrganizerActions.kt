package oliveira.fabio.challenge52.organizer.presentation.action

import androidx.annotation.StringRes

internal sealed class OrganizerActions {
    object ResetFilters : OrganizerActions()
    object UpdateTransactions : OrganizerActions()
    data class UpdateTransactionsAfterRemove(val position: Int) : OrganizerActions()
    data class ShowConfirmationMessage(@StringRes val strRes: Int) : OrganizerActions()
    object OpenCreateTransactionScreen : OrganizerActions()

    data class ShowCriticalError(
        @StringRes val titleRes: Int,
        @StringRes val descriptionRes: Int
    ) : OrganizerActions()
}