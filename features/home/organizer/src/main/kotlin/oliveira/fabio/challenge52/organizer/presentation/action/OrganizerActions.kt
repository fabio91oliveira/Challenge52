package oliveira.fabio.challenge52.organizer.presentation.action

import androidx.annotation.StringRes
import oliveira.fabio.challenge52.presentation.vo.Balance
import oliveira.fabio.challenge52.presentation.vo.Transaction
import java.util.*

internal sealed class OrganizerActions {
    data class ShowBalance(val balance: Balance) :
        OrganizerActions()

    data class UpdateBalance(val balance: Balance) :
        OrganizerActions()

    data class UpdateTransactions(val transactions: LinkedList<Transaction>) :
        OrganizerActions()

    data class RemoveTransaction(val position: Int) : OrganizerActions()
    data class CancelRemoveTransaction(val position: Int) : OrganizerActions()
    data class ShowConfirmationMessage(@StringRes val strRes: Int) : OrganizerActions()

    object ResetTransactionsFilter : OrganizerActions()
    object DefaultTransactionFilterValues : OrganizerActions()

    data class CriticalError(
        @StringRes val titleRes: Int,
        @StringRes val descriptionRes: Int
    ) : OrganizerActions()
}