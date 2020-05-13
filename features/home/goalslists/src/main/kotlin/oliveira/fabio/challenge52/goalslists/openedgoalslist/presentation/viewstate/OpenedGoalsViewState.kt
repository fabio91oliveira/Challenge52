package oliveira.fabio.challenge52.goalslists.openedgoalslist.presentation.viewstate

internal data class OpenedGoalsViewState
    (
    val isLoading: Boolean = false,
    val isAddButtonVisible: Boolean = false,
    val isOpenedGoalsListVisible: Boolean = false,
    val isEmptyStateVisible: Boolean = false,
    val isErrorVisible: Boolean = false
) {
    companion object {
        fun init() =
            OpenedGoalsViewState()
    }
}