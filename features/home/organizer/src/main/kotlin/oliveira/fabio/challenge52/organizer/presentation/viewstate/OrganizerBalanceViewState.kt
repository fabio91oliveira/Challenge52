package oliveira.fabio.challenge52.organizer.presentation.viewstate

import oliveira.fabio.challenge52.organizer.presentation.vo.BalanceTop

internal data class OrganizerBalanceViewState
    (
    val balanceTop: BalanceTop = BalanceTop()
) {
    companion object {
        fun init() =
            OrganizerBalanceViewState()
    }
}