package oliveira.fabio.challenge52.presentation.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.StringRes
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
import oliveira.fabio.challenge52.presentation.dialogfragment.FullScreenDialog
import oliveira.fabio.challenge52.presentation.dialogfragment.PopupDialog
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

    override fun onClickWeek(week: Week) {
        // TODO COMO EXECUTAR ANIMACAO?
        // TODO para que servia isso?
        //  goalWithWeeks.lastPosition = position
        with(goalDetailsViewModel) {
            if (isDateAfterTodayWhenWeekIsNotDeposited(week))
                showConfirmationDialogUpdateWeek(week)
            else
                changeWeekDepositStatus(week)
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

        // TODO BUSCAR DO BANCO ATRAVES DO ID E CARREGAR TUDO
        goalDetailsViewModel.getWeeksList(goalWithWeeks)
    }

    private fun initObservables() {
        with(goalDetailsViewModel) {
            goalDetailsViewState.observe(this@GoalDetailsActivity, Observer {
                showLoading(it.isLoading)
                handleDialog(it.dialog)
                showContent(it.isContentVisible)
                expandBar(it.isBarExpanded)
            })
            goalDetailsActions.observe(this@GoalDetailsActivity, Observer {
                when (it) {
                    is GoalDetailsActions.AddedGoalsFirstTime -> {
                        updateItemList(it.itemsList)
                        rvWeeks.scheduleLayoutAnimation()
                        if (!isFromDoneGoals)
                            goalDetailsViewModel.showConfirmationDialogDoneGoalWhenUpdated(
                                goalWithWeeks
                            )
                    }
                    is GoalDetailsActions.AddedGoals -> {
                        updateItemList(it.itemsList)
                        showContent(true)
                        expandBar(false)
                    }
                    is GoalDetailsActions.UpdatedGoal -> {
                        goalDetailsViewModel.getWeeksList(goalWithWeeks, it.week)
                        newIntent.putExtra(
                            HAS_CHANGED,
                            ActivityResultValueObject().apply { setChangeUpdated() })
                        goalDetailsViewModel.showConfirmationDialogDoneGoalWhenUpdated(goalWithWeeks)
                    }
                    is GoalDetailsActions.RemovedGoal -> {
                        newIntent.putExtra(
                            HAS_CHANGED,
                            ActivityResultValueObject().apply { setChangeRemoved() })
                        closeDetails()
                    }
                    is GoalDetailsActions.CompletedGoal -> {
                        newIntent.putExtra(
                            HAS_CHANGED,
                            ActivityResultValueObject().apply { setChangeCompleted() })
                        closeDetails()
                    }
                    is GoalDetailsActions.Error -> {
                        showFullScreenDialog(it.errorMessageRes)
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
                    dialogViewState.stringRes
                ) {
                    goalDetailsViewModel.removeGoal(goalWithWeeks)
                    goalDetailsViewModel.hideDialogs()
                }
            }
            is Dialog.ConfirmationDialogDoneGoal -> {
                showConfirmDialog(
                    dialogViewState.stringRes
                ) {
                    goalDetailsViewModel.completeGoal(goalWithWeeks)
                }
            }
            is Dialog.ConfirmationDialogUpdateWeek -> {
                showConfirmDialog(
                    dialogViewState.stringRes
                ) {
                    goalDetailsViewModel.changeWeekDepositStatus(dialogViewState.week)
                }
            }
        }
    }

    private fun showFullScreenDialog(stringResource: Int) {
        FullScreenDialog.Builder()
            .setTitle(R.string.goal_oops_title)
            .setSubtitle(stringResource)
            .setCloseAction(object : FullScreenDialog.FullScreenDialogCloseListener {
                override fun onClickCloseButton() {
                    finish()
                }
            })
            .setupConfirmButton(
                R.string.all_button_ok,
                object : FullScreenDialog.FullScreenDialogConfirmListener {
                    override fun onClickConfirmButton() {
                        finish()
                    }
                })
            .setupCancelButton(
                R.string.all_button_go_back,
                object : FullScreenDialog.FullScreenDialogCancelListener {
                    override fun onClickCancelButton() {
                        finish()
                    }
                })
            .build()
            .show(supportFragmentManager, FullScreenDialog.TAG)
    }

    private fun showConfirmDialog(
        @StringRes resString: Int,
        block: () -> Unit
    ) =
        PopupDialog.Builder()
            .setTitle(R.string.goal_warning_title)
            .setSubtitle(resString)
            .setupConfirmButton(
                android.R.string.ok,
                object : PopupDialog.PopupDialogConfirmListener {
                    override fun onClickConfirmButton() {
                        block()
                    }
                }
            )
            .setupCancelButton(
                android.R.string.cancel,
                object : PopupDialog.PopupDialogCancelListener {
                    override fun onClickCancelButton() {
                        goalDetailsViewModel.hideDialogs()
                    }
                })
            .build()
            .show(supportFragmentManager, PopupDialog.TAG)

    private fun showDefaultDialog(resString: Int) =
        PopupDialog.Builder()
            .setTitle(R.string.goal_warning_title)
            .setSubtitle(resString)
            .setupConfirmButton(
                R.string.button_ok,
                object : PopupDialog.PopupDialogConfirmListener {
                    override fun onClickConfirmButton() {
                        goalDetailsViewModel.hideDialogs()
                    }
                }
            )
            .build()
            .show(supportFragmentManager, PopupDialog.TAG)

    private fun expandBar(hasToExpand: Boolean) = appBarLayout.setExpanded(hasToExpand)

    companion object {
        private const val GOAL_TAG = "GOAL"
        private const val HAS_CHANGED = "HAS_CHANGED"
        private const val IS_FROM_DONE_GOALS = "IS_FROM_DONE_GOALS"
        private const val FIRST_POSITION = 0
    }
}