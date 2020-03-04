package oliveira.fabio.challenge52.home.help.presentation.viewstate

internal data class HelpViewState
    (
    val isLoading: Boolean = false,
    val isToolbarExpanded: Boolean = false,
    val isQuestionsVisible: Boolean = false,
    val isErrorVisible: Boolean = false
)