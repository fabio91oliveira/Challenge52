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


class HomeActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    GoalsListFragment.OnGoalsListChangeListener {

    private lateinit var goalsListFragment: GoalsListFragment
    private lateinit var doneGoalsListFragment: DoneGoalsListFragment
    private lateinit var activeFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        navigation.setOnNavigationItemSelectedListener(this)

        savedInstanceState?.let {
            initFragments()

            supportFragmentManager.findFragmentByTag(GoalsListFragment::class.java.simpleName)
                ?.let { goalsListFragment = it as GoalsListFragment }
            supportFragmentManager.findFragmentByTag(DoneGoalsListFragment::class.java.simpleName)
                ?.let { doneGoalsListFragment = it as DoneGoalsListFragment }

            val tag = savedInstanceState.getString(CURRENT_TAB)
            when (supportFragmentManager.findFragmentByTag(tag)) {
                is GoalsListFragment -> activeFragment =
                    supportFragmentManager.findFragmentByTag(tag) as GoalsListFragment
                is DoneGoalsListFragment -> activeFragment =
                    supportFragmentManager.findFragmentByTag(tag) as DoneGoalsListFragment
            }

        } ?: run {
            initFragments()
            activeFragment = goalsListFragment
            changeFragment(goalsListFragment)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CURRENT_TAB, activeFragment::class.java.simpleName)
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
                return false
            }
            R.id.navigation_options -> {
                return false
            }
        }
        return false
    }

    override fun onListChanged() {
        supportFragmentManager.findFragmentByTag(DoneGoalsListFragment::class.java.simpleName)
            ?.let { doneGoalsListFragment.listDoneGoals()}
    }

    private fun initFragments() {
        goalsListFragment = GoalsListFragment.newInstance()
        doneGoalsListFragment = DoneGoalsListFragment.newInstance()
    }

    private fun changeFragment(fragment: Fragment) {
        val tag = fragment::class.java.simpleName
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

    companion object {
        private const val CURRENT_TAB = "CURRENT_TAB"
    }
}