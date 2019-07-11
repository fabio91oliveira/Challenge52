package oliveira.fabio.challenge52.presentation.ui.activity

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import features.goaldetails.R
import kotlinx.android.synthetic.main.activity_goal_details.*
import oliveira.fabio.challenge52.BaseActivity
import oliveira.fabio.challenge52.extensions.showView
import oliveira.fabio.challenge52.model.vo.ActivityResultVO
import oliveira.fabio.challenge52.persistence.model.entity.Week
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks
import oliveira.fabio.challenge52.presentation.state.GoalDetailsState
import oliveira.fabio.challenge52.presentation.state.GoalDetailsStateLoading
import oliveira.fabio.challenge52.presentation.ui.adapter.WeeksAdapter
import oliveira.fabio.challenge52.presentation.viewmodel.GoalDetailsViewModel
import oliveira.fabio.challenge52.presentation.ui.dialog.ErrorDialogFragment
import org.koin.android.viewmodel.ext.android.viewModel


class GoalDetailsActivity : BaseActivity(), WeeksAdapter.OnClickWeekListener {

    private val goalDetailsViewModel: GoalDetailsViewModel by viewModel()
    private val isDoneGoals by lazy { intent.extras?.getBoolean(IS_FROM_DONE_GOALS, false) ?: run { false } }
    private lateinit var goalWithWeeks: GoalWithWeeks
    private lateinit var weeksAdapter: WeeksAdapter
    private lateinit var newIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_details)

        initSavedValues(savedInstanceState)
        savedInstanceState?.let {
            setupToolbar()
            initRecyclerView()
            initLiveData()
        } ?: run {
            init()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(GOAL_TAG, goalWithWeeks)
        outState.putSerializable(
            HAS_CHANGED, newIntent.getSerializableExtra(
                HAS_CHANGED
            )
        )
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

    override fun onClickWeek(week: Week, position: Int, func: () -> Unit) {
        when (goalDetailsViewModel.isDateAfterTodayWhenWeekIsNotDeposited(week)) {
            true -> {
                showConfirmDialog(
                    resources.getString(R.string.goal_details_date_after_today),
                    DialogInterface.OnClickListener { dialog, _ ->
                        updateWeek(week, position, func)
                        dialog.dismiss()
                    })
            }
            false -> updateWeek(week, position, func)
        }

    }

    private fun initSavedValues(savedInstanceState: Bundle? = null) {
        savedInstanceState?.let {
            goalWithWeeks = it.getSerializable(GOAL_TAG) as GoalWithWeeks
            newIntent = Intent().apply {
                putExtra(
                    HAS_CHANGED, it.getSerializable(
                        HAS_CHANGED
                    )
                )
            }
        } ?: run {
            goalWithWeeks = intent.extras?.getSerializable(GOAL_TAG) as GoalWithWeeks
            newIntent = Intent().apply { putExtra(HAS_CHANGED, ActivityResultVO()) }
        }
    }

    private fun init() {
        setupToolbar()
        initRecyclerView()
        initLiveData()
        goalDetailsViewModel.getParsedDetailsList(goalWithWeeks)
    }

    private fun initLiveData() {
        with(goalDetailsViewModel) {
            goalDetailsStateLoading.observe(this@GoalDetailsActivity, Observer {
                when (it) {
                    is GoalDetailsStateLoading.ShowLoading -> showLoading(true)
                    is GoalDetailsStateLoading.HideLoading -> showLoading(false)
                }
            })
            goalDetailsState.observe(this@GoalDetailsActivity, Observer {
                when (it) {
                    is GoalDetailsState.ShowError -> showErrorScreen(resources.getString(R.string.goal_details_make_done_error_message))
                    is GoalDetailsState.ShowAddedGoals -> {
                        it.itemsList?.let { list ->
                            goalWithWeeks.lastPosition?.let { position ->
                                weeksAdapter.addSingleItem(
                                    list[FIRST_POSITION],
                                    FIRST_POSITION
                                )
                                weeksAdapter.addSingleItem(list[position], position)
                                goalWithWeeks.lastPosition = null
                            } ?: run {
                                weeksAdapter.clearList()
                                weeksAdapter.addList(list)
                            }

                            showContent()
                            if (goalDetailsViewModel.firstTime) {
                                rvWeeks.scheduleLayoutAnimation()
                                if (!isDoneGoals) shouldShowMoveToDoneDialog()
                                goalDetailsViewModel.firstTime = false
                                expandBar(true)
                            } else {
                                expandBar(false)
                            }
                        } ?: run {
                            setResult(ACTIVITY_ERROR)
                            finish()
                        }
                    }
                    is GoalDetailsState.ShowUpdatedGoal -> {
                        when (it.hasBeenUpdated) {
                            true -> {
                                newIntent.putExtra(HAS_CHANGED, ActivityResultVO().apply { setChangeUpdated() })
                                shouldShowMoveToDoneDialog()
                            }
                            else -> showErrorScreen(resources.getString(R.string.goal_details_update_error_message))
                        }
                    }
                    is GoalDetailsState.ShowRemovedGoal -> {
                        when (it.hasBeenRemoved) {
                            true -> {
                                newIntent.putExtra(HAS_CHANGED, ActivityResultVO().apply { setChangeRemoved() })
                                closeDetails()
                            }
                            else -> showErrorScreen(resources.getString(R.string.goal_details_remove_error_message))
                        }
                    }
                    is GoalDetailsState.ShowCompletedGoal -> {
                        when (it.hasBeenCompleted) {
                            true -> {
                                newIntent.putExtra(HAS_CHANGED, ActivityResultVO().apply { setChangeCompleted() })
                                closeDetails()
                            }
                            else -> showErrorScreen(resources.getString(R.string.goal_details_make_done_error_message))
                        }
                    }
                }
            })
        }
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
        with(rvWeeks) {
            layoutManager = LinearLayoutManager(this@GoalDetailsActivity, RecyclerView.VERTICAL, false)
            weeksAdapter = WeeksAdapter(this@GoalDetailsActivity, isDoneGoals)
            adapter = weeksAdapter
            itemAnimator = null
        }
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

    private fun updateWeek(
        week: Week,
        position: Int,
        func: () -> Unit
    ) {
        goalDetailsViewModel.updateWeek(week)
        goalWithWeeks.lastPosition = position
        goalDetailsViewModel.getParsedDetailsList(goalWithWeeks, week)
        func.invoke()
    }

    private fun deleteGoal() = goalDetailsViewModel.removeGoal(goalWithWeeks)
    private fun completeGoal() = goalDetailsViewModel.completeGoal(goalWithWeeks)
    private fun showLoading(hasToShow: Boolean) = loading.showView(hasToShow)
    private fun showContent() = rvWeeks.showView(true)

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
        private const val ACTIVITY_ERROR = -3
    }
}