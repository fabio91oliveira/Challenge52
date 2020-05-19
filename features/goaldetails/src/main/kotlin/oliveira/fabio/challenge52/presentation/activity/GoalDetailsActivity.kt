package oliveira.fabio.challenge52.presentation.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import features.goaldetails.R
import kotlinx.android.synthetic.main.activity_goal_details.*
import oliveira.fabio.challenge52.BaseActivity
import oliveira.fabio.challenge52.extensions.isVisible
import oliveira.fabio.challenge52.presentation.action.GoalDetailsActions
import oliveira.fabio.challenge52.presentation.adapter.AdapterItem
import oliveira.fabio.challenge52.presentation.adapter.ItemsAdapter
import oliveira.fabio.challenge52.presentation.bottomsheetdialogfragment.OptionsBottomPopup
import oliveira.fabio.challenge52.presentation.dialogfragment.FullScreenDialog
import oliveira.fabio.challenge52.presentation.dialogfragment.LoadingDialog
import oliveira.fabio.challenge52.presentation.dialogfragment.PopupDialog
import oliveira.fabio.challenge52.presentation.viewmodel.GoalDetailsViewModel
import oliveira.fabio.challenge52.presentation.viewstate.Dialog
import oliveira.fabio.challenge52.presentation.vo.GoalResult
import oliveira.fabio.challenge52.presentation.vo.ItemDetail
import oliveira.fabio.challenge52.presentation.vo.TopDetails
import org.koin.androidx.viewmodel.ext.android.getStateViewModel

