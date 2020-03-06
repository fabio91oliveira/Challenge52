package oliveira.fabio.challenge52.home.goalslists.donegoalslist.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import features.goalhome.R
import kotlinx.android.synthetic.main.fragment_done_goals_list.*
import oliveira.fabio.challenge52.actions.Actions
import oliveira.fabio.challenge52.domain.model.Goal
import oliveira.fabio.challenge52.extensions.doSlideDownAnimation
import oliveira.fabio.challenge52.extensions.isVisible
import oliveira.fabio.challenge52.home.goalslists.donegoalslist.presentation.action.DoneGoalsActions
import oliveira.fabio.challenge52.home.goalslists.donegoalslist.presentation.action.DoneGoalsStateResources
import oliveira.fabio.challenge52.home.goalslists.donegoalslist.presentation.adapter.DoneGoalsAdapter
import oliveira.fabio.challenge52.home.goalslists.presentation.viewmodel.GoalsListsViewModel
import oliveira.fabio.challenge52.model.vo.ActivityResultValueObject
import oliveira.fabio.challenge52.presentation.view.StateView
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

internal class DoneGoalsListFragment : Fragment(R.layout.fragment_done_goals_list),
    DoneGoalsAdapter.OnClickGoalListener {

    private val goalsListsViewModel: GoalsListsViewModel by sharedViewModel()
    private val doneGoalsAdapter by lazy {
        DoneGoalsAdapter(
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

    override fun onClickGoal(goal: Goal) =
        openGoalDetailsActivity(goal)

    private fun init() {
        setupObservables()
        setupRecyclerView()
        setupSwipeRefreshLayout()
    }

    private fun setupObservables() {
        with(goalsListsViewModel) {
            doneGoalsViewState.observe(viewLifecycleOwner, Observer {
                showLoading(it.isLoading)
                showDoneGoalsList(it.isDoneGoalsListVisible)
                showStateView(stateViewEmpty, it.isEmptyStateVisible)
                showStateView(stateViewError, it.isErrorVisible)
            })
            doneGoalsActions.observe(viewLifecycleOwner, Observer {
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
                    is DoneGoalsActions.Empty -> {
                        doneGoalsAdapter.clearList()
                        setStateView(stateViewEmpty, it.doneGoalsStateResources)
                    }
                    is DoneGoalsActions.Error -> {
                        setStateView(stateViewError, it.doneGoalsStateResources) {
                            listDoneGoals()
                        }
                    }
                }
            })
        }
    }

    private fun setupRecyclerView() {
        rvDoneGoalsList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvDoneGoalsList.adapter = doneGoalsAdapter
    }

    private fun setupSwipeRefreshLayout() {
        with(srlDoneGoalsList) {
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
                goalsListsViewModel.listDoneGoals()
            }
        }
    }

    private fun showLoading(hasToShow: Boolean) {
        srlDoneGoalsList.isRefreshing = hasToShow
    }

    private fun showDoneGoalsList(hasToShow: Boolean) {
        rvDoneGoalsList.isVisible = hasToShow
    }

    private fun setStateView(
        stateView: StateView,
        doneGoalsStateResources: DoneGoalsStateResources,
        block: (() -> Unit)? = null
    ) {
        stateView.apply {
            setImage(doneGoalsStateResources.imageRes)
            setTitle(resources.getString(doneGoalsStateResources.titleRes))
            setDescription(resources.getString(doneGoalsStateResources.descriptionRes))
            block?.also {
                doneGoalsStateResources.buttonTextRes?.also { buttonText ->
                    setupButton(resources.getString(buttonText), it)
                }
            }
        }
    }

    private fun showSnackBar(message: String) =
        Snackbar.make(
            content,
            message,
            Snackbar.LENGTH_SHORT
        ).show()

    private fun openGoalDetailsActivity(goal: Goal) = startActivityForResult(
        Actions.openGoalDetails(requireContext()).putExtra(GOAL_TAG, goal)
            .putExtra(IS_FROM_DONE_GOALS, true),
        REQUEST_CODE_DETAILS
    )

    private fun showStateView(
        stateView: StateView,
        hasToShow: Boolean
    ) {
        if (hasToShow) stateView.doSlideDownAnimation()
        stateView.isVisible = hasToShow
    }

    companion object {
        private const val REQUEST_CODE_DETAILS = 400
        private const val GOAL_TAG = "GOAL"
        private const val HAS_CHANGED = "HAS_CHANGED"
        private const val IS_FROM_DONE_GOALS = "IS_FROM_DONE_GOALS"

        fun newInstance() =
            DoneGoalsListFragment()
    }

}
