package oliveira.fabio.challenge52.home.goalslists.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import features.goalhome.R
import kotlinx.android.synthetic.main.fragment_goals_lists.*
import oliveira.fabio.challenge52.home.goalslists.donegoalslist.presentation.fragment.DoneGoalsListFragment
import oliveira.fabio.challenge52.home.goalslists.openedgoalslist.presentation.fragment.OpenedGoalsListFragment
import oliveira.fabio.challenge52.home.goalslists.presentation.adapter.CustomFragmentPagerAdapter

class GoalsListsFragment : Fragment(R.layout.fragment_goals_lists) {

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
    }

    private fun setupAdapter() {
        fragmentPagerAdapter.addFragment(OpenedGoalsListFragment.newInstance())
        fragmentPagerAdapter.addFragment(DoneGoalsListFragment.newInstance())
    }

    private fun setupViewPager() {
        viewPager.adapter = fragmentPagerAdapter
    }

    private fun setupTabLayout() {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = resources.getString(R.string.goals_lists_opened_goals)
                }
                1 -> {
                    tab.text = resources.getString(R.string.goals_lists_done_goals)
                }
            }
        }.attach()
    }

    companion object {
        fun newInstance() =
            GoalsListsFragment()
    }
}