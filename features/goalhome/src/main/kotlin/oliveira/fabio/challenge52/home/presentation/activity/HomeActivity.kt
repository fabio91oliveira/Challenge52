package oliveira.fabio.challenge52.home.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import features.goalhome.R
import kotlinx.android.synthetic.main.activity_home.*
import oliveira.fabio.challenge52.BaseActivity
import oliveira.fabio.challenge52.home.goalslists.presentation.fragment.GoalsListsFragment
import oliveira.fabio.challenge52.home.help.presentation.fragment.HelpFragment

class HomeActivity : BaseActivity(R.layout.activity_home),
    BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var goalsListsFragment: GoalsListsFragment
    private lateinit var helpFragment: HelpFragment
    private lateinit var activeFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigation.setOnNavigationItemSelectedListener(this)
        savedInstanceState?.let {
            initFragments()

            supportFragmentManager.findFragmentByTag(GoalsListsFragment::class.java.simpleName)
                ?.let { goalsListsFragment = it as GoalsListsFragment }
            supportFragmentManager.findFragmentByTag(HelpFragment::class.java.simpleName)
                ?.let { helpFragment = it as HelpFragment }

            val tag = savedInstanceState.getString(CURRENT_TAB)
            when (supportFragmentManager.findFragmentByTag(tag)) {
                is GoalsListsFragment -> activeFragment =
                    supportFragmentManager.findFragmentByTag(tag) as GoalsListsFragment
                is HelpFragment -> activeFragment =
                    supportFragmentManager.findFragmentByTag(tag) as HelpFragment
            }

        } ?: run {
            initFragments()
            activeFragment = goalsListsFragment
            changeFragment(goalsListsFragment)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CURRENT_TAB, activeFragment::class.java.simpleName)
    }

    override fun onBackPressed() = finish()
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_goals -> {
                changeFragment(goalsListsFragment)
                return true
            }
            R.id.navigation_help -> {
                changeFragment(helpFragment)
                return true
            }
            R.id.navigation_options -> {
                return false
            }
        }
        return false
    }

    private fun initFragments() {
        goalsListsFragment = GoalsListsFragment.newInstance()
        helpFragment = HelpFragment.newInstance()
    }

    private fun changeFragment(fragment: Fragment) {
        val tag = fragment::class.java.simpleName
        if (supportFragmentManager.findFragmentByTag(tag) == null) {
            if (activeFragment != fragment) {
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, fragment, tag).addToBackStack(tag).hide(activeFragment)
                    .commit()
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

        fun newIntent(context: Context) = Intent(
            context,
            HomeActivity::class.java
        )
    }
}