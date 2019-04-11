package oliveira.fabio.challenge52.feature.home.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*
import oliveira.fabio.challenge52.R
import oliveira.fabio.challenge52.feature.donegoalslist.ui.fragment.DoneGoalsListFragment
import oliveira.fabio.challenge52.feature.goalslist.ui.fragment.GoalsListFragment

class HomeActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var goalsListFragment: Fragment
    private lateinit var doneGoalsListFragment: Fragment
    private lateinit var activeFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        navigation.setOnNavigationItemSelectedListener(this)

        savedInstanceState?.let {
            goalsListFragment =
                supportFragmentManager.findFragmentByTag(GoalsListFragment.javaClass.simpleName) as GoalsListFragment
            doneGoalsListFragment =
                supportFragmentManager.findFragmentByTag(DoneGoalsListFragment.javaClass.simpleName) as DoneGoalsListFragment
        } ?: run {
            initFragments()
            activeFragment = goalsListFragment
            changeFragment(goalsListFragment)
        }
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
                changeFragment(goalsListFragment)
                return true
            }
            R.id.navigation_done -> {
                changeFragment(doneGoalsListFragment)
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

    private fun changeFragment(fragment: Fragment) {
        val tag = fragment.javaClass.simpleName
        if (supportFragmentManager.findFragmentByTag(tag) == null) {
            if (activeFragment != fragment) {
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, fragment, tag).addToBackStack(tag).hide(activeFragment).commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, fragment, tag).commit()
            }
        } else {
            supportFragmentManager.beginTransaction().addToBackStack(null).hide(activeFragment)
                .show(fragment).commit()
        }
        activeFragment = fragment
    }
}