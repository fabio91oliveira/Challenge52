package oliveira.fabio.challenge52.home.goalslists.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

internal class CustomFragmentPagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {
    private val fragmentsList: MutableList<Fragment> = mutableListOf()

    fun addFragment(fragment: Fragment) = fragmentsList.add(fragment)

    override fun getItemCount() = fragmentsList.size
    override fun createFragment(position: Int) = fragmentsList[position]
}