package oliveira.fabio.challenge52.home.goalslists.presentation.viewstate

internal data class GoalsListsViewState
    (
    val userName: String? = null,
    val totalTasks: Int = 0,
    val balance: String? = null
) {
    companion object {
        fun init() = GoalsListsViewState()
    }
}