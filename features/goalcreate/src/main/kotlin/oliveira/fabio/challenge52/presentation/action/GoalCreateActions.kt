package oliveira.fabio.challenge52.presentation.action

sealed class GoalCreateActions {
    object ShowSuccess : GoalCreateActions()
    object ShowError : GoalCreateActions()
}