package oliveira.fabio.challenge52.home.help.presentation.action

import androidx.annotation.StringRes
import oliveira.fabio.challenge52.home.help.domain.model.vo.Question

internal sealed class HelpActions {
    data class PopulateQuestions(val questionsList: List<Question>) : HelpActions()
    data class ShowError(@StringRes val stringRes: Int) : HelpActions()
}