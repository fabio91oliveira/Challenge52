package oliveira.fabio.challenge52.feature.goaldetails.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_goal_details.*
import oliveira.fabio.challenge52.R
import oliveira.fabio.challenge52.feature.goaldetails.ui.adapter.WeeksAdapter
import oliveira.fabio.challenge52.model.entity.Week
import oliveira.fabio.challenge52.model.vo.GoalWithWeeks

class GoalDetailsActivity : AppCompatActivity(), WeeksAdapter.OnClickWeekListener {

    private val weeksAdapter by lazy { WeeksAdapter(this) }
    private val goalWithWeeks by lazy { intent?.extras?.getSerializable(GOAL_TAG) as GoalWithWeeks }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_details)

        savedInstanceState?.let {

        } ?: run {
            init()
        }
    }

    override fun onClickWeek(week: Week) {

    }

    private fun init() {
        setupToolbar()
        initRecyclerView()
        setValues()
    }

    private fun setValues() {

    }

    private fun setupToolbar() {
//        setSupportActionBar(toolbar)
//        supportActionBar?.title = goalWithWeeks.goal.name
    }

    private fun initRecyclerView() {
        rvWeeks.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvWeeks.adapter = weeksAdapter
        weeksAdapter.addList(goalWithWeeks.weeks)
    }

    companion object {
        private const val GOAL_TAG = "GOAL"
    }
}