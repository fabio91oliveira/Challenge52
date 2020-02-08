package oliveira.fabio.challenge52.presentation.activity

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
import oliveira.fabio.challenge52.extensions.isVisible
import oliveira.fabio.challenge52.model.vo.ActivityResultValueObject
import oliveira.fabio.challenge52.persistence.model.entity.Week
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks
import oliveira.fabio.challenge52.presentation.action.GoalDetailsActions
import oliveira.fabio.challenge52.presentation.adapter.WeeksAdapter
import oliveira.fabio.challenge52.presentation.dialog.AlertDialogFragment
import oliveira.fabio.challenge52.presentation.viewmodel.GoalDetailsViewModel
import oliveira.fabio.challenge52.presentation.viewstate.Dialog
import org.koin.android.viewmodel.ext.android.viewModel

class GoalDetailsActivity : BaseActivity(R.layout.activity_goal_details),
    WeeksAdapter.OnClickWeekListener {

    private val goalDetailsViewModel: GoalDetailsViewModel by viewModel()
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
            initObservables()
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

    override fun onClickWeek(week: Week, position: Int, block: () -> Unit) {
        // TODO JOGAR PARA ShowConfirmationDialogWeekIsPosterior E DEPOIS DE CONFIRMADO MESMO, FAZER A ANIMACAO EM OUTRA ACTION COMO
        // TODO AnimateWeekChanged passando position, setando last position, e fazendo a animacao
        when (goalDetailsViewModel.isDateAfterTodayWhenWeekIsNotDeposited(week)) {
            true -> {
                showConfirmDialog(
                    resources.getString(R.string.goal_details_date_after_today),
                    DialogInterface.OnClickListener { dialog, _ ->
                        updateWeek(week, position, block)
                        dialog.dismiss()
                    })
            }
            false -> updateWeek(week, position, block)
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
            newIntent = Intent().apply { putExtra(HAS_CHANGED, ActivityResultValueObject()) }
        }
    }

    private fun init() {
        setupToolbar()
        initRecyclerView()
        initObservables()
        goalDetailsViewModel.getWeeksList(goalWithWeeks)
    }

    private fun initObservables() {
        with(goalDetailsViewModel) {
            goalDetailsViewState.observe(this@GoalDetailsActivity, Observer {
                showLoading(it.isLoading)
                handleDialog(it.dialog)
            })
            goalDetailsActions.observe(this@GoalDetailsActivity, Observer {
                when (it) {
                    is GoalDetailsActions.ShowAddedGoalsFirstTime -> {
                        updateItemList(it.itemsList)

                        showContent(true)
                        rvWeeks.scheduleLayoutAnimation()
                        if (!isFromDoneGoals)
                            goalDetailsViewModel.showConfirmationDialogDoneGoalWhenUpdated(
                                goalWithWeeks
                            )
                        expandBar(true)
                    }
                    is GoalDetailsActions.ShowAddedGoals -> {
                        updateItemList(it.itemsList)
                        showContent(true)
                        expandBar(false)
                    }
                    is GoalDetailsActions.ShowUpdatedGoal -> {
                        goalDetailsViewModel.getWeeksList(goalWithWeeks, it.week)
                        newIntent.putExtra(
                            HAS_CHANGED,
                            ActivityResultValueObject().apply { setChangeUpdated() })
                        goalDetailsViewModel.showConfirmationDialogDoneGoalWhenUpdated(goalWithWeeks)
                    }
                    is GoalDetailsActions.ShowRemovedGoal -> {
                        newIntent.putExtra(
                            HAS_CHANGED,
                            ActivityResultValueObject().apply { setChangeRemoved() })
                        closeDetails()
                    }
                    is GoalDetailsActions.ShowCompletedGoal -> {
                        newIntent.putExtra(
                            HAS_CHANGED,
                            ActivityResultValueObject().apply { setChangeCompleted() })
                        closeDetails()
                    }
                    is GoalDetailsActions.ShowError -> {
                        showErrorScreen(it.errorMessageRes)
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
            weeksAdapter = WeeksAdapter(
                this@GoalDetailsActivity,
                isFromDoneGoals
            )
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
        position: Int,
        block: () -> Unit
    ) {
        goalDetailsViewModel.changeWeekDepositStatus(week)
        goalWithWeeks.lastPosition = position
        block()
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

    private fun showLoading(hasToShow: Boolean) {
        loading.isVisible = hasToShow
    }

    private fun showContent(hasToShow: Boolean) {
        rvWeeks.isVisible = hasToShow
    }

    private fun handleDialog(dialogViewState: Dialog) {
        when (dialogViewState) {
            is Dialog.DefaultDialogMoveToDone -> {
                showDefaultDialog(dialogViewState.stringRes)
            }
            is Dialog.ConfirmationDialogRemoveGoal -> {
                showConfirmDialog(
                    resources.getString(dialogViewState.stringRes),
                    DialogInterface.OnClickListener { dialog, _ ->
                        goalDetailsViewModel.removeGoal(goalWithWeeks)
                        goalDetailsViewModel.hideDialogs()
                        dialog.dismiss()
                    })
            }
            is Dialog.ConfirmationDialogDoneGoal -> {
                showConfirmDialog(
                    resources.getString(dialogViewState.stringRes),
                    DialogInterface.OnClickListener { dialog, _ ->
                        goalDetailsViewModel.completeGoal(goalWithWeeks)
                        dialog.dismiss()
                    })
            }
        }
    }

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
            setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
                goalDetailsViewModel.hideDialogs()
            }
        }.show()

    private fun showDefaultDialog(resString: Int) =
        AlertDialog.Builder(this).apply {
            setTitle(resources.getString(R.string.goal_warning_title))
            setMessage(resources.getString(resString))
            setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
                goalDetailsViewModel.hideDialogs()
            }
        }.show()

    private fun expandBar(hasToExpand: Boolean) = appBarLayout.setExpanded(hasToExpand)

    companion object {
        private const val GOAL_TAG = "GOAL"
        private const val HAS_CHANGED = "HAS_CHANGED"
        private const val IS_FROM_DONE_GOALS = "IS_FROM_DONE_GOALS"
        private const val FIRST_POSITION = 0
    }
}