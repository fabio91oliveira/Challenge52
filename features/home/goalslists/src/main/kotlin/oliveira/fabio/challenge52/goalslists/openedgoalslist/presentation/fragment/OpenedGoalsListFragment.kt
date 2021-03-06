package oliveira.fabio.challenge52.goalslists.openedgoalslist.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import features.home.goalslists.R
import kotlinx.android.synthetic.main.fragment_goals_lists.*
import kotlinx.android.synthetic.main.fragment_opened_goals_list.*
import oliveira.fabio.challenge52.extensions.isVisible
import oliveira.fabio.challenge52.features.NewGoalNavigation
import oliveira.fabio.challenge52.features.GoalDetailsNavigation
import oliveira.fabio.challenge52.goalslists.openedgoalslist.presentation.action.OpenedGoalsActions
import oliveira.fabio.challenge52.goalslists.openedgoalslist.presentation.adapter.OpenedGoalAdapter
import oliveira.fabio.challenge52.goalslists.presentation.viewmodel.GoalsListsViewModel
import oliveira.fabio.challenge52.presentation.vo.GoalResultTypeEnum
import oliveira.fabio.challenge52.presentation.vo.GoalResult
import oliveira.fabio.challenge52.presentation.view.StateView
import oliveira.fabio.challenge52.presentation.vo.Goal
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

internal class OpenedGoalsListFragment : Fragment(R.layout.fragment_opened_goals_list),
    OpenedGoalAdapter.OnClickGoalListener {

    private val goalsListsViewModel: GoalsListsViewModel by sharedViewModel()
    private val newGoalNavigation: NewGoalNavigation by inject()
    private val goalDetailsNavigation: GoalDetailsNavigation by inject()
    private val openedGoalsAdapter by lazy {
        OpenedGoalAdapter(
            this
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            Activity.RESULT_OK -> when (requestCode) {
                REQUEST_CODE_DETAILS -> {
                    data?.apply {
                        (getParcelableExtra<GoalResult>(HAS_CHANGED)).let {
                            if (it.hasChanged) {
                                when (it.type) {
                                    GoalResultTypeEnum.REMOVED -> {
                                        goalsListsViewModel.showMessageHasOneOpenedGoalDeleted()
                                        goalsListsViewModel.listOpenedGoals()
                                    }
                                    GoalResultTypeEnum.UPDATED -> {
                                        goalsListsViewModel.showMessageHasOpenedGoalBeenUpdated()
                                        goalsListsViewModel.listOpenedGoals()
                                    }
                                    GoalResultTypeEnum.COMPLETED -> {
                                        goalsListsViewModel.showMessageHasOpenedGoalBeenCompleted()
                                        goalsListsViewModel.listOpenedGoals()
                                        goalsListsViewModel.listDoneGoals()
                                    }
                                    GoalResultTypeEnum.NONE -> {
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Activity.RESULT_CANCELED -> when (requestCode) {
                REQUEST_CODE_CREATE -> {
                    goalsListsViewModel.showMessageGoalHasBeenCreated()
                    goalsListsViewModel.listOpenedGoals()
                }
                REQUEST_CODE_DETAILS -> {
                    data?.apply {
                        (getParcelableExtra<GoalResult>(HAS_CHANGED)).let {
                            if (it.hasChanged) goalsListsViewModel.showMessageHasOpenedGoalBeenUpdated()
                        }
                    }
                }
            }
        }
    }

    private fun init() {
        setupObservables()
        setupClickListener()
        setupRecyclerView()
        setupSwipeRefreshLayout()
    }

    private fun setupObservables() {
        with(goalsListsViewModel) {
            openedGoalsViewState.observe(viewLifecycleOwner, Observer {
                showAddButton(it.isAddButtonVisible)
                showLoading(it.isLoading)
                showOpenedGoalsList(it.isOpenedGoalsListVisible)
                showStateView(stateViewEmpty, it.isEmptyStateVisible)
                showStateView(stateViewError, it.isErrorVisible)
            })
            openedGoalsActions.observe(viewLifecycleOwner, Observer {
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
                    is OpenedGoalsActions.Empty -> {
                        openedGoalsAdapter.clearList()
                        setStateView(stateViewEmpty, it.openedGoalsStateResources)
                    }
                    is OpenedGoalsActions.Error -> {
                        setStateView(stateViewError, it.openedGoalsStateResources) {
                            listOpenedGoals()
                        }
                    }
                }
            })
        }
    }

    private fun setupSwipeRefreshLayout() {
        with(srlGoalsList) {
            setColorSchemeResources(
                android.R.color.white
            )
            setProgressBackgroundColorSchemeColor(
                ContextCompat.getColor(
                    context,
                    R.color.color_primary
                )
            )
            setOnRefreshListener {
                goalsListsViewModel.listOpenedGoals()
            }
        }
    }

    private fun setupRecyclerView() {
        with(rvOpenedGoalsList) {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = openedGoalsAdapter
            itemAnimator = null
        }

        rvOpenedGoalsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0)
                    goalsListsViewModel.hideAddButton()
                else if (dy < 0)
                    goalsListsViewModel.showAddButton()
            }
        })
    }

    private fun setupClickListener() {
        parentFragment?.fabAdd?.setOnClickListener {
            goToCreateGoal()
        }
    }

    private fun showSnackBar(@StringRes stringRes: Int) =
        Snackbar.make(content, resources.getText(stringRes), Snackbar.LENGTH_SHORT).show()

    private fun goToCreateGoal() {
        context?.also {
            startActivityForResult(
                newGoalNavigation.navigateToChallengeSelect(it),
                REQUEST_CODE_CREATE
            )
        }
    }

    private fun goToGoalDetails(goal: Goal) {
        context?.also {
            startActivityForResult(
                goalDetailsNavigation.navigateToFeature(it)
                    .putExtra(GOAL_TAG, goal),
                REQUEST_CODE_DETAILS
            )
        }
    }

    override fun onClickGoal(goal: Goal) =
        goToGoalDetails(goal)

    private fun showOpenedGoalsList(hasToShow: Boolean) {
        rvOpenedGoalsList.isVisible = hasToShow
    }

    private fun showStateView(
        stateView: StateView,
        hasToShow: Boolean
    ) {
        stateView.isVisible = hasToShow
    }

    private fun showLoading(hasToShow: Boolean) {
        srlGoalsList.isRefreshing = hasToShow
    }

    private fun setStateView(
        stateView: StateView,
        openedGoalsStateResources: OpenedGoalsActions.OpenedGoalsStateResources,
        block: (() -> Unit)? = null
    ) {
        stateView.apply {
            setImage(openedGoalsStateResources.imageRes)
            setTitle(resources.getString(openedGoalsStateResources.titleRes))
            setDescription(resources.getString(openedGoalsStateResources.descriptionRes))
            block?.also {
                openedGoalsStateResources.buttonTextRes?.also { buttonText ->
                    setupButton(resources.getString(buttonText), it)
                }
            }
        }
    }

    private fun showAddButton(hasToShow: Boolean) =
        if (hasToShow) parentFragment?.fabAdd?.show() else parentFragment?.fabAdd?.hide()

    companion object {
        private const val REQUEST_CODE_CREATE = 300
        private const val REQUEST_CODE_DETAILS = 400
        private const val GOAL_TAG = "GOAL"
        private const val HAS_CHANGED = "HAS_CHANGED"

        fun newInstance() =
            OpenedGoalsListFragment()
    }
}
