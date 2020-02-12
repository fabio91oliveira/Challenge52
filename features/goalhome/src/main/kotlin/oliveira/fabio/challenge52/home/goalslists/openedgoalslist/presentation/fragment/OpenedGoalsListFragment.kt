package oliveira.fabio.challenge52.home.goalslists.openedgoalslist.presentation.fragment

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import features.goalhome.R
import kotlinx.android.synthetic.main.fragment_opened_goals_list.*
import oliveira.fabio.challenge52.actions.Actions
import oliveira.fabio.challenge52.extensions.isVisible
import oliveira.fabio.challenge52.home.goalslists.openedgoalslist.presentation.action.OpenedGoalsActions
import oliveira.fabio.challenge52.home.goalslists.openedgoalslist.presentation.adapter.OpenedGoalsAdapter
import oliveira.fabio.challenge52.home.goalslists.openedgoalslist.presentation.viewstate.OpenedGoalsDialog
import oliveira.fabio.challenge52.home.goalslists.presentation.viewmodel.GoalsListsViewModel
import oliveira.fabio.challenge52.model.vo.ActivityResultTypeEnum
import oliveira.fabio.challenge52.model.vo.ActivityResultValueObject
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks
import oliveira.fabio.challenge52.presentation.dialogfragment.PopupDialog
import org.koin.android.viewmodel.ext.android.sharedViewModel

