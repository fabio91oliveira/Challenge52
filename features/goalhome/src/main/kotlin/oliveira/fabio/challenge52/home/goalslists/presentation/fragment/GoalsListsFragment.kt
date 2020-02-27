package oliveira.fabio.challenge52.home.goalslists.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import features.goalhome.R
import kotlinx.android.synthetic.main.fragment_goals_lists.*
import oliveira.fabio.challenge52.home.goalslists.donegoalslist.presentation.fragment.DoneGoalsListFragment
import oliveira.fabio.challenge52.home.goalslists.openedgoalslist.presentation.fragment.OpenedGoalsListFragment
import oliveira.fabio.challenge52.home.goalslists.presentation.adapter.CustomFragmentPagerAdapter
import oliveira.fabio.challenge52.home.goalslists.presentation.viewmodel.GoalsListsViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.text.MessageFormat


class GoalsListsFragment : Fragment(R.layout.fragment_goals_lists) {

    private val goalsListsViewModel: GoalsListsViewModel by sharedViewModel()

    private val fragmentPagerAdapter by lazy {
        CustomFragmentPagerAdapter(
            this
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupViewPager()
        setupTabLayout()
        setupObservables()
    }

    private fun setupAdapter() {
        fragmentPagerAdapter.addFragment(OpenedGoalsListFragment.newInstance())
        fragmentPagerAdapter.addFragment(DoneGoalsListFragment.newInstance())
    }

    private fun setupViewPager() {
        with(viewPager) {
            adapter = fragmentPagerAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    if (position == FIRST_POSITION)
                        goalsListsViewModel.showAddButton()
                    else
                        goalsListsViewModel.hideAddButton()
                }
            })
        }
    }

    private fun setupTabLayout() {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                FIRST_POSITION -> resources.getString(R.string.goals_lists_opened_goals)
                else -> resources.getString(R.string.goals_lists_done_goals)
            }
        }.attach()
    }

    private fun setupObservables() {
        with(goalsListsViewModel) {
            goalsListsViewState.observe(this@GoalsListsFragment, Observer {
                setUserName(it.userName)
                setTotalTasks(it.totalTasks)
            })
        }
    }

    private fun setUserName(userName: String?) {
        userName?.also {
            txtTitle.text = resources.getString(R.string.goals_lists_hello_user, it)
        }
    }

    private fun setTotalTasks(totalTasks: Int) {
        val text = resources.getText(R.string.goals_lists_welcome).toString()
        txtSubtitle.text = MessageFormat.format(text, totalTasks)
    }

    companion object {
        private const val FIRST_POSITION = 0
        fun newInstance() =
            GoalsListsFragment()
    }
}