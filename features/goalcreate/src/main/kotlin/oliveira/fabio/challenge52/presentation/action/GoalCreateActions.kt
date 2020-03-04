package oliveira.fabio.challenge52.presentation.action

internal sealed class GoalCreateActions {
    object Success : GoalCreateActions()
    object Error : GoalCreateActions()
}