package oliveira.fabio.challenge52.presentation.viewstate

data class GoalCreateViewState(
    val isCreateButtonEnable: Boolean = false
) {
    companion object {
        fun init() = GoalCreateViewState()
    }
}