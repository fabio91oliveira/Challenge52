package oliveira.fabio.challenge52.home.goalslists.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
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
        with(viewPager) {
            adapter = fragmentPagerAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    if (position == FIRST_POSITION)
                        fabAdd.show()
                    else
                        fabAdd.hide()
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

    companion object {
        private const val FIRST_POSITION = 0
        fun newInstance() =
            GoalsListsFragment()
    }
}