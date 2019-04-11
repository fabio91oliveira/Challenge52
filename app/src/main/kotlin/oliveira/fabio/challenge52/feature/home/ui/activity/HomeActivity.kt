package oliveira.fabio.challenge52.feature.home.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.view_navigation_count.view.*
import oliveira.fabio.challenge52.R
import oliveira.fabio.challenge52.feature.donegoalslist.ui.fragment.DoneGoalsListFragment
import oliveira.fabio.challenge52.feature.goalslist.ui.fragment.GoalsListFragment


class HomeActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var goalsListFragment: Fragment
    private lateinit var doneGoalsListFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        navigation.setOnNavigationItemSelectedListener(this)

        savedInstanceState?.let {
            goalsListFragment = supportFragmentManager.findFragmentByTag(KEY_GOALS_LIST) as GoalsListFragment
            doneGoalsListFragment =
                supportFragmentManager.findFragmentByTag(KEY_DONE_GOALS_LIST) as DoneGoalsListFragment
        } ?: run {
            initFragments()
            supportFragmentManager.beginTransaction().add(R.id.container, doneGoalsListFragment, KEY_DONE_GOALS_LIST)
                .hide(doneGoalsListFragment).commit()
            supportFragmentManager.beginTransaction().add(R.id.container, goalsListFragment, KEY_GOALS_LIST).commit()
        }
        test()
    }

    override fun onBackPressed() {
        supportFragmentManager.fragments.forEach { fragment ->
            if (fragment != null && fragment.isVisible) {
                with(fragment.childFragmentManager) {
                    if (backStackEntryCount > 0) {
                        popBackStack()
                        return
                    }
                }
            }
        }
        super.onBackPressed()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_goals -> {
                supportFragmentManager.beginTransaction().addToBackStack(null).hide(doneGoalsListFragment)
                    .show(goalsListFragment).commit()
                return true
            }
            R.id.navigation_done -> {
                supportFragmentManager.beginTransaction().addToBackStack(null).hide(goalsListFragment)
                    .show(doneGoalsListFragment).commit()
                return true
            }
            R.id.navigation_help -> {
                supportFragmentManager.beginTransaction().addToBackStack(null).show(goalsListFragment).commit()
                return true
            }
            R.id.navigation_options -> {
                supportFragmentManager.beginTransaction().addToBackStack(null).show(goalsListFragment).commit()
                return true
            }
        }
        return false
    }

    private fun initFragments() {
        goalsListFragment = GoalsListFragment.newInstance()
        doneGoalsListFragment = DoneGoalsListFragment.newInstance()
    }

    private fun test() {
//        val bottomNavigationMenuView = navigation.getChildAt(0) as BottomNavigationMenuView
//        val v = bottomNavigationMenuView.getChildAt(0)
//        val itemView = v as BottomNavigationItemView
//
//        val badge = LayoutInflater.from(this)
//            .inflate(R.layout.view_navigation_count, bottomNavigationMenuView, false)
//        val tv = badge.notification_badge
//        tv.text = "22"
//        itemView.addView(badge)
    }

    companion object {
        private const val KEY_GOALS_LIST = "GOALS_LIST"
        private const val KEY_DONE_GOALS_LIST = "DONE_GOALS_LIST"
    }

}