class GoalDetailsActivity : BaseActivity(R.layout.activity_goal_details),
    ItemsAdapter.OnClickItemListener {

    private val intentExtras by lazy { intent.extras }
    private val isFromDoneGoals by lazy { intentExtras?.getBoolean(IS_FROM_DONE_GOALS) ?: false }

    private lateinit var goalDetailsViewModel: GoalDetailsViewModel

    private val weeksAdapter by lazy { ItemsAdapter(this@GoalDetailsActivity, isFromDoneGoals) }
    private val progressDialog by lazy {
        LoadingDialog(
            this,
            R.string.all_please_wait,
            R.string.all_loading
        )
    }

    private lateinit var newIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSavedValues(savedInstanceState)
        setup()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        with(outState) {
            putParcelable(
                HAS_CHANGED, newIntent.getParcelableExtra<GoalResult>(
                    HAS_CHANGED
                )
            )
            super.onSaveInstanceState(this)
        }
    }

    override fun onBackPressed() = closeDetails()

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.goal_details_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.more_options -> {
                showOptions()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClickItem(itemDetail: ItemDetail, position: Int) {
        goalDetailsViewModel.changeWeekStatus(itemDetail, position)
    }

    private fun setup() {
        setupViewModel()
        setupToolbar()
        setupRecyclerView()
        setupSwipeRefreshLayout()
        setupObservables()
    }

    private fun setupSavedValues(savedInstanceState: Bundle? = null) {
        savedInstanceState?.also {
            newIntent = Intent().apply {
                putExtra(
                    HAS_CHANGED, it.getParcelable<GoalResult>(
                        HAS_CHANGED
                    )
                )
            }
        } ?: run {
            newIntent = Intent().apply {
                putExtra(
                    HAS_CHANGED,
                    GoalResult()
                )
            }
        }
    }

    private fun setupViewModel() {
        goalDetailsViewModel = getStateViewModel(bundle = intentExtras)
    }

    private fun setupObservables() {
        with(goalDetailsViewModel) {
            goalDetailsViewState.observe(this@GoalDetailsActivity, Observer {
                showLoading(it.isLoading)
                showWeekLoading(it.isItemsBeingUpdated)
                handleDialog(it.dialog)
                showContent(it.isContentVisible)
                addDetailsGoal(it.adapterList)
                updateTopDetails(it.topDetails)
            })
            goalDetailsActions.observe(this@GoalDetailsActivity, Observer {
                when (it) {
                    is GoalDetailsActions.UpdateDetails -> {
                        updateDetailsGoal()
                    }
                    is GoalDetailsActions.UpdateDetailsWithPosition -> {
                        updateWeek(it.position)
                        goalDetailsViewModel.showConfirmationDialogDoneGoalWhenUpdated()
                    }
                    is GoalDetailsActions.ShowConfirmationMessage -> {
                        showSnackBar(it.stringRes)
                        newIntent.putExtra(
                            HAS_CHANGED,
                            GoalResult()
                                .apply { setChangeUpdated() })
                    }
                    is GoalDetailsActions.RemoveGoal -> {
                        newIntent.putExtra(
                            HAS_CHANGED,
                            GoalResult()
                                .apply { setChangeRemoved() })
                        closeDetails()
                    }
                    is GoalDetailsActions.CompleteGoal -> {
                        newIntent.putExtra(
                            HAS_CHANGED,
                            GoalResult()
                                .apply { setChangeCompleted() })
                        closeDetails()
                    }
                    is GoalDetailsActions.CriticalError -> {
                        showFullScreenDialog(
                            it.titleRes,
                            it.descriptionRes
                        )
                    }
                }
            })
        }
    }

    private fun setupToolbar() {
        with(toolbar) {
            setSupportActionBar(this)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            setNavigationOnClickListener { closeDetails() }
        }
    }

    private fun setupRecyclerView() {
        with(rvDetails) {
            layoutManager =
                LinearLayoutManager(this@GoalDetailsActivity, RecyclerView.VERTICAL, false)
            adapter = weeksAdapter
            itemAnimator = null
        }
    }

    private fun setupSwipeRefreshLayout() {
        with(srlDetails) {
            setColorSchemeResources(
                android.R.color.white
            )
            setProgressBackgroundColorSchemeColor(
                androidx.core.content.ContextCompat.getColor(
                    context,
                    R.color.color_primary
                )
            )
            setOnRefreshListener {
                goalDetailsViewModel.mountDetails()
            }
        }
    }

    private fun closeDetails() {
        setResult(Activity.RESULT_OK, newIntent)
        finish()
    }

    private fun showLoading(hasToShow: Boolean) {
        srlDetails.isRefreshing = hasToShow
        shimmerContent.isVisible = hasToShow
    }

    private fun showWeekLoading(hasToShow: Boolean) =
        if (hasToShow) progressDialog.show() else progressDialog.dismiss()

    private fun showContent(hasToShow: Boolean) {
        contentCard.isVisible = hasToShow
        rvDetails.isVisible = hasToShow
    }

    private fun addDetailsGoal(list: MutableList<AdapterItem<String, ItemDetail>>) {
        weeksAdapter.clearList()
        weeksAdapter.addList(list)
    }

    private fun updateDetailsGoal() {
        weeksAdapter.notifyDataSetChanged()
    }

    private fun updateWeek(position: Int) {
        weeksAdapter.notifyItemChanged(position)
    }

    private fun updateTopDetails(topDetails: TopDetails) {
        with(topDetails) {
            txtName.text = goalName
            txtTotalMoneySaved.text = totalMoneySaved
            txtTotalToSaveMoneyTitle.text =
                getString(R.string.goal_details_card_money_of, totalMoneyToSave)
            txtStatus.text = getString(statusStrRes)
            txtTotalPercentage.text =
                totalPercentsCompleted.toString() + getString(R.string.progress_value_percent)
            txtPeriod.text = getString(periodStrRes)
            txtProgress.text =
                getString(
                    R.string.goal_details_card_period_range,
                    totalCompletedItems.toString(),
                    totalItems.toString()
                )
        }
    }

    private fun handleDialog(dialogViewState: Dialog) {
        when (dialogViewState) {
            is Dialog.DefaultDialogMoveToDone -> {
                showDefaultDialog(
                    dialogViewState.imageRes,
                    dialogViewState.titleRes,
                    dialogViewState.descriptionRes
                )
            }
            is Dialog.RegularErrorDialog -> {
                showDefaultDialog(
                    dialogViewState.imageRes,
                    dialogViewState.titleRes,
                    dialogViewState.descriptionRes
                )
            }
            is Dialog.ConfirmationDialogRemoveGoal -> {
                showConfirmDialog(
                    dialogViewState.imageRes,
                    dialogViewState.titleRes,
                    dialogViewState.descriptionRes
                ) {
                    goalDetailsViewModel.removeGoal()
                    goalDetailsViewModel.hideDialogs()
                }
            }
            is Dialog.ConfirmationDialogDoneGoal -> {
                showConfirmDialog(
                    dialogViewState.imageRes,
                    dialogViewState.titleRes,
                    dialogViewState.descriptionRes
                ) {
                    goalDetailsViewModel.completeGoal()
                }
            }
        }
    }

    private fun showSnackBar(@StringRes stringRes: Int) =
        Snackbar.make(content, resources.getText(stringRes), Snackbar.LENGTH_SHORT).show()

    private fun showFullScreenDialog(
        @StringRes titleRes: Int,
        @StringRes descriptionRes: Int
    ) {
        FullScreenDialog.Builder()
            .setTitle(titleRes)
            .setSubtitle(descriptionRes)
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

    private inline fun showConfirmDialog(
        @DrawableRes resImage: Int,
        @StringRes resTitle: Int,
        @StringRes resDescription: Int,
        crossinline block: () -> Unit
    ) =
        PopupDialog.Builder()
            .setImage(resImage)
            .setTitle(resTitle)
            .setSubtitle(resDescription)
            .setupConfirmButton(
                android.R.string.ok,
                object : PopupDialog.PopupDialogConfirmListener {
                    override fun onClickConfirmButton() {
                        block()
                    }
                }
            )
            .setupConfirmButtonColor(R.color.color_primary)
            .setupCancelButton(
                android.R.string.cancel,
                object : PopupDialog.PopupDialogCancelListener {
                    override fun onClickCancelButton() {
                        goalDetailsViewModel.hideDialogs()
                    }
                })
            .build()
            .show(supportFragmentManager, PopupDialog.TAG)

    private fun showDefaultDialog(
        @DrawableRes resImage: Int,
        @StringRes resTitle: Int,
        @StringRes resDescription: Int
    ) =
        PopupDialog.Builder()
            .setTitle(resTitle)
            .setSubtitle(resDescription)
            .setupConfirmButtonColor(R.color.color_primary)
            .setImage(resImage)
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

    private fun showOptions() {
        OptionsBottomPopup.Builder()
            .setOptions(createOptionsToDetailsScreen(isFromDoneGoals))
            .setFinishWhenClickOption(true)
            .build()
            .show(supportFragmentManager, OptionsBottomPopup.TAG)

    }

    private fun createOptionsToDetailsScreen(isFromDone: Boolean) =
        mutableListOf<OptionsBottomPopup.Option>().apply {
            if (isFromDone.not()) {
                add(
                    OptionsBottomPopup.Option(
                        R.drawable.ic_done,
                        R.string.goal_details_option_mark_as_done,
                        object : OptionsBottomPopup.Option.OptionsBottomPopupOptionListener {
                            override fun onClickOption() {
                                goalDetailsViewModel.showConfirmationDialogDoneGoal()
                            }
                        },
                        R.color.color_soft_grey_5
                    )
                )
            }
            add(
                OptionsBottomPopup.Option(
                    R.drawable.ic_remove,
                    R.string.goal_details_option_remove,
                    object : OptionsBottomPopup.Option.OptionsBottomPopupOptionListener {
                        override fun onClickOption() {
                            goalDetailsViewModel.showConfirmationDialogRemoveGoal()
                        }
                    },
                    R.color.color_soft_grey_5
                )
            )
        }.toTypedArray()

    companion object {
        private const val HAS_CHANGED = "HAS_CHANGED"
        private const val IS_FROM_DONE_GOALS = "IS_FROM_DONE_GOALS"

        fun newIntent(context: Context) = Intent(
            context,
            GoalDetailsActivity::class.java
        )
    }
}