package oliveira.fabio.challenge52.home.help.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.result.coroutines.SuspendableResult
import features.goalhome.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.home.help.domain.model.vo.Question
import oliveira.fabio.challenge52.home.help.presentation.action.HelpActions
import oliveira.fabio.challenge52.home.help.presentation.viewstate.HelpViewState
import timber.log.Timber

internal class HelpViewModel : ViewModel() {

    private val _helpActions by lazy { MutableLiveData<HelpActions>() }
    val helpActions by lazy { _helpActions }
    private val _helpViewState by lazy { MutableLiveData<HelpViewState>() }
    val helpViewState by lazy { _helpViewState }

    // TODO ISSO SERA REMOVIDO DESTA MANEIRA, VIRÁ DO FIREBASE
    fun getQuestions(context: Context) {
        viewModelScope.launch {
            SuspendableResult.of<MutableList<Question>, Exception> { createQuestions(context) }
                .fold(
                    success = {
                        HelpActions.PopulateQuestions(it).run()
                        HelpViewState(
                            isQuestionsVisible = true,
                            isToolbarExpanded = true
                        ).newState()
                    },
                    failure = {
                        HelpViewState(isErrorVisible = true).newState()
                        Timber.e(it)
                    }
                )
        }
    }

    private suspend fun createQuestions(context: Context) = withContext(Dispatchers.Main) {
        mutableListOf<Question>().apply {
            add(
                Question(
                    context.getString(R.string.help_first_question),
                    context.getString(R.string.help_first_question_answer)
                )
            )
            add(
                Question(
                    context.getString(R.string.help_second_question),
                    context.getString(R.string.help_second_question_answer)
                )
            )
            add(
                Question(
                    context.getString(R.string.help_third_question),
                    context.getString(R.string.help_third_question_answer)
                )
            )
        }
    }

    private fun HelpActions.run() {
        _helpActions.value = this
    }

    private fun HelpViewState.newState() {
        _helpViewState.value = this
    }
}