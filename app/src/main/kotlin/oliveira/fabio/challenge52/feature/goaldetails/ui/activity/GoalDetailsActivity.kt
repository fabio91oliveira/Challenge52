package oliveira.fabio.challenge52.feature.goaldetails.ui.activity

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_goal_details.*
import oliveira.fabio.challenge52.R
import oliveira.fabio.challenge52.feature.errorscreen.ui.dialog.ErrorDialogFragment
import oliveira.fabio.challenge52.feature.goaldetails.ui.adapter.WeeksAdapter
import oliveira.fabio.challenge52.feature.goaldetails.viewmodel.GoalDetailsViewModel
import oliveira.fabio.challenge52.feature.goalslist.ui.fragment.GoalsListFragment
import oliveira.fabio.challenge52.feature.goalslist.vo.ActivityResultVO
import oliveira.fabio.challenge52.persistence.model.entity.Week
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks
import org.koin.android.viewmodel.ext.android.viewModel


class GoalDetailsActivity : AppCompatActivity(), WeeksAdapter.OnClickWeekListener {

    private val newIntent by lazy { Intent().apply { putExtra(HAS_CHANGED, ActivityResultVO()) } }
    private val goalDetailsViewModel: GoalDetailsViewModel by viewModel()
    private val isDoneGoals by lazy { intent.extras?.getBoolean(IS_FROM_DONE_GOALS, false) ?: run { false } }
    private lateinit var goalWithWeeks: GoalWithWeeks
    private lateinit var weeksAdapter: WeeksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_details)

        initGoalWithWeeks(savedInstanceState)
        savedInstanceState?.let {
            setupToolbar()
            showLoading()
            initRecyclerView()
            initLiveData()
        } ?: run {
            init()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(GOAL_TAG, goalWithWeeks)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() = closeDetails()

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        when (isDoneGoals) {
            true -> menuInflater.inflate(R.menu.goal_details_menu_from_done, menu)
            else -> menuInflater.inflate(R.menu.goal_details_menu, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.details_edit -> {
                true
            }
            R.id.details_done -> {
                when (goalDetailsViewModel.isAllWeeksDeposited(goalWithWeeks)) {
                    true -> {
                        showConfirmDialog(
                            resources.getString(R.string.goal_details_are_you_sure_done),
                            DialogInterface.OnClickListener { dialog, _ ->
                                completeGoal()
                                dialog.dismiss()
                            })
                    }
                    false -> showDialog(resources.getString(R.string.goal_details_cannot_move_to_done))
                }
                true
            }
            R.id.details_remove -> {
                showConfirmDialog(
                    resources.getString(R.string.goal_details_are_you_sure_remove),
                    DialogInterface.OnClickListener { dialog, _ ->
                        deleteGoal()
                        dialog.dismiss()
                    })
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClickWeek(week: Week, position: Int) {
        goalDetailsViewModel.updateWeek(week)
        goalWithWeeks.lastPosition = position
        goalDetailsViewModel.getParsedDetailsList(goalWithWeeks, week)
    }

    private fun initGoalWithWeeks(savedInstanceState: Bundle? = null) {
        savedInstanceState?.let {
            goalWithWeeks = it.getSerializable(GOAL_TAG) as GoalWithWeeks
        } ?: run {
            goalWithWeeks = intent.extras?.getSerializable(GOAL_TAG) as GoalWithWeeks
        }
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
                goalWithWeeks.lastPosition?.let { position ->
                    weeksAdapter.addSingleItem(list[FIRST_POSITION], FIRST_POSITION)
                    weeksAdapter.addSingleItem(list[position], position)
                    goalWithWeeks.lastPosition = null
                } ?: run {
                    weeksAdapter.clearList()
                    weeksAdapter.addList(list)
                }

                showContent()
                if (goalDetailsViewModel.firstTime) {
                    rvWeeks.scheduleLayoutAnimation()
                    expandBar(true)
                    goalDetailsViewModel.firstTime = false
                    if (!isDoneGoals) shouldShowMoveToDoneDialog()
                }
            } ?: run {
                setResult(GoalsListFragment.ACTIVITY_ERROR)
                finish()
            }
        })
        goalDetailsViewModel.mutableLiveDataUpdated.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let {
                when (it) {
                    true -> {
                        newIntent.putExtra(HAS_CHANGED, ActivityResultVO().apply { setChangeUpdated() })
                        shouldShowMoveToDoneDialog()
                    }
                    else -> showErrorScreen(resources.getString(R.string.goal_details_update_error_message))
                }
            }
        })
        goalDetailsViewModel.mutableLiveDataRemoved.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let {
                when (it) {
                    true -> {
                        newIntent.putExtra(HAS_CHANGED, ActivityResultVO().apply { setChangeRemoved() })
                        closeDetails()
                    }
                    else -> showErrorScreen(resources.getString(R.string.goal_details_remove_error_message))
                }
            }
        })
        goalDetailsViewModel.mutableLiveDataCompleted.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let {
                when (it) {
                    true -> {
                        newIntent.putExtra(HAS_CHANGED, ActivityResultVO().apply { setChangeCompleted() })
                        closeDetails()
                    }
                    else -> showErrorScreen(resources.getString(R.string.goal_details_make_done_error_message))
                }
            }
        })
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = goalWithWeeks.goal.name
        toolbar.setNavigationOnClickListener { closeDetails() }
        collapsingToolbar.apply {
            val tf = ResourcesCompat.getFont(context, R.font.ubuntu_bold)
            setCollapsedTitleTypeface(tf)
            setExpandedTitleTypeface(tf)
        }
    }

    private fun initRecyclerView() {
        rvWeeks.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        weeksAdapter = WeeksAdapter(this, isDoneGoals)
        rvWeeks.adapter = weeksAdapter
        rvWeeks.itemAnimator = null
    }

    private fun closeDetails() {
        setResult(Activity.RESULT_OK, newIntent)
        finish()
    }

    private fun shouldShowMoveToDoneDialog() {
        if (goalDetailsViewModel.isAllWeeksDeposited(goalWithWeeks)) {
            showConfirmDialog(
                resources.getString(R.string.goal_details_move_to_done_first_dialog),
                DialogInterface.OnClickListener { _, _ ->
                    goalDetailsViewModel.completeGoal(goalWithWeeks)
                })
        }
    }

    private fun deleteGoal() = goalDetailsViewModel.removeGoal(goalWithWeeks)

    private fun completeGoal() = goalDetailsViewModel.completeGoal(goalWithWeeks)

    private fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loading.visibility = View.GONE
    }

    private fun showContent() {
        rvWeeks.visibility = View.VISIBLE
    }

    private fun showErrorScreen(message: String) = ErrorDialogFragment().apply {
        val b = Bundle()
        b.putString(ErrorDialogFragment.MESSAGE, message)
        arguments = b
        show(supportFragmentManager, ErrorDialogFragment.TAG)
    }

    private fun showConfirmDialog(message: String, listener: DialogInterface.OnClickListener) =
        AlertDialog.Builder(this).apply {
            setTitle(resources.getString(R.string.goal_warning_title))
            setMessage(message)
            setPositiveButton(android.R.string.ok, listener)
            setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.dismiss() }
        }.show()

    private fun showDialog(message: String) =
        AlertDialog.Builder(this).apply {
            setTitle(resources.getString(R.string.goal_warning_title))
            setMessage(message)
            setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
        }.show()

    private fun expandBar(hasToExpand: Boolean) = appBarLayout.setExpanded(hasToExpand)

    companion object {
        private const val GOAL_TAG = "GOAL"
        private const val HAS_CHANGED = "HAS_CHANGED"
        private const val IS_FROM_DONE_GOALS = "IS_FROM_DONE_GOALS"
        private const val FIRST_POSITION = 0
    }
}