class OpenedGoalsListFragment : Fragment(R.layout.fragment_opened_goals_list),
    OpenedGoalsAdapter.OnClickGoalListener {

    private val goalsListsViewModel: GoalsListsViewModel by sharedViewModel()
    private val openedGoalsAdapter by lazy {
        OpenedGoalsAdapter(
            this
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.let {
            initObservables()
            initClickListener()
            initRecyclerView()
        } ?: run {
            init()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            Activity.RESULT_OK -> when (requestCode) {
                REQUEST_CODE_CREATE -> {
                    goalsListsViewModel.showMessageGoalHasBeenCreated()
                    goalsListsViewModel.listOpenedGoals()
                }
                REQUEST_CODE_DETAILS -> {
                    data?.apply {
                        (getSerializableExtra(HAS_CHANGED) as ActivityResultValueObject).let {
                            if (it.hasChanged) {
                                when (it.type) {
                                    ActivityResultTypeEnum.REMOVED -> {
                                        goalsListsViewModel.showMessageHasOneOpenedGoalDeleted()
                                        goalsListsViewModel.listOpenedGoals()
                                    }
                                    ActivityResultTypeEnum.UPDATED -> {
                                        goalsListsViewModel.showMessageHasOpenedGoalBeenUpdated()
                                        goalsListsViewModel.listOpenedGoals()
                                    }
                                    ActivityResultTypeEnum.COMPLETED -> {
                                        goalsListsViewModel.showMessageHasOpenedGoalBeenCompleted()
                                        goalsListsViewModel.listOpenedGoals()
                                        goalsListsViewModel.listDoneGoals()
                                    }
                                    ActivityResultTypeEnum.NONE -> {
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Activity.RESULT_CANCELED -> when (requestCode) {
                REQUEST_CODE_CREATE -> {
                    goalsListsViewModel.showAddButton()
                }
                REQUEST_CODE_DETAILS -> {
                    data?.apply {
                        (getSerializableExtra(HAS_CHANGED) as ActivityResultValueObject).let {
                            if (it.hasChanged) goalsListsViewModel.showMessageHasOpenedGoalBeenUpdated()
                        }
                    }
                }
            }
        }
    }

    private fun init() {
        initObservables()
        initClickListener()
        initRecyclerView()
    }

    private fun initObservables() {
        with(goalsListsViewModel) {
            openedGoalsViewState.observe(this@OpenedGoalsListFragment, Observer {
                showAddButton(it.isAddButtonVisible)
                showRemoveButton(it.isDeleteButtonVisible)
                showLoading(it.isLoading)
                showOpenedGoalsList(it.isOpenedGoalsListVisible)
                showEmptyState(it.isEmptyStateVisible)
                showErrorState(it.isErrorVisible)
                handleDialog(it.dialog)
            })
            openedGoalsActions.observe(this@OpenedGoalsListFragment, Observer {
                when (it) {
                    is OpenedGoalsActions.ShowMessage -> {
                        showSnackBar(it.stringRes)
                    }
                    is OpenedGoalsActions.RefreshList -> {
                        goalsListsViewModel.listOpenedGoals()
                    }
                    is OpenedGoalsActions.OpenedGoalsList -> {
                        openedGoalsAdapter.clearList()
                        openedGoalsAdapter.addList(it.openedGoalsList)
                    }
                    is OpenedGoalsActions.ClearList -> {
                        openedGoalsAdapter.clearList()
                    }
                    is OpenedGoalsActions.Error -> {
                        setErrorState(it.errorMessageRes)
                    }
                }
            })
        }
    }

    private fun initRecyclerView() {
        with(rvGoalsList) {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = openedGoalsAdapter
            itemAnimator = null
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0)
                        goalsListsViewModel.hideAddButton()
                    else if (dy < 0)
                        goalsListsViewModel.showAddButton()
                }
            })
        }
    }

    private fun initClickListener() {
        fabAdd.setOnClickListener {
            openGoalCreateActivity()
        }
        fabRemove.setOnClickListener {
            goalsListsViewModel.showRemoveOpenedGoalsConfirmationDialog()
        }
        txtError.setOnClickListener {
            goalsListsViewModel.listOpenedGoals()
        }
        imgError.setOnClickListener {
            goalsListsViewModel.listOpenedGoals()
        }
    }

    private fun showSnackBar(@StringRes stringRes: Int) =
        Snackbar.make(coordinatorLayout, resources.getText(stringRes), Snackbar.LENGTH_SHORT).show()

    private fun openGoalCreateActivity() =
        startActivityForResult(
            Actions.openGoalCreate(requireContext()),
            REQUEST_CODE_CREATE
        )

    private fun openGoalDetailsActivity(goal: GoalWithWeeks) = startActivityForResult(
        Actions.openGoalDetails(requireContext()).putExtra(GOAL_TAG, goal),
        REQUEST_CODE_DETAILS
    )

    private fun showConfirmDialog(
        message: String,
        block: () -> Unit
    ) =
        PopupDialog.Builder()
            .setTitle(R.string.goal_warning_title)
            .setSubtitle(message)
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
                        goalsListsViewModel.hideOpenedDialogs()
                    }
                })
            .build()
            .show(childFragmentManager, PopupDialog.TAG)

    override fun onClickRemove(goal: GoalWithWeeks) =
        goalsListsViewModel.removeOpenedGoalFromListToRemove(goal)

    override fun onClickAdd(goal: GoalWithWeeks) =
        goalsListsViewModel.addOpenedGoalToListToRemove(goal)

    override fun onLongClick(goal: GoalWithWeeks) =
        goalsListsViewModel.addOpenedGoalToListToRemove(goal)

    override fun onClickGoal(goal: GoalWithWeeks) =
        openGoalDetailsActivity(goal)

    private fun showOpenedGoalsList(hasToShow: Boolean) {
        rvGoalsList.isVisible = hasToShow
    }

    private fun showEmptyState(hasToShow: Boolean) {
        if (hasToShow) initAnimationsNoGoals()
        txtNoGoalsFirst.isVisible = hasToShow
        imgNoGoals.isVisible = hasToShow
    }

    private fun showLoading(hasToShow: Boolean) {
        loading.isVisible = hasToShow
    }

    private fun setErrorState(@StringRes stringRes: Int) {
        txtError.text = resources.getString(stringRes)
    }

    private fun showErrorState(hasToShow: Boolean) {
        if (hasToShow) initAnimationsError()
        txtError.isVisible = hasToShow
        imgError.isVisible = hasToShow
    }

    private fun showAddButton(hasToShow: Boolean) =
        if (hasToShow) fabAdd.show() else fabAdd.hide()

    private fun showRemoveButton(hasToShow: Boolean) =
        if (hasToShow) fabRemove.show() else fabRemove.hide()

    private fun handleDialog(openedGoalsDialog: OpenedGoalsDialog) {
        if (openedGoalsDialog is OpenedGoalsDialog.RemoveConfirmationDialog)
            showConfirmDialog(
                resources.getQuantityString(
                    openedGoalsDialog.pluralRes,
                    openedGoalsDialog.doneGoalsRemovedSize
                )
            ) {
                goalsListsViewModel.removeOpenedGoals()
                goalsListsViewModel.hideOpenedDialogs()
            }
    }

    // TODO
    private fun initAnimationsNoGoals() {
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.interpolator = DecelerateInterpolator()
        fadeIn.duration = 2000

        val animation = AnimationSet(false)
        animation.addAnimation(fadeIn)
        txtNoGoalsFirst?.animation = animation
        imgNoGoals?.animation = animation

        val valueAnimator = ValueAnimator.ofFloat(-100f, 0f)
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator.duration = 1000
        valueAnimator.addUpdateListener {
            val progress = it.animatedValue as Float
            txtNoGoalsFirst?.translationY = progress
            imgNoGoals?.translationY = progress
        }
        valueAnimator.start()
    }

    private fun initAnimationsError() {
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.interpolator = DecelerateInterpolator()
        fadeIn.duration = 2000

        val animation = AnimationSet(false)
        animation.addAnimation(fadeIn)
        txtError?.animation = animation
        imgError?.animation = animation

        val valueAnimator = ValueAnimator.ofFloat(-100f, 0f)
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator.duration = 1000
        valueAnimator.addUpdateListener {
            val progress = it.animatedValue as Float
            txtError?.translationY = progress
            imgError?.translationY = progress
        }
        valueAnimator.start()
    }

    companion object {
        private const val REQUEST_CODE_CREATE = 300
        private const val REQUEST_CODE_DETAILS = 400
        private const val GOAL_TAG = "GOAL"
        private const val HAS_CHANGED = "HAS_CHANGED"

        fun newInstance() =
            OpenedGoalsListFragment()
    }
}
