package oliveira.fabio.challenge52.goalcreate.presentation.viewstate

internal data class GoalCreateViewState(
    val isCreateButtonEnable: Boolean = false
) {
    companion object {
        fun init() =
            GoalCreateViewState()
    }
}