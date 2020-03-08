package oliveira.fabio.challenge52.presentation.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import features.goaldetails.R
import kotlinx.android.synthetic.main.activity_goal_details.*
import oliveira.fabio.challenge52.BaseActivity
import oliveira.fabio.challenge52.domain.model.Week
import oliveira.fabio.challenge52.extensions.isVisible
import oliveira.fabio.challenge52.model.vo.ActivityResultValueObject
import oliveira.fabio.challenge52.presentation.action.GoalDetailsActions
import oliveira.fabio.challenge52.presentation.adapter.WeeksAdapter
import oliveira.fabio.challenge52.presentation.adapter.vo.AdapterItem
import oliveira.fabio.challenge52.presentation.dialogfragment.FullScreenDialog
import oliveira.fabio.challenge52.presentation.dialogfragment.PopupDialog
import oliveira.fabio.challenge52.presentation.viewmodel.GoalDetailsViewModel
import oliveira.fabio.challenge52.presentation.viewstate.Dialog
import oliveira.fabio.challenge52.presentation.vo.TopDetails
import org.koin.androidx.viewmodel.ext.android.getStateViewModel

class GoalDetailsActivity : BaseActivity(R.layout.activity_goal_details),
    WeeksAdapter.OnClickWeekListener {

    private val isFromDoneGoals by lazy { intent.extras?.getBoolean(IS_FROM_DONE_GOALS) ?: false }
    private val weeksAdapter by lazy { WeeksAdapter(this@GoalDetailsActivity, isFromDoneGoals) }
    private lateinit var goalDetailsViewModel: GoalDetailsViewModel

    private lateinit var newIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        with(outState) {
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
                goalDetailsViewModel.showConfirmationDialogDoneGoal()
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
        with(goalDetailsViewModel) {
            if (isDateAfterTodayWhenWeekIsNotChecked(week))
                showConfirmationDialogUpdateWeek(week)
            else
                changeWeekStatus(week)
        }
    }

    private fun setup() {
        setupSavedValues()
        setupViewModel()
        setupToolbar()
        setupRecyclerView()
        setupObservables()
    }

    private fun setupSavedValues(savedInstanceState: Bundle? = null) {
        savedInstanceState?.also {
            newIntent = Intent().apply {
                putExtra(
                    HAS_CHANGED, it.getSerializable(
                        HAS_CHANGED
                    )
                )
            }
        } ?: run {
            newIntent = Intent().apply { putExtra(HAS_CHANGED, ActivityResultValueObject()) }
        }
    }

    private fun setupViewModel() {
        goalDetailsViewModel = getStateViewModel(bundle = intent.extras)
    }

    private fun setupObservables() {
        with(goalDetailsViewModel) {
            goalDetailsViewState.observe(this@GoalDetailsActivity, Observer {
                showLoading(it.isLoading)
                showWeekLoading(it.isWeekBeingUpdated)
                handleDialog(it.dialog)
                showContent(it.isContentVisible)
            })
            goalDetailsActions.observe(this@GoalDetailsActivity, Observer {
                when (it) {
//                    is GoalDetailsActions.AddedGoalsFirstTime -> {
//                        updateItemList(it.itemsList)
//                        rvWeeks.scheduleLayoutAnimation()
////                        if (!isFromDoneGoals)
////                            goalDetailsViewModel.showConfirmationDialogDoneGoalWhenUpdated(
////                                goalWithWeeks
////                            )
//                    }
                    is GoalDetailsActions.PopulateGoalInformation -> {
                        addDetailsGoal(it.list)
                        if (!isFromDoneGoals)
                            goalDetailsViewModel.showConfirmationDialogDoneGoalWhenUpdated()
                    }
                    is GoalDetailsActions.UpdateWeek -> {
                        updateWeek(it.week)
                        showSnackBar(it.stringRes)
                        newIntent.putExtra(
                            HAS_CHANGED,
                            ActivityResultValueObject().apply { setChangeUpdated() })
                        goalDetailsViewModel.showConfirmationDialogDoneGoalWhenUpdated()
                    }
                    is GoalDetailsActions.UpdateTopDetails -> {
                        updateTopDetails(it.topDetails)
                    }
                    is GoalDetailsActions.RemoveGoal -> {
                        newIntent.putExtra(
                            HAS_CHANGED,
                            ActivityResultValueObject().apply { setChangeRemoved() })
                        closeDetails()
                    }
                    is GoalDetailsActions.CompleteGoal -> {
                        newIntent.putExtra(
                            HAS_CHANGED,
                            ActivityResultValueObject().apply { setChangeCompleted() })
                        closeDetails()
                    }
                    is GoalDetailsActions.CriticalError -> {
                        showFullScreenDialog(it.errorMessageRes)
                    }
                }
            })
        }
    }

    private fun setupToolbar() {
        with(toolbar) {
            setSupportActionBar(this)
            supportActionBar?.title = goalDetailsViewModel.goalName
            setNavigationOnClickListener { closeDetails() }
        }
    }

    private fun setupRecyclerView() {
        with(rvWeeks) {
            layoutManager =
                LinearLayoutManager(this@GoalDetailsActivity, RecyclerView.VERTICAL, false)
            adapter = weeksAdapter
            itemAnimator = null
        }
    }

    private fun closeDetails() {
        setResult(Activity.RESULT_OK, newIntent)
        finish()
    }

    private fun showLoading(hasToShow: Boolean) {
        loading.isVisible = hasToShow
    }

    private fun showWeekLoading(hasToShow: Boolean) = weeksAdapter.setLoading(hasToShow)

    private fun showContent(hasToShow: Boolean) {
        rvWeeks.isVisible = hasToShow
    }

    private fun addDetailsGoal(list: MutableList<AdapterItem<TopDetails, String, Week>>) {
        weeksAdapter.addList(list)
    }

    private fun updateWeek(week: Week) {
        weeksAdapter.updateWeek(week)
    }

    private fun updateTopDetails(topDetails: TopDetails) {
        weeksAdapter.updateTopDetail(topDetails)
    }

    private fun handleDialog(dialogViewState: Dialog) {
        when (dialogViewState) {
            is Dialog.DefaultDialogMoveToDone -> {
                showDefaultDialog(dialogViewState.stringRes)
            }
            is Dialog.RegularErrorDialog -> {
                showDefaultDialog(dialogViewState.stringRes)
            }
            is Dialog.ConfirmationDialogRemoveGoal -> {
                showConfirmDialog(
                    dialogViewState.stringRes
                ) {
                    goalDetailsViewModel.removeGoal()
                    goalDetailsViewModel.hideDialogs()
                }
            }
            is Dialog.ConfirmationDialogDoneGoal -> {
                showConfirmDialog(
                    dialogViewState.stringRes
                ) {
                    goalDetailsViewModel.completeGoal()
                }
            }
            is Dialog.ConfirmationDialogUpdateWeek -> {
                showConfirmDialog(
                    dialogViewState.stringRes
                ) {
                    goalDetailsViewModel.changeWeekStatus(dialogViewState.week)
                }
            }
        }
    }

    private fun showSnackBar(@StringRes stringRes: Int) =
        Snackbar.make(content, resources.getText(stringRes), Snackbar.LENGTH_SHORT).show()

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

    companion object {
        private const val HAS_CHANGED = "HAS_CHANGED"
        private const val IS_FROM_DONE_GOALS = "IS_FROM_DONE_GOALS"
    }
}