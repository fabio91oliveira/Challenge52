package oliveira.fabio.challenge52.presentation.state

sealed class GoalCreateState {
    object Success : GoalCreateState()
    object Error : GoalCreateState()
}