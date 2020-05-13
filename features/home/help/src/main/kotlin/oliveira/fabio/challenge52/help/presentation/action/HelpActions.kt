package oliveira.fabio.challenge52.help.presentation.action

import androidx.annotation.StringRes
import oliveira.fabio.challenge52.help.domain.model.vo.Question

internal sealed class HelpActions {
    data class PopulateQuestions(val questionsList: List<Question>) : HelpActions()
    data class ShowError(@StringRes val stringRes: Int) : HelpActions()
}