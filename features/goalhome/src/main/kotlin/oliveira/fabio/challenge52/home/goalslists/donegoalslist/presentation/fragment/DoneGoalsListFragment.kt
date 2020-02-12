package oliveira.fabio.challenge52.home.goalslists.donegoalslist.presentation.fragment

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
import kotlinx.android.synthetic.main.fragment_done_goals_list.*
import oliveira.fabio.challenge52.actions.Actions
import oliveira.fabio.challenge52.extensions.isVisible
import oliveira.fabio.challenge52.home.goalslists.donegoalslist.presentation.action.DoneGoalsActions
import oliveira.fabio.challenge52.home.goalslists.donegoalslist.presentation.adapter.DoneGoalsAdapter
import oliveira.fabio.challenge52.home.goalslists.donegoalslist.presentation.viewstate.DoneGoalsDialog
import oliveira.fabio.challenge52.home.goalslists.presentation.viewmodel.GoalsListsViewModel
import oliveira.fabio.challenge52.model.vo.ActivityResultValueObject
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks
import oliveira.fabio.challenge52.presentation.dialogfragment.PopupDialog
import org.koin.android.viewmodel.ext.android.sharedViewModel

class DoneGoalsListFragment : Fragment(R.layout.fragment_done_goals_list),
    DoneGoalsAdapter.OnClickGoalListener {

    private val goalsListsViewModel: GoalsListsViewModel by sharedViewModel()
    private val doneGoalsAdapter by lazy {
        DoneGoalsAdapter(
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
                REQUEST_CODE_DETAILS -> {
                    (data?.getSerializableExtra(HAS_CHANGED) as ActivityResultValueObject).let {
                        if (it.hasChanged) with(goalsListsViewModel) {
                            showMessageHasOneDoneGoalDeleted()
                            listDoneGoals()
                        }
                    }
                }
            }
        }
    }

    override fun onClickRemove(goal: GoalWithWeeks) =
        goalsListsViewModel.removeDoneGoalFromListToRemove(goal)

    override fun onClickAdd(goal: GoalWithWeeks) =
        goalsListsViewModel.addDoneGoalToListToRemove(goal)

    override fun onLongClick(goal: GoalWithWeeks) =
        goalsListsViewModel.addDoneGoalToListToRemove(goal)

    override fun onClickGoal(goal: GoalWithWeeks) =
        openGoalDetailsActivity(goal)

    private fun init() {
        initObservables()
        initClickListener()
        initRecyclerView()
    }

    private fun initObservables() {
        with(goalsListsViewModel) {
            doneGoalsViewState.observe(this@DoneGoalsListFragment, Observer {
                showRemoveButton(it.isDeleteButtonVisible)
                showLoading(it.isLoading)
                showDoneGoalsList(it.isDoneGoalsListVisible)
                showEmptyState(it.isEmptyStateVisible)
                showErrorState(it.isErrorVisible)
                handleDialog(it.dialog)
            })
            doneGoalsActions.observe(this@DoneGoalsListFragment, Observer {
                when (it) {
                    is DoneGoalsActions.ShowMessage -> {
                        showSnackBar(resources.getString(it.stringRes))
                    }
                    is DoneGoalsActions.RefreshList -> {
                        listDoneGoals()
                    }
                    is DoneGoalsActions.DoneGoalsList -> {
                        doneGoalsAdapter.clearList()
                        doneGoalsAdapter.addList(it.doneGoalsList)
                    }
                    is DoneGoalsActions.ClearList -> {
                        doneGoalsAdapter.clearList()
                    }
                    is DoneGoalsActions.Error -> {
                        setErrorState(it.errorMessageRes)
                    }
                }
            })
        }
    }

    private fun initRecyclerView() {
        rvDoneGoalsList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvDoneGoalsList.adapter = doneGoalsAdapter
    }

    private fun initClickListener() {
        fabRemove.setOnClickListener {
            goalsListsViewModel.showRemoveDoneGoalsConfirmationDialog()
        }
        txtError.setOnClickListener { goalsListsViewModel.listDoneGoals() }
        imgError.setOnClickListener { goalsListsViewModel.listDoneGoals() }
    }

    private fun showRemoveButton(hasToShow: Boolean) =
        if (hasToShow) fabRemove.show() else fabRemove.hide()

    private fun showLoading(hasToShow: Boolean) {
        loading.isVisible = hasToShow
        rvDoneGoalsList.isVisible = hasToShow.not()
    }

    private fun showDoneGoalsList(hasToShow: Boolean) {
        rvDoneGoalsList.isVisible = hasToShow
    }

    private fun showEmptyState(hasToShow: Boolean) {
        if (hasToShow) initAnimationsNoDoneGoals()
        txtNoDoneGoalsFirst.isVisible = hasToShow
        imgNoDoneGoals.isVisible = hasToShow
    }

    private fun showErrorState(hasToShow: Boolean) {
        if (hasToShow) initAnimationsError()
        txtError.isVisible = hasToShow
        imgError.isVisible = hasToShow
    }

    private fun setErrorState(@StringRes stringRes: Int) {
        txtError.text = resources.getString(stringRes)
    }

    private fun showSnackBar(message: String) =
        Snackbar.make(
            coordinatorLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).show()

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
                        goalsListsViewModel.hideDoneDialogs()
                    }
                })
            .build()
            .show(childFragmentManager, PopupDialog.TAG)

    private fun openGoalDetailsActivity(goal: GoalWithWeeks) = startActivityForResult(
        Actions.openGoalDetails(requireContext()).putExtra(GOAL_TAG, goal)
            .putExtra(IS_FROM_DONE_GOALS, true),
        REQUEST_CODE_DETAILS
    )

    private fun handleDialog(doneGoalsDialog: DoneGoalsDialog) {
        if (doneGoalsDialog is DoneGoalsDialog.RemoveConfirmationDialog)
            showConfirmDialog(
                resources.getQuantityString(
                    doneGoalsDialog.pluralRes,
                    doneGoalsDialog.doneGoalsRemovedSize
                )
            ) {
                goalsListsViewModel.removeDoneGoals()
                goalsListsViewModel.hideDoneDialogs()
            }
    }

    // TODO REFAC

    private fun initAnimationsNoDoneGoals() {
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.interpolator = DecelerateInterpolator()
        fadeIn.duration = 2000

        val animation = AnimationSet(false)
        animation.addAnimation(fadeIn)
        txtNoDoneGoalsFirst?.animation = animation
        imgNoDoneGoals?.animation = animation

        val valueAnimator = ValueAnimator.ofFloat(-100f, 0f)
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator.duration = 1000
        valueAnimator.addUpdateListener {
            val progress = it.animatedValue as Float
            txtNoDoneGoalsFirst?.translationY = progress
            imgNoDoneGoals?.translationY = progress
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

    //

    companion object {
        private const val REQUEST_CODE_DETAILS = 400
        private const val GOAL_TAG = "GOAL"
        private const val HAS_CHANGED = "HAS_CHANGED"
        private const val IS_FROM_DONE_GOALS = "IS_FROM_DONE_GOALS"
        const val ACTIVITY_ERROR = -3

        fun newInstance() =
            DoneGoalsListFragment()
    }

}
