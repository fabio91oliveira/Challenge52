package oliveira.fabio.challenge52.goalcreate.presentation.activity.old

import android.os.Bundle
import features.goalcreate.R
import kotlinx.android.synthetic.main.activity_goal_create.*
import oliveira.fabio.challenge52.BaseActivity

internal class GoalCreateActivity : BaseActivity(R.layout.activity_goal_create) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        setupToolbar()
    }

    private fun setupToolbar() {
        with(toolbar) {
            setSupportActionBar(this)
            setNavigationOnClickListener { finish() }
        }
    }
}