package oliveira.fabio.challenge52.presentation.action

sealed class GoalCreateActions {
    object Success : GoalCreateActions()
    object Error : GoalCreateActions()
}