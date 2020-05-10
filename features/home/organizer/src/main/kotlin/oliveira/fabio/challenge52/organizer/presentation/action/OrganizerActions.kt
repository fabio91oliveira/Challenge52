package oliveira.fabio.challenge52.organizer.presentation.action

import androidx.annotation.StringRes
import oliveira.fabio.challenge52.presentation.vo.Balance

internal sealed class OrganizerActions {
    data class ShowBalance(val balance: Balance) :
        OrganizerActions()

    data class CriticalError(
        @StringRes val titleRes: Int,
        @StringRes val descriptionRes: Int
    ) : OrganizerActions()
}