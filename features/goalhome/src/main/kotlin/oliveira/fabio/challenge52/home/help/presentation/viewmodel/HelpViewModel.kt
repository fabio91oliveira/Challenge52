package oliveira.fabio.challenge52.home.help.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.kittinunf.result.coroutines.SuspendableResult
import features.goalhome.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import oliveira.fabio.challenge52.home.help.domain.model.vo.Question
import kotlin.coroutines.CoroutineContext

class HelpViewModel : ViewModel(), CoroutineScope {

    private val job by lazy { SupervisorJob() }
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    val mutableLiveDataQuestions by lazy { MutableLiveData<MutableList<Question>>() }

    public override fun onCleared() {
        super.onCleared()
        if (job.isActive) job.cancel()
    }

    fun getQuestions(context: Context) {
        launch {
            SuspendableResult.of<MutableList<Question>, Exception> { createQuestions(context) }.fold(
                success = {
                    mutableLiveDataQuestions.postValue(it)
                },
                failure = {
                    mutableLiveDataQuestions.postValue(mutableListOf())
                }
            )
        }
    }

    private fun createQuestions(context: Context) = mutableListOf<Question>().apply {
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