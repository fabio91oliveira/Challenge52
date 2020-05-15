package oliveira.fabio.challenge52.organizer.presentation.viewstate

import oliveira.fabio.challenge52.organizer.presentation.vo.BalanceBottom

internal data class OrganizerTransactionsViewState
    (
    val balanceBottom: BalanceBottom = BalanceBottom()
) {
    companion object {
        fun init() =
            OrganizerTransactionsViewState()
    }
}