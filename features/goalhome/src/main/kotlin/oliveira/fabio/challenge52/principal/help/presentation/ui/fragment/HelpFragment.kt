package oliveira.fabio.challenge52.principal.help.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import features.goalhome.R
import kotlinx.android.synthetic.main.fragment_help.*
import oliveira.fabio.challenge52.principal.help.presentation.ui.adapter.QuestionsAdapter
import oliveira.fabio.challenge52.principal.help.presentation.viewmodel.HelpViewModel
import oliveira.fabio.challenge52.principal.home.presentation.ui.activity.HomeActivity
import org.koin.android.ext.android.getKoin
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

class HelpFragment : Fragment() {

    private val questionAdapter by lazy { QuestionsAdapter() }
    private val helpViewModel: HelpViewModel by getKoin().getOrCreateScope(
        "myScope",
        named<HomeActivity>()
    ).viewModel(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_help, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedInstanceState?.let {
            setupToolbar()
            initRecyclerView()
            initLiveDatas()
            showLoading()
        } ?: run {
            init()
        }
    }

    private fun init() {
        setupToolbar()
        initRecyclerView()
        initLiveDatas()
        showLoading()
        helpViewModel.getQuestions(requireContext())
    }

    private fun setupToolbar() {
        collapsingToolbar.apply {
            val tf = ResourcesCompat.getFont(context, R.font.ubuntu_bold)
            setCollapsedTitleTypeface(tf)
            setExpandedTitleTypeface(tf)
        }
    }

    private fun initRecyclerView() {
        rvQuestions.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvQuestions.adapter = questionAdapter
        rvQuestions.itemAnimator = null
    }

    private fun initLiveDatas() {
        helpViewModel.mutableLiveDataQuestions.observe(this, Observer {
            hideLoading()
            when (it.isNotEmpty()) {
                true -> {
                    questionAdapter.addList(it)
                    showContent()
                    expandBar(true)
                    rvQuestions.scheduleLayoutAnimation()
                }
                false -> {//handle error}
                    hideContent()
                    expandBar(false)
                }
            }
        })
    }

    private fun expandBar(hasToExpand: Boolean) = appBarLayout.setExpanded(hasToExpand)

    private fun showContent() {
        rvQuestions.visibility = View.VISIBLE
    }

    private fun hideContent() {
        rvQuestions.visibility = View.GONE
    }

    private fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loading.visibility = View.GONE
    }

    companion object {
        fun newInstance() =
            HelpFragment()
    }
}