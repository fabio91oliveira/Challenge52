package oliveira.fabio.challenge52.goal.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.findNavController
import features.newgoal.R
import kotlinx.android.synthetic.main.activity_goal_name.*
import oliveira.fabio.challenge52.BaseActivity

internal class GoalNameActivity : BaseActivity(R.layout.activity_goal_name) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        setupToolbar()
        findNavController(R.id.svContent).setGraph(
            R.navigation.goal_navigation,
            intent.extras
        )
    }

    private fun setupToolbar() {
        with(toolbar) {
            setSupportActionBar(this)
            setNavigationOnClickListener { finish() }
        }
    }

    override fun onBackPressed() {
        finish()
    }

    companion object {
        fun newIntent(context: Context) = Intent(
            context,
            GoalNameActivity::class.java
        )
    }
}