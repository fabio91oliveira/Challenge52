package oliveira.fabio.challenge52.feature.help.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import oliveira.fabio.challenge52.feature.help.vo.Question
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

    fun getQuestions() {
        launch {
            SuspendableResult.of<MutableList<Question>, Exception> { createQuestions() }.fold(
                success = {
                    mutableLiveDataQuestions.postValue(it)
                },
                failure = {
                    mutableLiveDataQuestions.postValue(mutableListOf())
                }
            )
        }
    }

    private fun createQuestions() = mutableListOf<Question>().apply {
        add(
            Question(
                "Question question question?",
                "Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer "
            )
        )
        add(
            Question(
                "Question question question?",
                "Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer "
            )
        )
        add(
            Question(
                "Question question question?",
                "Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer "
            )
        )
        add(
            Question(
                "Question question question?",
                "Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer "
            )
        )
        add(
            Question(
                "Question question question?",
                "Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer "
            )
        )
        add(
            Question(
                "Question question question?",
                "Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer "
            )
        )
        add(
            Question(
                "Question question question?",
                "Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer "
            )
        )
        add(
            Question(
                "Question question question?",
                "Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer "
            )
        )
        add(
            Question(
                "Question question question?",
                "Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer "
            )
        )
        add(
            Question(
                "Question question question?",
                "Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer "
            )
        )
        add(
            Question(
                "Question question question?",
                "Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer "
            )
        )
        add(
            Question(
                "Question question question?",
                "Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer "
            )
        )
        add(
            Question(
                "Question question question?",
                "Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer "
            )
        )
        add(
            Question(
                "Question question question?",
                "Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer "
            )
        )
        add(
            Question(
                "Question question question?",
                "Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer "
            )
        )
        add(
            Question(
                "Question question question?",
                "Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer "
            )
        )
        add(
            Question(
                "Question question question?",
                "Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer "
            )
        )
        add(
            Question(
                "Question question question?",
                "Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer "
            )
        )
        add(
            Question(
                "Question question question?",
                "Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer "
            )
        )
        add(
            Question(
                "Question question question?",
                "Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer "
            )
        )
        add(
            Question(
                "Question question question?",
                "Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer "
            )
        )
        add(
            Question(
                "Question question question?",
                "Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer Answer "
            )
        )
    }
}