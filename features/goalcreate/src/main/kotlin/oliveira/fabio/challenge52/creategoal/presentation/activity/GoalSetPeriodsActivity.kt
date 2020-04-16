package oliveira.fabio.challenge52.creategoal.presentation.activity

import android.content.Intent
import android.os.Bundle
import features.goalcreate.R
import kotlinx.android.synthetic.main.activity_goal_set_periods.*
import oliveira.fabio.challenge52.BaseActivity
import oliveira.fabio.challenge52.extensions.setRipple

internal class GoalSetPeriodsActivity : BaseActivity(R.layout.activity_goal_set_periods) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        setupToolbar()
        setupSelectButtonRipple()
        setupClickListener()
    }

    private fun setupToolbar() {
        with(toolbar) {
            setSupportActionBar(this)
            setNavigationOnClickListener { finish() }
        }
    }

    private fun setupSelectButtonRipple() {
        btnContinue.setRipple(android.R.color.white)
    }

    private fun setupClickListener() {
        btnContinue.setOnClickListener {
            Intent(this, CreateGoalFinalStepActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

}