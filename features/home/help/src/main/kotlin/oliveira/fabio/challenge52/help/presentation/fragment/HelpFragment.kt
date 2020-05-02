package oliveira.fabio.challenge52.help.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import features.home.help.R
import kotlinx.android.synthetic.main.fragment_help.*
import oliveira.fabio.challenge52.extensions.isVisible
import oliveira.fabio.challenge52.help.presentation.action.HelpActions
import oliveira.fabio.challenge52.help.presentation.adapter.QuestionsAdapter
import oliveira.fabio.challenge52.help.presentation.viewmodel.HelpViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HelpFragment : Fragment(R.layout.fragment_help) {

    private val questionAdapter by lazy { QuestionsAdapter() }
    private val helpViewModel: HelpViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedInstanceState?.let {
            setupToolbar()
            setupRecyclerView()
            setupObservables()
        } ?: run {
            init()
        }
    }

    private fun init() {
        setupToolbar()
        setupRecyclerView()
        setupObservables()
        helpViewModel.getQuestions(requireContext())
    }

    private fun setupToolbar() {
        collapsingToolbar.apply {
            val tf = ResourcesCompat.getFont(context, R.font.manjari_regular)
            setCollapsedTitleTypeface(tf)
            setExpandedTitleTypeface(tf)
        }
    }

    private fun setupRecyclerView() {
        with(rvQuestions) {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = questionAdapter
            itemAnimator = null
        }
    }

    private fun setupObservables() {
        with(helpViewModel) {
            helpViewState.observe(viewLifecycleOwner, Observer {
                expandBar(it.isToolbarExpanded)
                showLoading(it.isLoading)
                showQuestions(it.isQuestionsVisible)
                showError(it.isErrorVisible)
            })
            helpActions.observe(viewLifecycleOwner, Observer {
                when (it) {
                    is HelpActions.PopulateQuestions -> {
                        questionAdapter.addList(it.questionsList)
                        rvQuestions.scheduleLayoutAnimation()
                    }
                    is HelpActions.ShowError -> {

                    }
                }
            })
        }
    }

    private fun expandBar(hasToExpand: Boolean) = appBarLayout.setExpanded(hasToExpand)

    private fun showQuestions(hasToShow: Boolean) {
        rvQuestions.isVisible = hasToShow
    }

    private fun showError(hasToShow: Boolean) {
    }

    private fun showLoading(hasToShow: Boolean) {
        loading.isVisible = hasToShow
    }

    companion object {
        fun newInstance() =
            HelpFragment()
    }
}