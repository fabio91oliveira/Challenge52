package oliveira.fabio.challenge52.feature.help.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_help.*
import oliveira.fabio.challenge52.R
import oliveira.fabio.challenge52.feature.help.ui.adapter.QuestionsAdapter
import oliveira.fabio.challenge52.feature.help.vo.Question

class HelpFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_help, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setupToolbar()
        initRecyclerView()
    }

    private fun setupToolbar() {
//        toolbar
//
//
//        collapsingToolbar.apply {
//            val tf = ResourcesCompat.getFont(requireContext(), R.font.ubuntu_bold)
//            setCollapsedTitleTypeface(tf)
//            setExpandedTitleTypeface(tf)
//        }
    }

    private fun initRecyclerView() {
        val adapter = QuestionsAdapter()
        adapter.addList(getQuestions())
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter
    }

    private fun getQuestions() = mutableListOf<Question>().apply {
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

    companion object {
        fun newInstance() = HelpFragment()
    }
}