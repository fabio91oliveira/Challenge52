package oliveira.fabio.challenge52.organizer.presentation.viewstate

internal data class OrganizerViewState
    (
    val isLoading: Boolean = false,
    val isHideLoading: Boolean = false,
    val isHide: Boolean = false,
    val isTransactionsVisible: Boolean = false,
    val isEmptyStateVisible: Boolean = false,
    val isAddButtonVisible: Boolean = true,
    val currentMonthYear: String
) {
    companion object {
        fun init(currentMonthYear: String) = OrganizerViewState(currentMonthYear = currentMonthYear)
    }
}