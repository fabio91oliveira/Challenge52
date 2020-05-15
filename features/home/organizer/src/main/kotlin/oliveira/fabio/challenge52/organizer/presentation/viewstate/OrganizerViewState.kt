package oliveira.fabio.challenge52.organizer.presentation.viewstate

internal data class OrganizerViewState
    (
    val isLoadingBalance: Boolean = false,
    val isLoadingTransactions: Boolean = false,
    val isLoadingRemove: Boolean = false,
    val isHideLoading: Boolean = false,
    val isHide: Boolean = false,
    val isTransactionsVisible: Boolean = false,
    val isEmptyStateVisible: Boolean = false,
    val isLoadingFilters: Boolean = false,
    val isFiltersVisible: Boolean = false,
    val isEmptyStateFilterTransactionVisible: Boolean = false,
    val isAddButtonVisible: Boolean = true,
    val currentMonthYear: String
) {
    companion object {
        fun init(currentMonthYear: String) = OrganizerViewState(currentMonthYear = currentMonthYear)
    }
}