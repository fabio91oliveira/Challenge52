package oliveira.fabio.challenge52.presentation.state

sealed class GoalDetailsStateLoading {
    object ShowLoading : GoalDetailsStateLoading()
    object HideLoading : GoalDetailsStateLoading()
}