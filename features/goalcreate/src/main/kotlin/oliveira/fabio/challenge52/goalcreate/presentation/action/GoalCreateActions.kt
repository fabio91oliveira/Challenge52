package oliveira.fabio.challenge52.goalcreate.presentation.action

internal sealed class GoalCreateActions {
    object Success : GoalCreateActions()
    object Error : GoalCreateActions()
}