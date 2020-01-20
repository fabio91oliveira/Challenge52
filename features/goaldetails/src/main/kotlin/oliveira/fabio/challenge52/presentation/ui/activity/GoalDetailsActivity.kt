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
import oliveira.fabio.challenge52.domain.model.vo.Item
import oliveira.fabio.challenge52.extensions.showView
import oliveira.fabio.challenge52.model.vo.ActivityResultVO
import oliveira.fabio.challenge52.persistence.model.entity.Week
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks
import oliveira.fabio.challenge52.presentation.dialog.AlertDialogFragment
import oliveira.fabio.challenge52.presentation.state.GoalDetailsAction
import oliveira.fabio.challenge52.presentation.ui.adapter.WeeksAdapter
import oliveira.fabio.challenge52.presentation.viewmodel.GoalDetailsViewModel
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.ext.android.viewModel

class GoalDetailsActivity : BaseActivity(R.layout.activity_goal_details),
    WeeksAdapter.OnClickWeekListener {

    private val goalDetailsViewModel: GoalDetailsViewModel by currentScope.viewModel(this)
    private val isFromDoneGoals by lazy {
        intent.extras?.getBoolean(IS_FROM_DONE_GOALS) ?: false
    }
    private lateinit var goalWithWeeks: GoalWithWeeks
    private lateinit var weeksAdapter: WeeksAdapter
    private lateinit var newIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        with(outState) {
            putSerializable(GOAL_TAG, goalWithWeeks)
            putSerializable(
                HAS_CHANGED, newIntent.getSerializableExtra(
                    HAS_CHANGED
                )
            )
            super.onSaveInstanceState(this)
        }
    }

    override fun onBackPressed() = closeDetails()

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        when (isFromDoneGoals) {
            true -> menuInflater.inflate(R.menu.goal_details_menu_from_done, menu)
            else -> menuInflater.inflate(R.menu.goal_details_menu, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.details_done -> {
                goalDetailsViewModel.showConfirmationDialogDoneGoal(goalWithWeeks)
                true
            }
            R.id.details_remove -> {
                goalDetailsViewModel.showConfirmationDialogRemoveGoal()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClickWeek(week: Week, position: Int) {
        // TODO JOGAR PARA ShowConfirmationDialogWeekIsPosterior E DEPOIS DE CONFIRMADO MESMO, FAZER A ANIMACAO EM OUTRA ACTION COMO
        // TODO AnimateWeekChanged passando position, setando last position, e fazendo a animacao
        when (goalDetailsViewModel.isDateAfterTodayWhenWeekIsNotDeposited(week)) {
            true -> {
                showConfirmDialog(
                    resources.getString(R.string.goal_details_date_after_today),
                    DialogInterface.OnClickListener { dialog, _ ->
                        updateWeek(week, position)
                        dialog.dismiss()
                    })
            }
            false -> updateWeek(week, position)
        }
    }

    private fun initSavedValues(savedInstanceState: Bundle? = null) {
        savedInstanceState?.also {
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
        goalDetailsViewModel.getWeeksList(goalWithWeeks)
    }

    private fun initLiveData() {
        with(goalDetailsViewModel) {
            goalDetailsViewState.observe(this@GoalDetailsActivity, Observer {
                showLoading(it.isLoading)
            })
            goalDetailsAction.observe(this@GoalDetailsActivity, Observer {
                when (it) {
                    is GoalDetailsAction.ShowError -> {
                        showErrorScreen(it.errorMessageRes)
                    }
                    is GoalDetailsAction.ShowAddedGoalsFirstTime -> {
                        updateItemList(it.itemsList)

                        showContent(true)
                        rvWeeks.scheduleLayoutAnimation()
                        if (!isFromDoneGoals) goalDetailsViewModel.showConfirmationDialogDoneGoal(
                            goalWithWeeks,
                            true
                        )
                        expandBar(true)
                    }
                    is GoalDetailsAction.ShowAddedGoals -> {
                        updateItemList(it.itemsList)
                        showContent(true)
                        expandBar(false)
                    }
                    is GoalDetailsAction.ShowUpdatedGoal -> {
                        goalDetailsViewModel.getWeeksList(goalWithWeeks, it.week)
                        newIntent.putExtra(
                            HAS_CHANGED,
                            ActivityResultVO().apply { setChangeUpdated() })
                        goalDetailsViewModel.showConfirmationDialogDoneGoal(goalWithWeeks, true)
                    }
                    is GoalDetailsAction.ShowRemovedGoal -> {
                        newIntent.putExtra(
                            HAS_CHANGED,
                            ActivityResultVO().apply { setChangeRemoved() })
                        closeDetails()
                    }
                    is GoalDetailsAction.ShowCompletedGoal -> {
                        newIntent.putExtra(
                            HAS_CHANGED,
                            ActivityResultVO().apply { setChangeCompleted() })
                        closeDetails()
                    }
                    is GoalDetailsAction.ShowConfirmationDialogDoneGoal -> {
                        if (it.hasToShow) {
                            showConfirmDialog(
                                resources.getString(it.messageRes),
                                DialogInterface.OnClickListener { dialog, _ ->
                                    goalDetailsViewModel.completeGoal(goalWithWeeks)
                                    dialog.dismiss()
                                })
                        } else {
                            showDialog(resources.getString(R.string.goal_details_cannot_move_to_done))
                        }
                    }
                    is GoalDetailsAction.ShowCantMoveToDoneDialog -> {
                        showDialog(resources.getString(R.string.goal_details_cannot_move_to_done))
                    }
                    is GoalDetailsAction.ShowConfirmationDialogRemoveGoal -> {
                        showConfirmDialog(
                            resources.getString(R.string.goal_details_are_you_sure_remove),
                            DialogInterface.OnClickListener { dialog, _ ->
                                goalDetailsViewModel.removeGoal(goalWithWeeks)
                                dialog.dismiss()
                            })
                    }
                }
            })
        }
    }

    private fun setupToolbar() {
        with(toolbar) {
            setSupportActionBar(this)
            supportActionBar?.title = goalWithWeeks.goal.name
            setNavigationOnClickListener { closeDetails() }
        }
        collapsingToolbar.apply {
            val tf = ResourcesCompat.getFont(context, R.font.ubuntu_bold)
            setCollapsedTitleTypeface(tf)
            setExpandedTitleTypeface(tf)
        }
    }

    private fun initRecyclerView() {
        with(rvWeeks) {
            layoutManager =
                LinearLayoutManager(this@GoalDetailsActivity, RecyclerView.VERTICAL, false)
            weeksAdapter = WeeksAdapter(this@GoalDetailsActivity, isFromDoneGoals)
            adapter = weeksAdapter
            itemAnimator = null
        }
    }

    private fun closeDetails() {
        setResult(Activity.RESULT_OK, newIntent)
        finish()
    }

    private fun updateWeek(
        week: Week,
        position: Int
    ) {
        goalDetailsViewModel.changeWeekDepositStatus(week)
        goalWithWeeks.lastPosition = position
    }

    private fun updateItemList(list: MutableList<Item>) {
        goalWithWeeks.lastPosition?.also { position ->
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
    }

    private fun showLoading(hasToShow: Boolean) = loading.showView(hasToShow)
    private fun showContent(hasToShow: Boolean) = rvWeeks.showView(hasToShow)

    private fun showErrorScreen(stringResource: Int) {
        AlertDialogFragment.newInstance(
            R.drawable.ic_error,
            R.string.goal_oops_title,
            stringResource
        ) {
            finish()
        }.show(supportFragmentManager, AlertDialogFragment.TAG)
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