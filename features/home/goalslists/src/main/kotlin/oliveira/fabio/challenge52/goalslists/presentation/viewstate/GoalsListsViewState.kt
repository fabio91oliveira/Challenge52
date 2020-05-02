package oliveira.fabio.challenge52.goalslists.presentation.viewstate

internal data class GoalsListsViewState
    (
    val userName: String? = null,
    val totalTasks: Int = 0
) {
    companion object {
        fun init() =
            GoalsListsViewState()
    }
}