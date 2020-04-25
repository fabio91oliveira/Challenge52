package oliveira.fabio.challenge52.home.goalslists.donegoalslist.presentation.viewstate

internal data class DoneGoalsViewState
    (
    val isLoading: Boolean = false,
    val isDoneGoalsListVisible: Boolean = false,
    val isEmptyStateVisible: Boolean = false,
    val isErrorVisible: Boolean = false
) {
    companion object {
        fun init() = DoneGoalsViewState()
    }
}