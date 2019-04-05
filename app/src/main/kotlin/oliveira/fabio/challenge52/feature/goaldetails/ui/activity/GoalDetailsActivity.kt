package oliveira.fabio.challenge52.feature.goaldetails.ui.activity

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_goal_details.*
import oliveira.fabio.challenge52.R
import oliveira.fabio.challenge52.feature.goaldetails.ui.adapter.WeeksAdapter
import oliveira.fabio.challenge52.feature.goaldetails.viewmodel.GoalDetailsViewModel
import oliveira.fabio.challenge52.model.entity.Week
import oliveira.fabio.challenge52.model.vo.GoalWithWeeks
import org.koin.android.viewmodel.ext.android.viewModel

class GoalDetailsActivity : AppCompatActivity(), WeeksAdapter.OnClickWeekListener {

    private val newIntent by lazy { Intent().apply { putExtra(HAS_CHANGED, false) } }
    private val goalDetailsViewModel: GoalDetailsViewModel by viewModel()
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

    override fun onBackPressed() {
        closeDetails()
    }

    override fun onClickWeek(week: Week) {
        goalDetailsViewModel.updateWeek(week)
    }

    private fun init() {
        setupToolbar()
        initRecyclerView()
        initLiveData()
        setupDetails()
    }

    private fun initLiveData() {
        goalDetailsViewModel.mutableLiveDataUpdated.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let {
                if (it) {
                    newIntent.putExtra(HAS_CHANGED, true)
                }
            }
        })
    }

    private fun setupDetails() {
        val progress = goalWithWeeks.getPercentOfConclusion()

        val animation = ObjectAnimator.ofInt(progressBar, "progress", 0, progress)
        animation.duration = 1000
        animation.interpolator = DecelerateInterpolator()
        animation.addUpdateListener {
            val prog = it.animatedValue as Int
            txtPercent.text =
                prog.toString() + resources.getString(R.string.goal_list_progress_value_percent)
        }
        animation.start()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { closeDetails() }
    }

    private fun initRecyclerView() {
        rvWeeks.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvWeeks.adapter = weeksAdapter
        weeksAdapter.addList(goalWithWeeks.weeks)
    }

    private fun closeDetails() {
        setResult(Activity.RESULT_OK, newIntent)
        finish()
    }

    companion object {
        private const val GOAL_TAG = "GOAL"
        private const val HAS_CHANGED = "HAS_CHANGED"
    }
}