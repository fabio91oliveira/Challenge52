package oliveira.fabio.challenge52.home.goalslists.presentation.viewstate

data class GoalsListsViewState
    (
    val userName: String? = null,
    val totalTasks: Int = 0
) {
    companion object {
        fun init() = GoalsListsViewState()
    }
}