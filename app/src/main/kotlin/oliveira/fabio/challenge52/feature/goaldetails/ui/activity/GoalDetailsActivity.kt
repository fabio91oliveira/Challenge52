package oliveira.fabio.challenge52.feature.goaldetails.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
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

    private var firstTime = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_details)

        savedInstanceState?.let {
            setupToolbar()
            showLoading()
            initRecyclerView()
            initLiveData()
        } ?: run {
            init()
        }
    }

    override fun onBackPressed() {
        closeDetails()
    }

    override fun onClickWeek(week: Week) {
        goalDetailsViewModel.updateWeek(week)
        goalDetailsViewModel.getParsedDetailsList(goalWithWeeks, week)
    }

    private fun init() {
        setupToolbar()
        showLoading()
        initRecyclerView()
        initLiveData()
        goalDetailsViewModel.getParsedDetailsList(goalWithWeeks)
    }

    private fun initLiveData() {
        goalDetailsViewModel.mutableLiveDataItemList.observe(this, Observer {
            hideLoading()
            it?.let { list ->
                weeksAdapter.clearList()
                weeksAdapter.addList(list)
                showContent()
                if (firstTime) {
                    expandBar(true)
                    firstTime = false
                }
            } ?: run {
                expandBar(false)
            }
        })
        goalDetailsViewModel.mutableLiveDataUpdated.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let {
                if (it) {
                    newIntent.putExtra(HAS_CHANGED, true)
                }
            }
        })
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = goalWithWeeks.goal.name
        toolbar.setNavigationOnClickListener { closeDetails() }
    }

    private fun initRecyclerView() {
        rvWeeks.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvWeeks.adapter = weeksAdapter
    }

    private fun closeDetails() {
        setResult(Activity.RESULT_OK, newIntent)
        finish()
    }

    private fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loading.visibility = View.GONE
    }

    private fun showContent() {
        rvWeeks.visibility = View.VISIBLE
    }

    private fun hideContent() {
        rvWeeks.visibility = View.GONE
    }

    private fun showError() {

    }

    private fun hideError() {

    }

    private fun expandBar(hasToExpand: Boolean) = appBarLayout.setExpanded(hasToExpand)

    companion object {
        private const val GOAL_TAG = "GOAL"
        private const val HAS_CHANGED = "HAS_CHANGED"
    }
}